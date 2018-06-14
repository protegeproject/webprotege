package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Label;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Jun 2018
 */
public class IsDeprecatedCriteriaPresenter implements CriteriaPresenter {

    @Inject
    public IsDeprecatedCriteriaPresenter() {
    }

    @Override
    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(new Label("Is Deprecated View"));
    }

    @Override
    public void stop() {

    }
}
