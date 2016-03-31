package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class ChoiceDescriptor implements IsSerializable {

    private String label;

    @SuppressWarnings("GwtInconsistentSerializableClass")
    private OWLPrimitiveData data;


    private ChoiceDescriptor() {
    }

    public ChoiceDescriptor(String label, OWLPrimitiveData data) {
        this.label = label;
        this.data = data;
    }

    public String getLabel() {
        return label;
    }

    public OWLPrimitiveData getData() {
        return data;
    }
}
