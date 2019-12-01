package edu.stanford.bmir.protege.web.client.form;

import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;
import edu.stanford.bmir.protege.web.shared.form.field.ChoiceDescriptor;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/03/16
 */
public interface ChoiceFieldEditor extends FormElementEditor {

    void setChoices(List<ChoiceDescriptor> choices);

    void setDefaultChoices(List<FormDataValue> defaultChoices);
}
