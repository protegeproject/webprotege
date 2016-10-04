package edu.stanford.bmir.protege.web.server.init;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.multibindings.ProvidesIntoSet;
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
    public ConfigurationTask provideCheckPerspectivesDataExists(CheckPerspectivesDataExists check) {
        return check;
    }

    @Provides(type = Provides.Type.SET)
    public ConfigurationTask provideCheckMongoDBConnectionTask(CheckMongoDBConnectionTask check) {
        return check;
    }
}
