package edu.stanford.bmir.protege.web.server.inject;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;

import javax.inject.Singleton;
import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
@Module
public class WebProtegePropertiesModule {

    @Provides
    @Singleton
    public WebProtegeProperties provideWebProtegeProperties(WebProtegePropertiesProvider povider) {
        return povider.get();
    }

    @Provides
    @ApplicationName
    public String provideApplicationName(ApplicationNameProvider provider) {
        return provider.get();
    }

    @Provides
    @ApplicationHost
    public String provideApplicationHost(ApplicationHostProvider provider) {
        return provider.get();
    }

    @Provides
    @AdminEmail
    public Optional<String> provideAdminEmail(AdminEmailProvider provider) {
        return provider.get();
    }
}
