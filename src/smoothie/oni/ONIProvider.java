package smoothie.oni;

import SimpleOpenNI.SimpleOpenNI;

import com.google.common.base.Optional;
import com.google.inject.Inject;

import processing.core.PApplet;
import smoothie.PAppletInactiveException;

/**
 * OpenNI cannot be injected directly because its instantiation requires an active PApplet.
 * This class is an injectable provider that makes that requirement explicit.
 *
 * @author georgebdavis@github
 */
public class ONIProvider {
    /** PApplet with which to associate provider. */
    final PApplet processing;

    /** ControlP5 to provide (if instantiated). */
    Optional<SimpleOpenNI> oONI = Optional.absent();

    @Inject
    public ONIProvider (PApplet processing) {
        this.processing = processing;
    }

    /**
     * Provides a SimpleOpenNI instance associated with the provided PApplet.
     *
     * NOTE: Due to OpenNI instantiation logic, this can be performed only during / after the
     * PApplet's setup phase.  Calling an an inactive PApplet will throw an exception.
     *
     * @return OpenNI instance
     * @throws PAppletInactiveException if underlying PApplet is not active
     */
    synchronized public SimpleOpenNI getONI() throws PAppletInactiveException {
        if (oONI.isPresent()) return oONI.get();
        else {
            if (processing.isDisplayable()) {
                SimpleOpenNI oni = new SimpleOpenNI(processing);

                // enable features
                oni.setMirror(true);
                oni.enableDepth();
                oni.enableGesture();
                oni.enableHands();

                oONI = Optional.of(oni);
            } else {
                throw new PAppletInactiveException(processing);
            }
        }
        return oONI.get();
    }
}
