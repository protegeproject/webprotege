package edu.stanford.bmir.protege.web.server.inject;

import com.google.inject.AbstractModule;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import edu.stanford.bmir.protege.web.server.app.WebProtegeApplicationConfig;
import edu.stanford.bmir.protege.web.server.crud.persistence.ProjectEntityCrudKitSettingsRepository;
import edu.stanford.bmir.protege.web.server.issues.IssueRepository;
import edu.stanford.bmir.protege.web.server.permissions.ProjectPermissionRecordRepository;
import edu.stanford.bmir.protege.web.server.permissions.WorldProjectPermissionRecordRepository;
import edu.stanford.bmir.protege.web.server.project.ProjectDetailsRepository;
import edu.stanford.bmir.protege.web.server.user.UserRecordRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static com.google.inject.Scopes.SINGLETON;

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

        bind(MongoClient.class).toProvider(MongoClientProvider.class).asEagerSingleton();

        bind(MongoDatabase.class).toProvider(MongoDatabaseProvider.class);

        bind(ProjectEntityCrudKitSettingsRepository.class).in(SINGLETON);

        bind(UserRecordRepository.class).in(SINGLETON);

        bind(ProjectDetailsRepository.class).in(SINGLETON);

        bind(ProjectPermissionRecordRepository.class)
                .toInstance(applicationContext.getBean(ProjectPermissionRecordRepository.class));

        bind(WorldProjectPermissionRecordRepository.class)
                .toInstance(applicationContext.getBean(WorldProjectPermissionRecordRepository.class));

        bind(IssueRepository.class)
                .toInstance(applicationContext.getBean(IssueRepository.class));
    }
}
