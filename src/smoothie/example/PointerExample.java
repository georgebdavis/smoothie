package smoothie.example;

import com.google.common.base.Optional;
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
import smoothie.cp5.CP5PointerAdapter;
import smoothie.cp5.CP5Provider;
import smoothie.pointer.MousePointerService;
import smoothie.pointer.Pointer;
import smoothie.pointer.PointerService;

/**
 * Simple sketch demonstrating use of the Pointer adapter.
 *
 * @author georgebdavis@github
 */
public class PointerExample extends Sketch {
    static final Logger logger = LoggerFactory.getLogger(PointerExample.class);
    @Override protected Logger getLogger() { return logger; }

    /** Most recent pointer state. */
    protected Optional<Pointer> oPointer = Optional.absent();

    @Inject
    public PointerExample(EventBus bus, CP5Provider cp5provider, CP5PointerAdapter adapter) {
        super(bus);
    }

    @Subscribe @Override
    public void onDraw(ProcessingEvent.Draw drawEvent) {
        // clear frame
        final PApplet processing = drawEvent.processing;
        processing.background(0f);

        // nothing to do if no pointer yet
        if (oPointer.isPresent()) {
            final Pointer pointer = oPointer.get();

            // prepare style
            processing.pushStyle();
            processing.strokeWeight(5.0f);
            switch (pointer.state) {
                case ABSENT :
                    processing.stroke(processing.color(0,0,0,100));
                    break;
                case UP:
                    processing.stroke(processing.color(255,0,0,100));
                    break;
                case DOWN:
                    processing.stroke(processing.color(0,255,0,100));
                    break;
            }

            // draw under cursor
            drawEvent.processing.ellipse(pointer.x, pointer.y, 10, 10);

            // reset style
            processing.popStyle();
        }
    }

    @Subscribe
    public void onPointer(Pointer pointer) {
        // remember most recent pointer state
        this.oPointer = Optional.of(pointer);
    }

    public static void main(String[] args) {
        // create module & injector
        Module module = new AbstractModule () {
            @Override protected void configure() {
                // normal smoothie bindings
                install(new SmoothieModule());
                bind(PointerExample.class).asEagerSingleton();

                // eagerly bind PointerService so that it starts during setup
                bind(PointerService.class).to(MousePointerService.class).asEagerSingleton();
            }
        };
        Injector injector = Guice.createInjector(module);

        // create smoothlet
        Smoothlet smoothlet = injector.getInstance(Smoothlet.class);

        // run the smoothlet
        smoothlet.asApplication(300, 300);
    }
}
