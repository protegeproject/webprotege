package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/01/2013
 */
public class CreateEntityEditor extends FlowPanel implements CreateEntityPresenter {

    @UiField
    protected TextBox browserTextField;

    interface CreateEntityEditorUiBinder extends UiBinder<HTMLPanel, CreateEntityEditor> {

    }

    private static CreateEntityEditorUiBinder ourUiBinder = GWT.create(CreateEntityEditorUiBinder.class);

    public CreateEntityEditor() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        add(rootElement);
    }


    @Override
    public Widget getWidget() {
        return this;
    }

    @Override
    public String getBrowserText() {
        return browserTextField.getText().trim();
    }

    @Override
    public Focusable getInitialFocusable() {
        return browserTextField;
    }

}