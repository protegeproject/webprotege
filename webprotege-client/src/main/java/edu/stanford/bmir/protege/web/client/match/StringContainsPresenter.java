package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class StringContainsPresenter implements CriteriaPresenter {

    @Nonnull
    private final SimpleStringCriteriaView view;

    @Inject
    public StringContainsPresenter(@Nonnull SimpleStringCriteriaView view) {
        this.view = view;
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void stop() {

    }
}
