package edu.stanford.bmir.protege.web.client.library.dlg;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2012
 */
public interface WebProtegeDialogValidator {
    
    public static final String VALID_STATE_MESSAGE = "";

    /**
     * Gets the validation state that describes whether the contents of a {@link WebProtegeDialog} are valid or not.
     * @return A validation state.
     */
    ValidationState getValidationState();

    /**
     * Gets a validation message.
     * @return If {@link WebProtegeDialogValidator#getValidationMessage()} returns {@link ValidationState#VALID} then the empty string is
     * returned.  If {@link WebProtegeDialogValidator#getValidationMessage()} returns {@link ValidationState#INVALID} then a plain string
     * is returned that describes why the {@link ValidationState#INVALID} state exists.
     */
    String getValidationMessage();
}
