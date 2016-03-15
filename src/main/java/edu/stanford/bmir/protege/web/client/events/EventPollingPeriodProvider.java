package edu.stanford.bmir.protege.web.client.events;

import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 15/03/16
 */
public class EventPollingPeriodProvider implements Provider<Integer> {

    public static final int TEN_SECONDS = 10 * 1000;

    @Override
    public Integer get() {
        return TEN_SECONDS;
    }
}
