package smoothie;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import processing.core.PApplet;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Processing Mouse Service.
 * <p>
 * Listens to a processing applet and publishes mouse events onto a Guava EventBus.
 */
public class PMouseService extends AbstractIdleService implements MouseListener, MouseMotionListener {
    protected static final Logger logger = LoggerFactory.getLogger(PMouseService.class);

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
    public PMouseService (PApplet processing, EventBus eventBus) {
        this.processing = processing;
        this.eventBus = eventBus;
    }

    /** @return associated PApplet instance */
    public PApplet getProcessing() {
        return processing;
    }

    /** @return associated EventBus instance */
    public EventBus getEventBus() {
        return eventBus;
    }

    @Override protected void startUp() throws Exception {
        processing.addMouseListener(this);
        processing.addMouseMotionListener(this);
    }

    @Override protected void shutDown() throws Exception {
        processing.removeMouseListener(this);
        processing.removeMouseMotionListener(this);
    }

    synchronized public void mouseClicked(MouseEvent event) {
        eventBus.post(new PMouseEvent.Clicked(processing, event));
    }

    synchronized public void mouseEntered(MouseEvent event) {
        eventBus.post(new PMouseEvent.Entered(processing, event));
    }

    synchronized public void mouseExited(MouseEvent event) {
        eventBus.post(new PMouseEvent.Exited(processing, event));
    }

    synchronized public void mousePressed(MouseEvent event) {
        eventBus.post(new PMouseEvent.Pressed(processing, event));
    }

    synchronized public void mouseReleased(MouseEvent event) {
        eventBus.post(new PMouseEvent.Released(processing, event));
    }

    synchronized public void mouseDragged(MouseEvent event) {
        eventBus.post(new PMouseEvent.Dragged(processing, event));
    }

    synchronized public void mouseMoved(MouseEvent event) {
        eventBus.post(new PMouseEvent.Moved(processing, event));
    }
}
