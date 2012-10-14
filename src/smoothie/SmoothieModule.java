package smoothie;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import processing.core.PApplet;

/**
 * Exposes Smoothie components for easy injection.
 *
 * @author george.b.davis@gmail.com *
 */
public class SmoothieModule extends AbstractModule {
    @Override protected void configure() {
        bind(EventBus.class).in(Scopes.SINGLETON);
        bind(Smoothlet.class).in(Scopes.SINGLETON);
        bind(PApplet.class).to(Smoothlet.class);
        bind(PMouseService.class).in(Scopes.SINGLETON);
        bind(PKeyService.class).in(Scopes.SINGLETON);
    }
}
