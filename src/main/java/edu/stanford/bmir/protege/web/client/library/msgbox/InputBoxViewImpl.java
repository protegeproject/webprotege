package edu.stanford.bmir.protege.web.client.library.msgbox;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBox;

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
    public Optional<Focusable> getInitialFocusable() {
        return Optional.of(inputArea);
    }

    @Override
    public Widget getWidget() {
        return this;
    }

    @Override
    public void setMultiline(boolean multiline) {
        inputArea.setMultiline(multiline);
    }

    @Override
    public void setInitialInput(String initialInput) {
        inputArea.setText(initialInput);
    }
}