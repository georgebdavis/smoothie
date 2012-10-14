package smoothie.oni;

import SimpleOpenNI.SimpleOpenNI;
import SimpleOpenNI.XnVFlowRouter;
import SimpleOpenNI.XnVHandPointContext;
import SimpleOpenNI.XnVPointControl;
import SimpleOpenNI.XnVPushDetector;
import SimpleOpenNI.XnVSessionManager;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import processing.core.PApplet;
import processing.core.PVector;
import smoothie.pointer.AbstractPointerService;
import smoothie.pointer.Pointer;

public class HandPointerService extends AbstractPointerService {
    protected static final Logger logger = LoggerFactory
            .getLogger(HandPointerService.class);

    /** OpenNI provider */
    final ONIProvider oniProvider;

    /** Processing applet. */
    final PApplet processing;

    /** OpenNI context. */
    SimpleOpenNI oni;

    /** NITE session manager */
    XnVSessionManager nite;

    /** NITE push detector */
    XnVPushDetector pushDetector;

    /** NITE flow manager for push detector */
    XnVFlowRouter pushRouter;

    /** NITE flow manager for point control detector */
    XnVFlowRouter pointRouter;

    /** Convenience class */
    NiteHandler niteHandler;

    @Inject
    public HandPointerService(EventBus eventBus, PApplet processing, ONIProvider oniProvider) {
        super(eventBus);
        this.oniProvider = oniProvider;
        this.processing = processing;
    }

    @Override
    protected void startUp() throws Exception {
        logger.info("Initializing OpenNI...");

        oni = oniProvider.getONI();
        oni.setSmoothingHands(.5f);

        // initialize NITE
        nite = oni.createSessionManager("Click,Wave", "RaiseHand");
        niteHandler = new NiteHandler();

        // point recognition
        pointRouter = new XnVFlowRouter();
        pointRouter.SetActive(niteHandler);
        nite.AddListener(pointRouter);

        // push recognition
        pushDetector = new XnVPushDetector();
        pushDetector.RegisterPush(niteHandler);
        pushRouter = new XnVFlowRouter();
        pushRouter.SetActive(pushDetector);
        nite.AddListener(pushRouter);

        // start thread
        new Thread("ONI Update Thread") {
            final long LOG_DELAY = 10000;
            @Override public void run() {
                startAndWait();
                logger.info("Starting update thread!");
                long lastLog = System.currentTimeMillis();
                int i = 0;
                while (isRunning()) {
                    long t = System.currentTimeMillis();
                    if (t - lastLog > LOG_DELAY) {
                        logger.info("Still updating ONI, " + i + " updates since last log.");
                        lastLog = t;
                        i = 0;
                    }
                    i+=1;

                    oni.update();
                    oni.update(nite);
                    try {
                        sleep(25);
                    } catch (InterruptedException e) {
                        logger.error("Interrupted!", e);
                    }
                }
                logger.info("Closed update thread.");
            }
        }.start();

        logger.info("HandPointerService started!");
    }

    @Override protected void shutDown() throws Exception {
        this.nite.EndSession();
    }

    /*
     * session callbacks
     */

    void onStartSession(PVector pos) {
        logger.info("onStartSession: " + pos);
    }

    void onEndSession() {
        logger.info("onEndSession: ");
    }

    void onFocusSession(String strFocus, PVector pos, float progress) {
        logger.info("onFocusSession: focus=" + strFocus + ",pos=" + pos + ",progress=" + progress);
    }

    /**
     * Helper class to track point position.
     *
     * @author georgebdavis@github
     */
    class NiteHandler extends XnVPointControl {
        synchronized public void onPush(float vel, float angle) {
            if (!isRunning()) return;

//            setPointer(new Pointer(aPointer.get().state.equals(Pointer.State.UP)?
//                    Pointer.State.DOWN:Pointer.State.UP, aPointer.get().x, aPointer.get().y));
            setPointer(new Pointer(Pointer.State.DOWN, aPointer.get().x, aPointer.get().y));
            setPointer(new Pointer(Pointer.State.UP, aPointer.get().x, aPointer.get().y));
            logger.info("Clicked: " + aPointer.get().toString());
        }

        @Override
        synchronized public void OnPointCreate(XnVHandPointContext cxt) {
            if (!isRunning()) return;

            logger.info("OnPointCreate, handId: " + cxt.getNID());
            setPointer(new Pointer(aPointer.get().state.equals(Pointer.State.UP)?
                    Pointer.State.DOWN:Pointer.State.UP, aPointer.get().x, aPointer.get().y));
        }

        @Override
        synchronized public void OnPointUpdate(XnVHandPointContext cxt) {
            if (!isRunning()) return;

            PVector screenPos = new PVector();
            oni.convertRealWorldToProjective(new PVector(cxt.getPtPosition().getX(),
                                                         cxt.getPtPosition().getY(),
                                                         cxt.getPtPosition().getZ()), screenPos);

            // resize
            int x = (int) screenPos.x * processing.width / oni.depthWidth();
            int y = (int) screenPos.y * processing.height / oni.depthHeight();
            screenPos = new PVector(screenPos.x, screenPos.y);
            setPointer(new Pointer(Pointer.State.UP, x, y));
        }

        @Override
        synchronized public void OnPointDestroy(long nID) {
            if (!isRunning()) return;
        }
    }
}
