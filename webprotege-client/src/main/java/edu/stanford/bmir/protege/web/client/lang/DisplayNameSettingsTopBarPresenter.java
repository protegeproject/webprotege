package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.user.client.ui.AcceptsOneWidget;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29 Jul 2018
 */
public class DisplayNameSettingsTopBarPresenter {

    @Nonnull
    private final DisplayNameSettingsTopBarView view;

    @Inject
    public DisplayNameSettingsTopBarPresenter(@Nonnull DisplayNameSettingsTopBarView view) {
        this.view = checkNotNull(view);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }
}
