package smoothie;

import processing.core.PApplet;

/**
 * Parent class for events originating from Processing.
 *
 * @author george.b.davis@gmail.com
 */
public abstract class ProcessingEvent {
    public final PApplet processing;

    public ProcessingEvent(PApplet processing) {
        this.processing = processing;
    }

    public static class Setup extends ProcessingEvent {
        public Setup(PApplet processing) {
            super(processing);
        }};

    public static class Draw extends ProcessingEvent {
        public Draw(PApplet processing) {
            super(processing);
        }};
}
