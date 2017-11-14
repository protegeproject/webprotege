package edu.stanford.bmir.protege.web.client.entity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.library.dlg.WebProtegeDialogForm;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBox;
import edu.stanford.bmir.protege.web.client.library.text.ExpandingTextBoxMode;
import org.semanticweb.owlapi.model.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/06/2012
 */
public class CreateEntityForm extends WebProtegeDialogForm {

    private static final Messages MESSAGES = GWT.create(Messages.class);

    private ExpandingTextBox entityBrowserTextField;

    private EntityType<?> entityType;

    private static ExpandingTextBoxMode mode = ExpandingTextBoxMode.MULTI_LINE;

    public CreateEntityForm(EntityType<?> type) {
        this.entityType = type;
        entityBrowserTextField = new ExpandingTextBox();
        entityBrowserTextField.setWidth("450px");
        entityBrowserTextField.setMode(mode);
        entityBrowserTextField.setAnchorVisible(false);
        entityBrowserTextField.setPlaceholder(MESSAGES.createEntityInstructions());
        addWidget("", entityBrowserTextField);
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

    public Optional<HasRequestFocus> getInitialFocusable() {
        return Optional.of(() -> entityBrowserTextField.setFocus(true));
    }
}
