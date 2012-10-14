package smoothie.oni;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;

/**
 * Exposes Smoothie components for easy injection.
 *
 * @author georgebdavis@github
 */
public class ONIModule extends AbstractModule {
    @Override protected void configure() {
        bind(ONIProvider.class).in(Scopes.SINGLETON);
    }

    @Provides Optional<ONIProvider> provideOptionalONIProvider(ONIProvider provider) {
        return Optional.of(provider);
    }
}
