package smoothie;

import processing.core.PApplet;

import java.awt.event.MouseEvent;

/**
 * Exposes an input from a mouse or similar pointer in a Guava EventBus-friendly manner.
 *
 * {@link smoothie.PMouseService} recovers and publishes these from a PApplet;
 *
 * @author george.b.davis@gmail.com
 */
public abstract class PMouseEvent extends ProcessingEvent {
    /** AWT Event */
    public final MouseEvent awtEvent;

    protected PMouseEvent(PApplet processing, MouseEvent awtEvent) {
        super(processing);
        this.awtEvent = awtEvent;
    }

    /** Encapsulates a mouseClicked() callback. */
    public static class Clicked extends PMouseEvent {
        public Clicked(PApplet processing, MouseEvent awtEvent) {
            super(processing, awtEvent);
        }};

    /** Encapsulates a mouseEntered() callback. */
    public static class Entered extends PMouseEvent {
        public Entered(PApplet processing, MouseEvent awtEvent) {
            super(processing, awtEvent);
        }};

    /** Encapsulates a mouseExited() callback. */
    public static class Exited extends PMouseEvent {
        public Exited(PApplet processing, MouseEvent awtEvent) {
            super(processing, awtEvent);
        }};

    /** Encapsulates a mousePressed() callback. */
    public static class Pressed extends PMouseEvent {
        public Pressed(PApplet processing, MouseEvent awtEvent) {
            super(processing, awtEvent);
        }};

    /** Encapsulates a mouseReleased() callback. */
    public static class Released extends PMouseEvent {
        public Released(PApplet processing, MouseEvent awtEvent) {
            super(processing, awtEvent);
        }};

    /** Encapsulates a mouseDragged() callback. */
    public static class Dragged extends PMouseEvent {
        public Dragged(PApplet processing, MouseEvent awtEvent) {
            super(processing, awtEvent);
        }};

    /** Encapsulates a mouseMoved() callback. */
    public static class Moved extends PMouseEvent {
        public Moved(PApplet processing, MouseEvent awtEvent) {
            super(processing, awtEvent);
        }};
}
