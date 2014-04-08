package edu.stanford.bmir.protege.web.client.ui.library.dlg;

import com.google.gwt.user.client.ui.ValueBoxBase;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/10/2012
 */
public class NullWebProtegeDialogTextFieldValidator implements WebProtegeDialogInlineValidator<ValueBoxBase<String>> {

    private static NullWebProtegeDialogTextFieldValidator instance = new NullWebProtegeDialogTextFieldValidator();
    

    public static NullWebProtegeDialogTextFieldValidator get() {
        return instance;
    }

    public InlineValidationResult getValidation(ValueBoxBase<String> widget) {
        return InlineValidationResult.getValid();
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
