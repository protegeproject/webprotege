package edu.stanford.bmir.protege.web.shared.inject;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/03/16
 */
public class EventPollingPeriodProvider implements Provider<Integer> {

    public static final int TEN_SECONDS = 10 * 1000;

    @Inject
    public EventPollingPeriodProvider() {
    }

    @Override
    public Integer get() {
        return TEN_SECONDS;
    }
}
