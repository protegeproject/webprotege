package edu.stanford.bmir.protege.web.client.app;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/02/16
 */
@Singleton
public class ApplicationPresenter {

    private final ApplicationView applicationView;

    @Inject
    public ApplicationPresenter(ApplicationView applicationView) {
        this.applicationView = checkNotNull(applicationView);
    }

    public ApplicationView getApplicationView() {
        return applicationView;
    }
}
