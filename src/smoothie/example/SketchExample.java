package smoothie.example;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import smoothie.ProcessingEvent;
import smoothie.Sketch;
import smoothie.SmoothieModule;
import smoothie.Smoothlet;


/**
 * Simple sketch to demonstrate smoothie usage.
 *
 * @author georgebdavis@github
 */
public class SketchExample extends Sketch {
    static final Logger logger = LoggerFactory.getLogger(SketchExample.class);
    @Override protected Logger getLogger() { return logger; }

    /** x-coordinate of ellipse center */
    protected float x;

    /** y-coordinate of ellipse center */
    protected float y;

    /** radius scale of ellipse */
    protected float r;

    @Inject
    public SketchExample(EventBus bus) {
        super(bus);
    }

    @Subscribe @Override
    public void onSetup(ProcessingEvent.Setup setupEvent) {
        // during setup phase, determine key coordinates
        x = setupEvent.processing.width / 2;
        y = setupEvent.processing.height / 2;
        r = Math.min(setupEvent.processing.width, setupEvent.processing.height);
        logger.debug(String.format("Setup - x: %.0f, y: %.0f, r: %.0f", x, y, r));
    }

    @Subscribe @Override
    public void onDraw(ProcessingEvent.Draw drawEvent) {
        // during draw phase, add on an ellipse with random x and y coordinates
        drawEvent.processing.ellipse(x, y, (float) Math.random()*r, (float)  Math.random()*r);
    }

    public static void main(String[] args) {
        Module module = new AbstractModule () {
            @Override protected void configure() {
                // the smoothie module is required to set up a smoothlet
                install(new SmoothieModule());

                // always bind the sketch eagerly so that it is created before smoothlet starts
                bind(SketchExample.class).asEagerSingleton();
            }
        };
        Smoothlet smoothlet = Guice.createInjector(module).getInstance(Smoothlet.class);

        // since the sketch is listening to the smoothlet, all we have to do is start it
        smoothlet.asApplication(300, 300);
    }
}
