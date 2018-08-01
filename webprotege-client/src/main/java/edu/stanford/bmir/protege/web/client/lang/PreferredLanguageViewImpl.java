package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.lang.DisplayDictionaryLanguage;

import javax.annotation.Nonnull;
import javax.inject.Inject;


import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29 Jul 2018
 */
public class PreferredLanguageViewImpl extends Composite implements PreferredLanguageView {

    private PopupPanel popupPanel;

    interface PreferredLanguageViewImplUiBinder extends UiBinder<HTMLPanel, PreferredLanguageViewImpl> {

    }

    private static PreferredLanguageViewImplUiBinder ourUiBinder = GWT.create(PreferredLanguageViewImplUiBinder.class);


    @UiField
    Button button;

    @Nonnull
    private final DisplayLanguageEditorPresenter editorPresenter;

    @Inject
    public PreferredLanguageViewImpl(@Nonnull DisplayLanguageEditorPresenter editorPresenter) {
        this.editorPresenter = editorPresenter;
        popupPanel = new PopupPanel(true, true);
        popupPanel.addCloseHandler(event -> editorPresenter.stop());
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("button")
    public void buttonClick(ClickEvent event) {
        editorPresenter.start(popupPanel);
        popupPanel.showRelativeTo(button);
    }
}