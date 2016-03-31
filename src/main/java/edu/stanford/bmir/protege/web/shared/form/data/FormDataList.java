package edu.stanford.bmir.protege.web.shared.form.data;

import org.semanticweb.owlapi.model.OWLLiteral;

import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/03/16
 */
public class FormDataList implements FormDataValue {

    private List<FormDataValue> list = new ArrayList<>();

    private FormDataList() {
    }

    public FormDataList(List<FormDataValue> list) {
        this.list.addAll(list);
    }

    public List<FormDataValue> getList() {
        return new ArrayList<>(list);
    }

    @Override
    public List<OWLLiteral> asLiteralList() {
        List<OWLLiteral> result = new ArrayList<>();
        for(FormDataValue value : list) {
            result.addAll(value.asLiteral().asSet());
        }
        return result;
    }
}
