package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.AbstractModule;
import edu.stanford.bmir.protege.web.server.app.WebProtegeApplicationConfig;
import edu.stanford.bmir.protege.web.server.crud.persistence.ProjectEntityCrudKitSettingsRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/05/15
 *
 * A module for binding Spring Data Repositories.
 */
public class RepositoryModule extends AbstractModule {

    @Override
    protected void configure() {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(WebProtegeApplicationConfig.class);

        bind(ApplicationContext.class).toInstance(applicationContext);

        bind(ProjectEntityCrudKitSettingsRepository.class)
                .toInstance(applicationContext.getBean(ProjectEntityCrudKitSettingsRepository.class));
    }
}
