package edu.stanford.bmir.protege.web.client.place;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.web.bindery.event.shared.EventBus;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/02/16
 */
public class WebProtegeActivityManager extends ActivityManager {

    @Inject
    public WebProtegeActivityManager(ActivityMapper mapper, EventBus eventBus) {
        super(mapper, eventBus);
    }
}
