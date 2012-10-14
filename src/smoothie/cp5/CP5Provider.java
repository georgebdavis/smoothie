package smoothie.cp5;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import controlP5.ControlP5;
import processing.core.PApplet;
import smoothie.PAppletInactiveException;

/**
 * ControlP5 cannot be injected directly because its instantiation requires an active PApplet.
 * This class is an injectable provider that makes that requirement explicit.
 *
 * @author georgebdavis@github
 */
public class CP5Provider {
    /** PApplet with which to associate provider. */
    final PApplet processing;

    /** ControlP5 to provide (if instantiated). */
    Optional<ControlP5> oCP5 = Optional.absent();

    @Inject
    public CP5Provider (PApplet processing) {
        this.processing = processing;
    }

    /**
     * Provides a ControlP5 instance associated with the provide PApplet.
     *
     * NOTE: Due to ControlP5 instantiation logic, this can be performed only during / after the
     * PApplet's setup phase.  Calling an an inactive PApplet will throw an exception.
     *
     * @return ControlP5 instance
     * @throws PAppletInactiveException if underlying PApplet is not active
     */
    synchronized public ControlP5 getCP5() throws PAppletInactiveException {
        if (oCP5.isPresent()) return oCP5.get();
        else {
            if (processing.isDisplayable()) {
                oCP5 = Optional.of(new ControlP5(processing));
            } else {
                throw new PAppletInactiveException(processing);
            }
        }
        return oCP5.get();
    }
}
