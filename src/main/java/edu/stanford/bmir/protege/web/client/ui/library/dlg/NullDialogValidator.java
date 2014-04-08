package edu.stanford.bmir.protege.web.client.ui.library.dlg;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2012
 * <p>
 *     A dialog validator that always returns the valid state.
 * </p>
 */
public class NullDialogValidator implements WebProtegeDialogValidator {


    public ValidationState getValidationState() {
        return ValidationState.VALID;
    }

    public String getValidationMessage() {
        return WebProtegeDialogValidator.VALID_STATE_MESSAGE;
    }
}
