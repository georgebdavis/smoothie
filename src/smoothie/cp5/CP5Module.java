package smoothie.cp5;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * Exposes Smoothie components for easy injection.
 *
 * @author georgebdavis@github
 */
public class CP5Module extends AbstractModule {
    @Override protected void configure() {
        bind(CP5Provider.class).in(Scopes.SINGLETON);
        bind(CP5PointerAdapter.class).asEagerSingleton();
    }
}
