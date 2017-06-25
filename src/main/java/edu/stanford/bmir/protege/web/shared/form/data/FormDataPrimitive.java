package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.singletonList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/03/16
 */

public class FormDataPrimitive extends FormDataValue {

    @JsonUnwrapped
    @SuppressWarnings("GwtInconsistentSerializableClass")
    private Object value;

    @GwtSerializationConstructor
    private FormDataPrimitive() {
    }

    private FormDataPrimitive(@Nonnull OWLEntity entity) {
        value = checkNotNull(entity);
    }


    private FormDataPrimitive(@Nonnull OWLLiteral literal) {
        value = checkNotNull(literal);
    }


    private FormDataPrimitive(@Nonnull IRI iri) {
        value = checkNotNull(iri);
    }

    private FormDataPrimitive(@Nonnull Number number) {
        value = checkNotNull(number);
    }

    private FormDataPrimitive(@Nonnull Boolean b) {
        value = checkNotNull(b);
    }

    private FormDataPrimitive(@Nonnull String string) {
        value = checkNotNull(string);
    }

    @Nonnull
    public Object getValue() {
        return value;
    }

    @Override
    public Optional<IRI> asIRI() {
        if(value instanceof IRI) {
            return Optional.of((IRI) value);
        }
        else {
            return Optional.empty();
        }
    }
//    private FormDataPrimitive(OWLEntityData entityData) {
//        this.value = entityData;
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
        return new FormDataPrimitive(plainString);
    }

    public static FormDataPrimitive get(String plainString, String lang) {
        return new FormDataPrimitive(DataFactory.getOWLLiteral(plainString, lang));
    }

    public static FormDataPrimitive get(OWLLiteral literal) {
        return new FormDataPrimitive(literal);
    }

    public static FormDataPrimitive get(Number number) {
        return new FormDataPrimitive(number);
    }

    public static FormDataPrimitive get(boolean b) {
        return new FormDataPrimitive(b);
    }

    @Override
    public List<FormDataValue> asList() {
        return singletonList(this);
    }

    @Override
    public Optional<OWLLiteral> asLiteral() {
        if(value instanceof OWLLiteral) {
            return Optional.of((OWLLiteral) value);
        }
        else {
            return Optional.empty();
        }
    }

    public Optional<OWLEntity> asEntity() {
        if(value instanceof OWLEntity) {
            return Optional.of((OWLEntity) value);
        }
        else {
            return Optional.empty();
        }
    }

//    @Override
//    public Optional<OWLClassData> asOWLClassData() {
//        if(value instanceof OWLClassData) {
//            return Optional.of((OWLClassData) value);
//        }
//        else {
//            return Optional.absent();
//        }
//    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
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
        return this.value.equals(other.value);
    }


    @Override
    public String toString() {
        return toStringHelper("FormDataPrimitive")
                .addValue(asString())
                .toString();
    }


    public String asString() {
        if(value instanceof IRI) {
            return "IRI(" + ((IRI) value).toQuotedString() + ")";
        }
        else if(value instanceof OWLLiteral) {
            OWLLiteral literal = (OWLLiteral) value;
            return "Literal(" +
                    literal.toString() + ")";
        }
        else if(value instanceof OWLEntity) {
            return ((OWLEntity) value).getEntityType().getName() + "(" + ((OWLEntity) value).getIRI().toQuotedString() + ")";
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
