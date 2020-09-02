package edu.stanford.bmir.protege.web.client.library.msgbox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBox;

import javax.inject.Inject;

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
    TextBox textBox;

    @UiField
    TextArea textArea;

    private boolean multiline = true;

    private static InputBoxViewImplUiBinder ourUiBinder = GWT.create(InputBoxViewImplUiBinder.class);

    @Inject
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
        if(multiline) {
            return textArea.getValue().trim();
        }
        else {
            return textBox.getValue().trim();
        }
    }

    @Override
    public java.util.Optional<HasRequestFocus> getInitialFocusable() {
        if(multiline) {
            return java.util.Optional.of(() -> textArea.setFocus(true));
        }
        else {
            return java.util.Optional.of(() -> textBox.setFocus(true));
        }
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    @Override
    public void setMultiline(boolean multiline) {
        if(multiline) {
            switchToMultiline();
        }
        else {
            switchToSingleLine();
        }
    }

    private void switchToSingleLine() {
        multiline = false;
        textBox.setVisible(true);
        textArea.setVisible(false);
        textBox.setValue(textArea.getValue());
    }

    private void switchToMultiline() {
        multiline = true;
        textArea.setVisible(false);
        textBox.setVisible(true);
        textArea.setValue(textBox.getValue());
    }

    @Override
    public void setInitialInput(String initialInput) {
        if(multiline) {
            textArea.setValue(initialInput);
        }
        else {
            textBox.setValue(initialInput);
        }
    }
}