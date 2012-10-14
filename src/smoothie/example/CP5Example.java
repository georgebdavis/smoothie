package smoothie.example;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;

import controlP5.ControlP5;
import controlP5.Textlabel;
import controlP5.Toggle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import smoothie.ProcessingEvent;
import smoothie.Sketch;
import smoothie.SmoothieModule;
import smoothie.Smoothlet;
import smoothie.cp5.CP5Module;
import smoothie.cp5.CP5PointerAdapter;
import smoothie.cp5.CP5Provider;
import smoothie.pointer.MousePointerService;
import smoothie.pointer.Pointer;
import smoothie.pointer.PointerService;

/**
 * A simple sketch demonstrating use of the CP5 adapters.
 *
 * @author georgebdavis@github
 */
public class CP5Example extends Sketch {
    static final Logger logger = LoggerFactory.getLogger(CP5Example.class);
    @Override protected Logger getLogger() { return logger; }

    /** Used to acquire the CP5 root to create widgets. */
    final CP5Provider cp5provider;

    /** CP5 root, used to manage widgets. */
    protected ControlP5 cp5;

    /** Toggle button controlling display of label. */
    protected Toggle xyToggle;

    /** Label demonstrating use of pointer events. */
    protected Textlabel label;

    @Inject
    public CP5Example(EventBus bus, CP5Provider cp5provider, CP5PointerAdapter adapter) {
        super(bus);
        this.cp5provider = cp5provider;
    }

    @Subscribe @Override
    public void onSetup(ProcessingEvent.Setup setupEvent) {
        cp5 = cp5provider.getCP5();

        xyToggle = cp5.addToggle("Show X,Y")
                      .setValue(false);

        label = cp5.addTextlabel("coordinates", "", 75, 75);
    }

    @Subscribe @Override
    public void onDraw(ProcessingEvent.Draw drawEvent) {
        // clear the frame
        drawEvent.processing.background(0);

        // toggle controls label visibility
        if (xyToggle.getState()) {
            label.setVisible(true);
        } else {
            label.setVisible(false);
        }
    }

    @Subscribe
    public void onPointer(Pointer pointer) {
        label.setText(String.format("%3d, %3d, %s", pointer.x, pointer.y, pointer.state));
    }

    public static void main(String[] args) {
        // create module & injector
        Module module = new AbstractModule () {
            @Override protected void configure() {
                // normal smoothie bindings
                install(new SmoothieModule());
                bind(CP5Example.class).asEagerSingleton();

                bind(PointerService.class).to(MousePointerService.class).asEagerSingleton();

                // configures CP5 adapters
                install(new CP5Module());
            }
        };
        Injector injector = Guice.createInjector(module);

        // create smoothlet
        Smoothlet smoothlet = injector.getInstance(Smoothlet.class);

        // run the smoothlet
        smoothlet.asApplication(300, 300);
    }
}
