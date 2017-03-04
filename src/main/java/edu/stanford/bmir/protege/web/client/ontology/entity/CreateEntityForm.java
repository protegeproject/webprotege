package edu.stanford.bmir.protege.web.client.ontology.entity;

import com.google.common.base.Optional;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;
import com.google.gwt.user.client.ui.Focusable;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogForm;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBox;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBoxMode;
import org.semanticweb.owlapi.model.EntityType;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class CreateEntityForm extends WebProtegeDialogForm {

    private ExpandingTextBox entityBrowserTextField;

    private EntityType<?> entityType;

    private static ExpandingTextBoxMode mode = ExpandingTextBoxMode.MULTI_LINE;

    public CreateEntityForm(EntityType<?> type) {
        this.entityType = type;
        entityBrowserTextField = new ExpandingTextBox();
        entityBrowserTextField.setWidth("450px");
        entityBrowserTextField.setMode(mode);
        entityBrowserTextField.setAnchorVisible(false);
        entityBrowserTextField.setPlaceholder("Enter one " + getTypeName() + " name per line (press CTRL+ENTER to accept and close dialog)");
        addWidget("Name", entityBrowserTextField);

//        entityBrowserTextField = addTextBox("Name", "Enter " + getTypeName() + " name", "", new WebProtegeDialogInlineValidator<ValueBoxBase<String>>() {
//            @Override
//            public InlineValidationResult getValidation(ValueBoxBase<String> widget) {
//                return widget.getText().trim().isEmpty() ? InlineValidationResult.getInvalid("Name must not be empty") : InlineValidationResult.getValid();
//            }
//        });
//        entityBrowserTextField.setVisibleLength(60);

    }

    public List<String> getEntityBrowserText() {
        String enteredText = entityBrowserTextField.getText().trim();
        List<String> result = new ArrayList<String>();
        RegExp regExp = RegExp.compile("\n");
        SplitResult split = regExp.split(enteredText);
        for(int i = 0; i < split.length(); i++) {
            result.add(split.get(i));
        }
        return result;
    }

    public Optional<Focusable> getInitialFocusable() {
        return Optional.<Focusable>of(entityBrowserTextField);
    }

    private String getTypeName() {
        if(entityType.equals(EntityType.CLASS)) {
            return "class";
        }
        else if(entityType.equals(EntityType.OBJECT_PROPERTY)) {
            return "object property";
        }
        else if(entityType.equals(EntityType.DATA_PROPERTY)) {
            return "data property";
        }
        else if(entityType.equals(EntityType.ANNOTATION_PROPERTY)) {
            return "annotation property";
        }
        else if(entityType.equals(EntityType.NAMED_INDIVIDUAL)) {
            return "individual";
        }
        else {
            return "a datatype";
        }
    }
}
