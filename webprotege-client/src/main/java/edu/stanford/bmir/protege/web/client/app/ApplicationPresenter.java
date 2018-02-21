package edu.stanford.bmir.protege.web.client.app;

import edu.stanford.bmir.protege.web.client.place.WindowTitleUpdater;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11/02/16
 */
@ApplicationSingleton
public class ApplicationPresenter {

    @Nonnull
    private final ApplicationView applicationView;

    @Nonnull
    private final WindowTitleUpdater windowTitleUpdater;

    @Inject
    public ApplicationPresenter(ApplicationView applicationView, WindowTitleUpdater windowTitleUpdater) {
        this.applicationView = checkNotNull(applicationView);
        this.windowTitleUpdater = checkNotNull(windowTitleUpdater);
    }

    @Nonnull
    public ApplicationView getApplicationView() {
        return applicationView;
    }

    public void start() {
        windowTitleUpdater.start();
    }
}
