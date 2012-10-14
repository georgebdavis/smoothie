package smoothie.example;

import SimpleOpenNI.SimpleOpenNI;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import processing.core.PApplet;
import smoothie.ProcessingEvent;
import smoothie.Sketch;
import smoothie.SmoothieModule;
import smoothie.Smoothlet;
import smoothie.oni.HandPointerService;
import smoothie.oni.ONIModule;
import smoothie.oni.ONIProvider;
import smoothie.pointer.Pointer;
import smoothie.pointer.PointerService;

/**
 * A simple sketch demonstrating use of the CP5 adapters.
 *
 * @author georgebdavis@github
 */
public class ONIExample extends Sketch {
    static final Logger logger = LoggerFactory.getLogger(ONIExample.class);
    @Override protected Logger getLogger() { return logger; }

    /** processing instance */
    final PApplet processing;

    /** Used to acquire the ONI context. */
    final ONIProvider oniProvider;

    /** ONI context. */
    protected SimpleOpenNI oni;

    /** last pointer */
    private Pointer pointer = new Pointer(Pointer.State.ABSENT, 0, 0);

    @Inject
    public ONIExample(EventBus bus, PApplet processing, ONIProvider oniProvider) {
        super(bus);
        this.oniProvider = oniProvider;
        this.processing = processing;
    }

    @Subscribe @Override
    public void onSetup(ProcessingEvent.Setup setupEvent) {
        oni = oniProvider.getONI();
//        pointerService.startAndWait();
        processing.resize(oni.depthWidth(), oni.depthHeight());
        processing.fill(processing.color(0,255,0));
        processing.smooth();
    }

    @Subscribe @Override
    public void onDraw(ProcessingEvent.Draw drawEvent) {
        processing.image(oni.depthImage(), 0, 0);
        processing.text(pointer.state.toString(), pointer.x, pointer.y);
        processing.ellipse(pointer.x, pointer.y, 5, 5);
    }

    @Subscribe
    public void onPointer(Pointer pointer) {
        this.pointer  = pointer;
        logger.info(pointer.toString());
    }

    public static void main(String[] args) {
        // create module & injector
        Module module = new AbstractModule () {
            @Override protected void configure() {
                // normal smoothie bindings
                install(new SmoothieModule());
                bind(ONIExample.class).asEagerSingleton();

                bind(PointerService.class).to(HandPointerService.class).asEagerSingleton();

                // configures CP5 adapters
                install(new ONIModule());
            }
        };
        Injector injector = Guice.createInjector(module);

        // create smoothlet
        Smoothlet smoothlet = injector.getInstance(Smoothlet.class);

        // run the smoothlet
        smoothlet.asApplication(640, 480);
    }
}
