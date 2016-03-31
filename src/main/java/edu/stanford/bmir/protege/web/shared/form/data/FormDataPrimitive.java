package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLPrimitive;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/03/16
 */
public class FormDataPrimitive implements FormDataValue {

    private OWLPrimitive primitive;

    private FormDataPrimitive() {
    }

    public FormDataPrimitive(OWLPrimitive primitive) {
        this.primitive = primitive;
    }

    public static FormDataPrimitive get(String plainString) {
        return new FormDataPrimitive(DataFactory.getOWLLiteral(plainString));
    }

    public static FormDataPrimitive get(String plainString, String lang) {
        return new FormDataPrimitive(DataFactory.getOWLLiteral(plainString, lang));
    }

    public OWLPrimitive getPrimitive() {
        return primitive;
    }

    @Override
    public Optional<OWLPrimitive> asPrimitive() {
        return Optional.of(primitive);
    }

    @Override
    public Optional<OWLLiteral> asLiteral() {
        if(primitive instanceof OWLLiteral) {
            return Optional.of((OWLLiteral) primitive);
        }
        else {
            return Optional.absent();
        }
    }

    @Override
    public List<OWLLiteral> asLiteralList() {
        if(primitive instanceof OWLLiteral) {
            return Arrays.<OWLLiteral>asList((OWLLiteral) primitive);
        }
        else {
            return Collections.emptyList();
        }
    }
}
