package edu.stanford.bmir.protege.web.server.init;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class ConfigurationTasksModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<ConfigurationTask> multibinder = Multibinder.newSetBinder(binder(), ConfigurationTask.class);
        multibinder.addBinding().to(CheckWebProtegeDataDirectoryExists.class);
        multibinder.addBinding().to(CheckDataDirectoryIsReadableAndWritable.class);
        multibinder.addBinding().to(CheckPerspectivesDataExists.class);
        multibinder.addBinding().to(CheckMongoDBConnectionTask.class);
        multibinder.addBinding().to(WarmUpMetaProjectTask.class);
    }
}
