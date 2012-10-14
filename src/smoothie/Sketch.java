package smoothie;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import org.slf4j.Logger;

public abstract class Sketch {
    protected abstract Logger getLogger();

    /**
     * Code to be executed during PApplet's setup phase.
     *
     * NOTE: Overrides should register on EventBus with @Subscribe
     *
     * @param setupEvent issued by PApplet
     */
    @Subscribe
    public void onSetup (ProcessingEvent.Setup setupEvent) {
        getLogger().info("Setting up...");
    }

    /**
     * Code to be executed during PApplet's draw phase.
     *
     * NOTE: Overrides should register on EventBus with @Subscribe
     *
     * @param drawEvent issued by PApplet
     */
    @Subscribe
    public void onDraw (ProcessingEvent.Draw drawEvent) {
        getLogger().debug(this.toString() + "Drawing...");
    }

    @Inject
    public Sketch (EventBus bus) {
        getLogger().debug("Sketch registering with eventbus.");
        bus.register(this);
    }
}
