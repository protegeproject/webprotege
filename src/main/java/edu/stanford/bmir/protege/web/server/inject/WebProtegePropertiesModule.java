package edu.stanford.bmir.protege.web.server.inject;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.shared.app.ApplicationScheme;

import javax.inject.Singleton;
import java.util.Optional;

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
    public String provideApplicationName(WebProtegeProperties properties) {
        return properties.getApplicationName();
    }

    @Provides
    public ApplicationScheme provideApplicationScheme(WebProtegeProperties properties) {
        return properties.getApplicationScheme();
    }

    @Provides
    @ApplicationHost
    public String provideApplicationHost(WebProtegeProperties properties) {
        return properties.getApplicationHost();
    }


    @Provides
    @ApplicationPort
    public Optional<Integer> provideApplicationPort(WebProtegeProperties properties) {
        return properties.getApplicationPort();
    }

    @Provides
    @ApplicationPath
    public String provideApplicationPath(WebProtegeProperties properties) {
        return properties.getApplicationPath().orElse("");
    }

    @Provides
    @AdminEmail
    public Optional<String> provideAdminEmail(WebProtegeProperties properties) {
        return properties.getAdministratorEmail();
    }

}
