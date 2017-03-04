package edu.stanford.bmir.protege.web.client.library.dlg;

import com.google.gwt.user.client.ui.ValueBoxBase;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/10/2012
 */
public class NonEmptyWebProtegeDialogTextFieldValidator implements WebProtegeDialogInlineValidator<ValueBoxBase<String>> {

    private String msg;

    public NonEmptyWebProtegeDialogTextFieldValidator(String msg) {
        this.msg = msg;
    }

    public InlineValidationResult getValidation(ValueBoxBase<String> widget) {
        if(widget.getValue().isEmpty()) {
            return InlineValidationResult.getInvalid(msg);
        }
        else {
            return InlineValidationResult.getValid();
        }
    }

    @Override
    public boolean shouldCheckOnKeyUp() {
        return false;
    }

    @Override
    public boolean shouldCheckOnValueChange() {
        return false;
    }
}
