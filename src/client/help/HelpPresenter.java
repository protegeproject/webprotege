package edu.stanford.bmir.protege.web.client.help;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import edu.stanford.bmir.protege.web.client.app.Presenter;
import edu.stanford.bmir.protege.web.shared.HasDispose;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public class HelpPresenter implements HasDispose, Presenter {

    private final HelpView helpView;

    @Inject
    public HelpPresenter(HelpView helpView) {
        this.helpView = helpView;
    }

    public void start(@Nonnull AcceptsOneWidget container, @Nonnull EventBus eventBus) {
        container.setWidget(helpView);
    }

    public void dispose() {

    }
}
