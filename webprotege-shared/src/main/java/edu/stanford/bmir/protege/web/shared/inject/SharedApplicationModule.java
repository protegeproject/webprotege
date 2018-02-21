package edu.stanford.bmir.protege.web.shared.inject;

import dagger.Module;
import dagger.Provides;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Mar 2017
 */
@Module
public class SharedApplicationModule {

    @Provides
    @EventPollingPeriod
    int provideEventPollingPeriod(EventPollingPeriodProvider provider) {
        return provider.get();
    }
}
