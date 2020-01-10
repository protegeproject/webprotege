package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-30
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class LiteralFormControlValue implements PrimitiveFormControlData {

    public static LiteralFormControlValue get(@Nonnull OWLLiteral literal) {
        return new AutoValue_LiteralFormControlValue(literal);
    }

    @JsonValue
    @Nonnull
    public abstract OWLLiteral getLiteral();

    @JsonIgnore
    public double getValueAsDouble() {
        String lexicalForm = getLiteral().getLiteral();
        return Double.parseDouble(lexicalForm);
    }

    @JsonIgnore
    public boolean isNumber() {
        OWLDatatype datatype = getLiteral().getDatatype();
        if(!datatype.isBuiltIn()) {
            return false;
        }
        return datatype.getBuiltInDatatype().isNumeric();
    }

    @Nonnull
    @Override
    public Optional<OWLEntity> asEntity() {
        return Optional.empty();
    }

    @Nonnull
    @Override
    public Optional<IRI> asIri() {
        return Optional.empty();
    }

    @Nonnull
    @Override
    public Optional<OWLLiteral> asLiteral() {
        return Optional.of(getLiteral());
    }
}
