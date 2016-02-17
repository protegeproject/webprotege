package edu.stanford.bmir.protege.web.client.help;

import com.google.gwt.user.client.ui.AcceptsOneWidget;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/02/16
 */
public class HelpPresenter {

    private final HelpView helpView;

    @Inject
    public HelpPresenter(HelpView helpView) {
        this.helpView = helpView;
    }

    public void start(AcceptsOneWidget container) {
        container.setWidget(helpView);
    }
}
