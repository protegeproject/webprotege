package edu.stanford.bmir.protege.web.client.ui.ontology.entity;

import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.ValidationState;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogForm;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.WebProtegeDialogValidator;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class CreateEntityForm extends WebProtegeDialogForm {

    private TextBox entityBrowserTextField = new TextBox();
    
    public CreateEntityForm() {
        entityBrowserTextField.setVisibleLength(60);
        addWidget("Name", entityBrowserTextField);
        addDialogValidator(new EntityNameValidator());
    }
    
    public String getEntityBrowserText() {
        return entityBrowserTextField.getText().trim();
    }
    
    
    private class EntityNameValidator implements WebProtegeDialogValidator {

        public ValidationState getValidationState() {
            return getEntityBrowserText().isEmpty() ? ValidationState.INVALID : ValidationState.VALID;
        }

        public String getValidationMessage() {
            return "Please enter a name";
        }
    }

    public Focusable getInitialFocusable() {
        return entityBrowserTextField;
    }
}
