package smoothie.pointer;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import processing.core.PApplet;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Captures AWT MouseEvents from a PApplet, translates to Pointer, and publishes to an event bus.
 */
public class MousePointerService extends AbstractPointerService implements MouseListener,
                                                                           MouseMotionListener {
    protected static final Logger logger = LoggerFactory.getLogger(MousePointerService.class);

    /** Processing applet to get events from. */
    protected final PApplet processing;

    /** Used to capture whether event is within the screen. */
    protected boolean present = false;

    @Inject
    public MousePointerService (PApplet processing, EventBus eventBus) {
        super(eventBus);
        this.processing = processing;
    }

    @Override
    protected void startUp() throws Exception {
        logger.info("Initializing mouse pointer service.");
        processing.addMouseListener(this);
        processing.addMouseMotionListener(this);
    }

    @Override
    protected void shutDown() throws Exception {
        logger.info("Mouse pointer service shutting down.");
        processing.removeMouseListener(this);
        processing.removeMouseMotionListener(this);
    }


    /*
     * LISTENER IMPLEMENTATIONS
     */

    synchronized protected void onMouseEvent(MouseEvent event) {
        // determine state
        Pointer.State state = null;
        if (!present) state = Pointer.State.ABSENT;
        else if ((event.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) > 0) state = Pointer.State.DOWN;
        else state = Pointer.State.UP;

        setPointer(new Pointer(state, event.getX(), event.getY()));
    }

    synchronized public void mouseClicked(MouseEvent event) {
        this.onMouseEvent(event);
    }

    synchronized public void mouseEntered(MouseEvent event) {
        present = true;
        this.onMouseEvent(event);
    }

    synchronized public void mouseExited(MouseEvent event) {
        present = false;
        this.onMouseEvent(event);
    }

    synchronized public void mousePressed(MouseEvent event) {
        this.onMouseEvent(event);
    }

    synchronized public void mouseReleased(MouseEvent event) {
        this.onMouseEvent(event);
    }

    synchronized public void mouseDragged(MouseEvent event) {
        this.onMouseEvent(event);
    }

    synchronized public void mouseMoved(MouseEvent event) {
        this.onMouseEvent(event);
    }
}
