package smoothie.cp5;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;

import controlP5.ControlP5;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import smoothie.ProcessingEvent;
import smoothie.pointer.Pointer;

/**
 * Publishes Pointer events from the Smoothie eventbus to CP5.
 */
public class CP5PointerAdapter extends AbstractIdleService {
    protected static final Logger logger = LoggerFactory.getLogger(CP5PointerAdapter.class);

    /** EventBus on which to listen for Pointer events. */
    protected final EventBus eventBus;

    /** ControlP5 provider. */
    protected final CP5Provider cp5provider;

    /** ControlP5 instance on which to publish adapted events. */
    protected ControlP5 cp5;

    /** Previous pointer. */
    protected Pointer previous = null;

    /**
     * Automatically start if bound to Smoothlet that issues Setup event.
     *
     * TODO (gbd): Is this the right thing to do?
     *
     * @param setupEvent issued by Smoothlet
     */
    @Subscribe
    public void onSetup(ProcessingEvent.Setup setupEvent) {
        this.start();
    }

    @Subscribe
    public void onPointer(Pointer pointer) {
        if (this.isRunning()) {
            cp5.getPointer().set(pointer.x, pointer.y);

            if (pointer.state.equals(Pointer.State.DOWN) &&
                    (previous == null || (previous.state != Pointer.State.DOWN))) {
                cp5.getPointer().pressed();
            }

            if (pointer.state.equals(Pointer.State.UP) &&
                    (previous == null || (previous.state != Pointer.State.UP))) {
                cp5.getPointer().released();
            }

            previous = pointer;
        }
    }

    @Override
    protected void startUp() throws Exception {
        logger.info("Starting CP5 Pointer adapter.");
        cp5 = cp5provider.getCP5();
        cp5.getPointer().enable();
    }

    @Override
    protected void shutDown() throws Exception {
        logger.info("Stopping CP5 Pointer adapter.");
        eventBus.unregister(this);
        cp5.getPointer().disable();
    }

    @Inject
    public CP5PointerAdapter(EventBus eventBus, CP5Provider cp5provider) {
        this.eventBus = eventBus;
        this.cp5provider = cp5provider;
        eventBus.register(this);
    }
}
