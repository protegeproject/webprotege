package edu.stanford.bmir.protege.web.server.init;

import dagger.Module;
import dagger.Provides;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
@Module
public class ConfigurationTasksModule {

    @Provides(type = Provides.Type.SET)
    public ConfigurationTask provideCheckWebProtegeDataDirectoryExists(CheckWebProtegeDataDirectoryExists check) {
        return check;
    }

    @Provides(type = Provides.Type.SET)
    public ConfigurationTask provideCheckDataDirectoryIsReadableAndWritable(CheckDataDirectoryIsReadableAndWritable check) {
        return check;
    }

    @Provides(type = Provides.Type.SET)
    public ConfigurationTask provideCheckMongoDBConnectionTask(CheckMongoDBConnectionTask check) {
        return check;
    }
}
