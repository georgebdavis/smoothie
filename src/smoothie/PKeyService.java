package smoothie;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import processing.core.PApplet;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Processing Key Service.
 * <p>
 * Listens to a processing applet and publishes mouse events onto a Guava EventBus.
 */
public class PKeyService extends AbstractIdleService implements KeyListener {
    protected static final Logger logger = LoggerFactory.getLogger(PKeyService.class);

    /** Processing applet to get events from. */
    protected final PApplet processing;

    /** Event bus on which to publish. */
    protected final EventBus eventBus;

    /**
     * Create a new MouseService.
     *
     * It is recommended this be managed by Guice injection;
     * see {@link smoothie.PBModule}.
     *
     * @param processing - applet on which to listen to events
     * @param eventBus - bus on which to publish events
     */
    @Inject
    public PKeyService (PApplet processing, EventBus eventBus) {
        this.processing = processing;
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    /** @return asssociated PApplet instance */
    public PApplet getProcessing() {
        return processing;
    }

    /** @return asssociated EventBus instance */
    public EventBus getEventBus() {
        return eventBus;
    }
    
    /**
     * Automatically start if bound to Smoothlet that issues Setup event.
     *
     * TODO (gbd): Is this the right thing to do?  Other PointerServices might have other
     * dependencies (e.g. Kinect), requiring different timing and breaking the usage pattern.
     *
     * @param setupEvent issued by Smoothlet
     */
    @Subscribe
    public void onSetup(ProcessingEvent.Setup setupEvent) {
        this.start();
    }

    @Override protected void startUp() throws Exception {
        processing.addKeyListener(this);
    }

    @Override protected void shutDown() throws Exception {
        processing.removeKeyListener(this);
    }

    public void keyPressed(KeyEvent event) {
        eventBus.post(new PKeyEvent.Pressed(processing, event));
    }

    public void keyReleased(KeyEvent event) {
        eventBus.post(new PKeyEvent.Released(processing, event));
    }

    public void keyTyped(KeyEvent event) {
        eventBus.post(new PKeyEvent.Typed(processing, event));
    }
}
