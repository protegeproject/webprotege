package edu.stanford.bmir.protege.web.client.help;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.HasDispose;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public class HelpPresenter implements HasDispose {

    private final HelpView helpView;

    @Inject
    public HelpPresenter(HelpView helpView) {
        this.helpView = helpView;
    }

    public void start(AcceptsOneWidget container) {
        container.setWidget(helpView);
    }

    public void dispose() {

    }
}
