package smoothie;

import processing.core.PApplet;

/**
 * Flags that an operation requiring an active PApplet has been requested in relation to an
 * inactive one.
 *
 * Third party libraries such as ControlP5 have operations that they assume will take place during
 * the setup() phase, making it difficult to decouple for injection and event logic.  This exception
 * makes it easier to wrap those operations and provide a more explicit error.
 *
 * @author gbd
 */
public class PAppletInactiveException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public final PApplet processing;

    public PAppletInactiveException(PApplet processing) {
        super(processing +
              " inactive.  Operation must be invoked during or after PApplet setup()!");
        this.processing = processing;
    }
}
