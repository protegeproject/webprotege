package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.form.data.FormDataValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class ChoiceDescriptor implements IsSerializable {

    private String label;

    @JsonUnwrapped
    private FormDataValue value;

    private ChoiceDescriptor() {
    }

    private ChoiceDescriptor(String label, FormDataValue value) {
        this.label = label;
        this.value = value;
    }

    public static ChoiceDescriptor choice(String label, FormDataValue value) {
        return new ChoiceDescriptor(label, value);
    }

    public String getLabel() {
        return label;
    }

    public FormDataValue getValue() {
        return value;
    }
}
