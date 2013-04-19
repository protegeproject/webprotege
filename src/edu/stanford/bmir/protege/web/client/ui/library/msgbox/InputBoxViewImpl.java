package edu.stanford.bmir.protege.web.client.ui.library.msgbox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.web.bindery.requestfactory.gwt.rebind.model.HasExtraTypes;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.ui.library.text.ExpandingTextBox;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/04/2013
 */
public class InputBoxViewImpl extends Composite implements InputBoxView, HasInitialFocusable {

    interface InputBoxViewImplUiBinder extends UiBinder<HTMLPanel, InputBoxViewImpl> {

    }

    @UiField
    protected HasText messageLabel;

    @UiField
    protected ExpandingTextBox inputArea;

    private static InputBoxViewImplUiBinder ourUiBinder = GWT.create(InputBoxViewImplUiBinder.class);

    public InputBoxViewImpl() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @Override
    public void setMessage(String msg) {
        messageLabel.setText(msg);
    }

    @Override
    public String getInputValue() {
        return inputArea.getText().trim();
    }

    @Override
    public Focusable getInitialFocusable() {
        return inputArea;
    }

    @Override
    public Widget getWidget() {
        return this;
    }
}