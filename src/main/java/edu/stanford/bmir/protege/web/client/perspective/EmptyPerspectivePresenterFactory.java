package edu.stanford.bmir.protege.web.client.perspective;

import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/16
 */
public class EmptyPerspectivePresenterFactory {

    @Nonnull
    private final EmptyPerspectiveView view;

    @Nonnull
    private final EventBus eventBus;

    @Inject
    public EmptyPerspectivePresenterFactory(@Nonnull EmptyPerspectiveView view,
                                            @Nonnull EventBus eventBus) {
        this.view = view;
        this.eventBus = eventBus;
    }

    public EmptyPerspectivePresenter createEmptyPerspectivePresenter(PerspectiveId perspectiveId) {
        return new EmptyPerspectivePresenter(perspectiveId, view, eventBus);
    }
}
