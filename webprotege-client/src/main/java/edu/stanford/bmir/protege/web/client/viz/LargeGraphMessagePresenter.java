package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-12
 */
public class LargeGraphMessagePresenter {

    @Nonnull
    private final LargeGraphMessageView view;

    @Inject
    public LargeGraphMessagePresenter(@Nonnull LargeGraphMessageView view) {
        this.view = checkNotNull(view);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void setMessage(@Nonnull OWLEntityData entity,
                           int nodeCount,
                           int edgeCount) {
        view.setDisplayMessage(entity, nodeCount, edgeCount);
    }

    public void setDisplayGraphHandler(Runnable displayGraphHandler) {
        view.setDisplayGraphHandler(checkNotNull(displayGraphHandler));
    }

    public void setDisplaySettingsHandler(Runnable displaySettingsHandler) {
        view.setDisplaySettingsHandler(checkNotNull(displaySettingsHandler));
    }

}
