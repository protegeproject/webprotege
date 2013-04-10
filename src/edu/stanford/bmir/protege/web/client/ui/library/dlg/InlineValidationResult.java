package edu.stanford.bmir.protege.web.client.ui.library.dlg;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 12/10/2012
 */
public class InlineValidationResult {

    public static final InlineValidationResult INLINE_VALIDATION_RESULT = new InlineValidationResult(ValidationState.VALID, "");

    private ValidationState state;
    
    private String message;
    
    public static InlineValidationResult getValid() {
        return INLINE_VALIDATION_RESULT;
    }
    
    public static InlineValidationResult getInvalid(String message) {
        return new InlineValidationResult(ValidationState.INVALID, message);
    }

    public InlineValidationResult(ValidationState state, String message) {
        this.state = state;
        if(message == null) {
            this.message = "";
        }
        else {
            this.message = message;
        }
    }

    /**
     * Gets the validation state.
     * @return The validation state. Not <code>null</code>.
     */
    public ValidationState getState() {
        return state;
    }

    /**
     * Gets the validation message.
     * @return The validation message. Not <code>null</code>.
     */
    public String getMessage() {
        return message;
    }

    public boolean isInvalid() {
        return state == ValidationState.INVALID;
    }

    public boolean isValid() {
        return state == ValidationState.VALID;
    }
}
