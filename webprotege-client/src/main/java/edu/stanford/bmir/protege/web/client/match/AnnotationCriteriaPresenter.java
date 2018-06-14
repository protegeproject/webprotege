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
public class AnnotationCriteriaPresenter implements CriteriaPresenter {

    @Nonnull
    private final AnnotationCriteriaView view;

    @Nonnull
    private final AnnotationValueCriteriaPresenter valuePresenter;

    @Inject
    public AnnotationCriteriaPresenter(@Nonnull AnnotationCriteriaView view, @Nonnull AnnotationValueCriteriaPresenter valuePresenter) {
        this.view = checkNotNull(view);
        this.valuePresenter = checkNotNull(valuePresenter);
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        valuePresenter.start(view.getValueCriteriaViewContainer());
    }

    @Override
    public void stop() {

    }
}
