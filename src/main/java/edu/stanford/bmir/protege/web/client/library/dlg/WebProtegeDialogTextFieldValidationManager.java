package edu.stanford.bmir.protege.web.client.library.dlg;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/10/2012
 */
public class WebProtegeDialogTextFieldValidationManager {

    private final ValueBoxBase<String> textBox;

    private final InlineLabel errorLabel;

    private final WebProtegeDialogInlineValidator<ValueBoxBase<String>> validator;

    public WebProtegeDialogTextFieldValidationManager(ValueBoxBase<String> textBox, final WebProtegeDialogInlineValidator<ValueBoxBase<String>> validator) {
        this.textBox = textBox;
        this.errorLabel = new InlineLabel();
        this.validator = validator;
        textBox.addKeyUpHandler(new KeyUpHandler() {
            public void onKeyUp(KeyUpEvent event) {
                if (validator.shouldCheckOnKeyUp()) {
                    runValidation();
                }
            }
        });
        textBox.addValueChangeHandler(new ValueChangeHandler<String>() {
            public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent) {
                if (validator.shouldCheckOnValueChange()) {
                    runValidation();
                }
            }
        });

        errorLabel.addStyleName("web-protege-red-foreground");
        errorLabel.setText("");
        errorLabel.setVisible(false);
    }

    public Widget getErrorWidget() {
        return errorLabel;
    }

    public InlineValidationResult runValidation() {
        InlineValidationResult validation = validator.getValidation(textBox);
        if (validation.isInvalid()) {
            errorLabel.setText(validation.getMessage());
            errorLabel.setVisible(true);
        }
        else {
            errorLabel.setText("");
            errorLabel.setVisible(false);
        }
        return validation;
    }
}
