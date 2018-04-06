package edu.stanford.bmir.protege.web.server.inject;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import edu.stanford.bmir.protege.web.server.init.CheckDataDirectoryIsReadableAndWritable;
import edu.stanford.bmir.protege.web.server.init.CheckMongoDBConnectionTask;
import edu.stanford.bmir.protege.web.server.init.CheckWebProtegeDataDirectoryExists;
import edu.stanford.bmir.protege.web.server.init.ConfigurationTask;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
@Module
public class ConfigurationTasksModule {

    @Provides @IntoSet
    public ConfigurationTask provideCheckWebProtegeDataDirectoryExists(CheckWebProtegeDataDirectoryExists check) {
        return check;
    }

    @Provides @IntoSet
    public ConfigurationTask provideCheckDataDirectoryIsReadableAndWritable(CheckDataDirectoryIsReadableAndWritable check) {
        return check;
    }

    @Provides @IntoSet
    public ConfigurationTask provideCheckMongoDBConnectionTask(CheckMongoDBConnectionTask check) {
        return check;
    }
}
