package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class ChoiceDescriptor implements IsSerializable {

    private String label;

    private FormDataValue value;

    private ChoiceDescriptor() {
    }

    public ChoiceDescriptor(String label, FormDataValue value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public FormDataValue getValue() {
        return value;
    }
}
