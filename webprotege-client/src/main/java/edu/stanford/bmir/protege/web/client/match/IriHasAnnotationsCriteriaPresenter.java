package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class IriHasAnnotationsCriteriaPresenter implements CriteriaPresenter {

    @Nonnull
    private final AnnotationCriteriaPresenter delegate;

    @Inject
    public IriHasAnnotationsCriteriaPresenter(@Nonnull AnnotationCriteriaPresenter delegate) {
        this.delegate = checkNotNull(delegate);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        delegate.start(container);
    }

    @Override
    public void stop() {
        delegate.stop();
    }
}
