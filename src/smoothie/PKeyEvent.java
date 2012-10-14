package smoothie;

import processing.core.PApplet;

import java.awt.event.KeyEvent;

/**
 * Exposes an input from a keyboard in a Guava EventBus-friendly manner.
 *
 * {@link smoothie.PKeyService} recovers and publishes these from a PApplet;
 *
 * @author george.b.davis@gmail.com
 */
public abstract class PKeyEvent extends ProcessingEvent {
    /** AWT Event */
    public final KeyEvent awtEvent;

    protected PKeyEvent(PApplet processing, KeyEvent awtEvent) {
        super(processing);
        this.awtEvent = awtEvent;
    }

    /** Encapsulates a keyPressed() callback. */
    public static class Pressed extends PKeyEvent {
        public Pressed(PApplet processing, KeyEvent awtEvent) {
            super(processing, awtEvent);
        }};

    /** Encapsulates a keyReleased() callback. */
    public static class Released extends PKeyEvent {
        public Released(PApplet processing, KeyEvent awtEvent) {
            super(processing, awtEvent);
        }};

    /** Encapsulates a keyTyped() callback. */
    public static class Typed extends PKeyEvent {
        public Typed(PApplet processing, KeyEvent awtEvent) {
            super(processing, awtEvent);
        }};
}
