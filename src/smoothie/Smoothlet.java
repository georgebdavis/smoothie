package smoothie;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import processing.core.PApplet;

import java.awt.Frame;

/**
 * Smoothlet extends PApplet to publish draw cycle events to a Guava EventBus provided at
 * instantiation.  Sketch components can be initialized or injected independently and react to the
 * bus rather than being explicitly incorporated.
 *
 * @author george.b.davis@gmail.com
 */
@SuppressWarnings("serial")
public class Smoothlet extends PApplet {
    static final Logger logger = LoggerFactory.getLogger(Smoothlet.class);

    /** EventBus on which to publish lifecycle events. */
    protected final EventBus eventBus;

    /** One setup event to reuse during our lifecycle. */
    protected final ProcessingEvent.Setup setup = new ProcessingEvent.Setup(this);

    /** One draw event to reuse during our lifecycle. */
    protected final ProcessingEvent.Draw draw = new ProcessingEvent.Draw(this);

    @Override public void setup() {
        logger.debug("Initializing Smoothlet!");
        smooth();
        eventBus.post(setup);
    }

    @Override public void draw() {
        eventBus.post(draw);
    }

    /** @param eventBus on which to publish lifecycle events */
    @Inject
    public Smoothlet (EventBus eventBus) {
        super();
        this.eventBus = eventBus;

        // instantiate frame to allow frame-dependent components to be instantiated.
        frame = new Frame();
    }

    public void asApplication(int width, int height) {
        setSize(width, height);
        frame.setSize(width, height);
        frame.add(this);
        frame.setVisible(true);
        init();
    }
}
