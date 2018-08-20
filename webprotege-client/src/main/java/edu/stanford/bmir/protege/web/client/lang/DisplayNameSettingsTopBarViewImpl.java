package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;


import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29 Jul 2018
 */
public class DisplayNameSettingsTopBarViewImpl extends Composite implements DisplayNameSettingsTopBarView {

    private PopupPanel popupPanel;

    interface DisplayNameSettingsTopBarViewImplUiBinder extends UiBinder<HTMLPanel, DisplayNameSettingsTopBarViewImpl> {

    }

    private static DisplayNameSettingsTopBarViewImplUiBinder ourUiBinder = GWT.create(DisplayNameSettingsTopBarViewImplUiBinder.class);


    @UiField
    Button button;

    @Nonnull
    private final DisplayNameSettingsPresenter editorPresenter;

    @Inject
    public DisplayNameSettingsTopBarViewImpl(@Nonnull DisplayNameSettingsPresenter editorPresenter) {
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