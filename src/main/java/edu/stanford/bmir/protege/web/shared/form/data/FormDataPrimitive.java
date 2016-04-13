package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/03/16
 */
public class FormDataPrimitive extends FormDataValue {


    @SuppressWarnings("GwtInconsistentSerializableClass")
    private OWLObject primitive;

    private FormDataPrimitive() {
    }

    private FormDataPrimitive(OWLEntity entity) {
        primitive = checkNotNull(entity);
    }


    private FormDataPrimitive(OWLLiteral literal) {
        primitive = checkNotNull(literal);
    }


    private FormDataPrimitive(IRI iri) {
        primitive = checkNotNull(iri);
    }

    @Override
    public Optional<IRI> asIRI() {
        if(primitive instanceof IRI) {
            return Optional.of((IRI)primitive);
        }
        else {
            return Optional.absent();
        }
    }
//    private FormDataPrimitive(OWLEntityData entityData) {
//        this.primitive = entityData;
//    }

    public static FormDataPrimitive get(OWLEntity entity) {
        return new FormDataPrimitive(entity);
    }

//    public static FormDataPrimitive get(OWLEntityData entityData) {
//        return new FormDataPrimitive(entityData);
//    }

    public static FormDataPrimitive get(IRI iri) {
        return new FormDataPrimitive(iri);
    }

    public static FormDataPrimitive get(String plainString) {
        return new FormDataPrimitive(DataFactory.getOWLLiteral(plainString));
    }

    public static FormDataPrimitive get(String plainString, String lang) {
        return new FormDataPrimitive(DataFactory.getOWLLiteral(plainString, lang));
    }

    public static FormDataPrimitive get(OWLLiteral literal) {
        return new FormDataPrimitive(literal);
    }

    @Override
    public List<FormDataValue> asList() {
        return Arrays.<FormDataValue>asList(this);
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

//    @Override
//    public Optional<OWLClassData> asOWLClassData() {
//        if(primitive instanceof OWLClassData) {
//            return Optional.of((OWLClassData) primitive);
//        }
//        else {
//            return Optional.absent();
//        }
//    }

    @Override
    public int hashCode() {
        return Objects.hashCode(primitive);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FormDataPrimitive)) {
            return false;
        }
        FormDataPrimitive other = (FormDataPrimitive) obj;
        return this.primitive.equals(other.primitive);
    }


    @Override
    public String toString() {
        return toStringHelper("FormDataPrimitive")
                .addValue(asString())
                .toString();
    }


    public String asString() {
        if(primitive instanceof IRI) {
            return "IRI(" + ((IRI) primitive).toQuotedString() + ")";
        }
        else if(primitive instanceof OWLLiteral) {
            OWLLiteral literal = (OWLLiteral) primitive;
            return "Literal(" +
                    literal.toString() + ")";
        }
        else if(primitive instanceof OWLEntity) {
            return ((OWLEntity) primitive).getEntityType().getName() + "(" + ((OWLEntity) primitive).getIRI().toQuotedString() + ")";
        }
        else {
            throw new RuntimeException("Unknown Type");
        }
    }

    @Override
    public boolean isObject() {
        return false;
    }
}
