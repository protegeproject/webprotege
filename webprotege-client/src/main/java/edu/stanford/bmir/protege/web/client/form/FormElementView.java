package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;
import edu.stanford.bmir.protege.web.shared.form.field.Optionality;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public interface FormElementView extends IsWidget {

    void addStylePropertyValue(String cssProperty, String cssValue);

    void setId(FormElementId elementId);

    Optional<FormElementId> getId();

    void setFormLabel(String formLabel);

    void setEditor(FormElementEditor editor);

    FormElementEditor getEditor();

    void setRequiredValueNotPresentVisible(boolean visible);

    void setRequired(Optionality required);

    Optionality getRequired();
}
