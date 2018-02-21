package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive.BooleanPrimitive.BOOLEAN_FALSE_PRIMITIVE;
import static edu.stanford.bmir.protege.web.shared.form.data.FormDataPrimitive.BooleanPrimitive.BOOLEAN_TRUE_PRIMITIVE;
import static java.util.Collections.singletonList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/03/16
 */
public abstract class FormDataPrimitive extends FormDataValue {

    @GwtSerializationConstructor
    private FormDataPrimitive() {
    }

    /**
     * Gets the actual primitive value.  This could be a String, Number, OWLLiteral, OWLEntity or IRI.
     * @return The primitive value.
     */
    @Nonnull
    public abstract Object getValue();

    public abstract OWLObject toOWLObject();

    /**
     * Gets a {@link FormDataPrimitive} that wraps an {@link OWLEntity}.
     * @param entity The entity to be wrapped.
     * @return The primitive that wraps the entity.
     */
    @Nonnull
    public static FormDataPrimitive get(@Nonnull OWLEntity entity) {
        return new OWLEntityPrimitive(entity);
    }

    /**
     * Gets a {@link FormDataPrimitive} that wraps an {@link IRI}.
     * @param iri The IRI to be wrapped.
     * @return The primitive that wraps the IRI.
     */
    public static FormDataPrimitive get(@Nonnull IRI iri) {
        return new IRIPrimitive(iri);
    }

    /**
     * Gets a {@link FormDataPrimitive} that wraps a {@link String}.
     * @param string The string to be wrapped.
     * @return The primitive that wraps the string.
     */
    @Nonnull
    public static FormDataPrimitive get(@Nonnull String string) {
        return new StringPrimitive(string);
    }

    /**
     * Gets a {@link FormDataPrimitive} that wraps an {@link String} with an xml:lang.
     * @param string The string to be wrapped.
     * @param lang The lang.
     * @return The primitive that wraps the string with the lang.
     */
    @Nonnull
    public static FormDataPrimitive get(@Nonnull String string,
                                        @Nonnull String lang) {
        return new LiteralPrimitive(DataFactory.getOWLLiteral(string, lang));
    }

    /**
     * Gets a {@link FormDataPrimitive} that wraps an {@link OWLLiteral}.
     * @param literal The {@link OWLLiteral} to be wrapped.
     * @return The primitive that wraps the {@link OWLLiteral}.
     */
    @Nonnull
    public static FormDataPrimitive get(@Nonnull OWLLiteral literal) {
        return new LiteralPrimitive(literal);
    }

    /**
     * Gets a {@link FormDataPrimitive} that wraps an {@link Number}.
     * @param number The {@link Number} to be wrapped.
     * @return The primitive that wraps the {@link Number}.
     */
    @Nonnull
    public static FormDataPrimitive get(@Nonnull Number number) {
        return new NumberPrimitive(number);
    }

    /**
     * Gets a {@link FormDataPrimitive} that wraps an {@link boolean}.
     * @param b The boolean to be wrapped.
     * @return The primitive that wraps the boolean.
     */
    @Nonnull
    public static FormDataPrimitive get(boolean b) {
        if(b) {
            return BOOLEAN_TRUE_PRIMITIVE;
        }
        else {
            return BOOLEAN_FALSE_PRIMITIVE;
        }
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * Determines if this primitive wraps a String value.  This is true if the primitive was
     * created with the factory method that takes a String value as a parameter.
     * @return true if this primitive wraps a String value, otherwise false.
     */
    public abstract boolean isString();

    /**
     * Gets this primitive as a String.
     * @return The String value of this primitive.
     * @throws RuntimeException if this primitive does not wrap a {@link String}.
     */
    public abstract String getValueAsString();


    /**
     * Determines if this primitive wraps a Number value.  This is true if the primitive was
     * created with the factory method that takes a Number value as a parameter.
     * @return true if this primitive wraps a boolean value, otherwise false.
     */
    public abstract boolean isNumber();

    /**
     * Gets this primitive as a double.
     * @return The double value of this primitive.
     * @throws RuntimeException if this primitive does not wrap a {@link Number}.
     */
    public abstract double getValueAsDouble();

    /**
     * Determines if this primitive wraps a boolean value.  This is true if the primitive was
     * created with the factory method that takes a boolean value as a parameter.
     * @return true if this primitive wraps a boolean value, otherwise false.
     */
    public abstract boolean isBoolean();

    /**
     * Gets this primitive as boolean.
     * @return The primitive as a boolean value.
     * @throws RuntimeException if this primitive does not wrap a boolean value.
     */
    public abstract boolean getValueAsBoolean();




    @Override
    public List<FormDataValue> asList() {
        return singletonList(this);
    }


    @Override
    public String toString() {
        return toStringHelper("FormDataPrimitive")
                .addValue(getValue())
                .toString();
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
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
        return this.getValue().equals(other.getValue());
    }


    public static class OWLEntityPrimitive extends FormDataPrimitive {

        private OWLEntity entity;

        public OWLEntityPrimitive(@Nonnull OWLEntity entity) {
            this.entity = entity;
        }

        @GwtSerializationConstructor
        private OWLEntityPrimitive() {
        }

        @Override
        public Optional<IRI> asIRI() {
            return Optional.empty();
        }

        @Override
        public Optional<OWLEntity> asOWLEntity() {
            return Optional.of(entity);
        }

        @Nonnull
        @Override
        public Object getValue() {
            return entity;
        }

        @Override
        public boolean isString() {
            return false;
        }

        @Override
        public String getValueAsString() {
            throw new RuntimeException("Not a String");
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public double getValueAsDouble() {
            throw new RuntimeException("Not a number");
        }

        @Override
        public boolean isBoolean() {
            return false;
        }

        @Override
        public boolean getValueAsBoolean() {
            throw new RuntimeException("Not a boolean");
        }


        @Override
        public String toString() {
            return toStringHelper("OWLEntityPrimitive")
                    .add("type", entity.getEntityType().getName())
                    .add("iri", entity.getIRI().toString())
                    .toString();
        }

        @Override
        public OWLObject toOWLObject() {
            return entity;
        }
    }

    public static class IRIPrimitive extends FormDataPrimitive {

        private IRI iri;

        public IRIPrimitive(IRI iri) {
            this.iri = iri;
        }

        @GwtSerializationConstructor
        private IRIPrimitive() {
        }

        @Override
        public Optional<OWLEntity> asOWLEntity() {
            return Optional.empty();
        }

        @Override
        public Optional<IRI> asIRI() {
            return Optional.of(iri);
        }

        @Nonnull
        @Override
        public Object getValue() {
            return iri;
        }

        @Override
        public boolean isString() {
            return false;
        }

        @Override
        public String getValueAsString() {
            throw new RuntimeException("Not a String");
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public double getValueAsDouble() {
            throw new RuntimeException("Not a number");
        }

        @Override
        public boolean isBoolean() {
            return false;
        }

        @Override
        public boolean getValueAsBoolean() {
            throw new RuntimeException("Not a boolean");
        }

        @Override
        public OWLObject toOWLObject() {
            return iri;
        }
    }

    public static class NumberPrimitive extends FormDataPrimitive {

        private double number;

        public NumberPrimitive(Number number) {
            this.number = number.doubleValue();
        }

        @GwtSerializationConstructor
        private NumberPrimitive() {
        }

        @Override
        public Optional<OWLEntity> asOWLEntity() {
            return Optional.empty();
        }

        @Override
        public Optional<IRI> asIRI() {
            return Optional.empty();
        }

        @Override
        public Optional<OWLLiteral> asLiteral() {
            return Optional.of(toOWLLiteral());
        }

        private OWLLiteral toOWLLiteral() {
            return DataFactory.get().getOWLLiteral(Double.toString(number), OWL2Datatype.XSD_DECIMAL);
        }

        @Nonnull
        @Override
        public Object getValue() {
            return number;
        }

        @Override
        public String toString() {
            return toStringHelper("NumberPrimitive")
                    .addValue(number)
                    .toString();
        }
        @Override
        public boolean isString() {
            return false;
        }

        @Override
        public String getValueAsString() {
            throw new RuntimeException("Not a String");
        }

        @Override
        public boolean isNumber() {
            return true;
        }

        @Override
        public double getValueAsDouble() {
            return number;
        }

        @Override
        public boolean isBoolean() {
            return false;
        }

        @Override
        public boolean getValueAsBoolean() {
            throw new RuntimeException("Not a boolean");
        }

        @Override
        public OWLObject toOWLObject() {
            return toOWLLiteral();
        }
    }

    public static class StringPrimitive extends FormDataPrimitive {

        private String string;

        public StringPrimitive(String string) {
            this.string = string;
        }

        @GwtSerializationConstructor
        private StringPrimitive() {
        }

        @Override
        public Optional<OWLEntity> asOWLEntity() {
            return Optional.empty();
        }

        @Override
        public Optional<IRI> asIRI() {
            return Optional.empty();
        }

        @Override
        public Optional<OWLLiteral> asLiteral() {
            return Optional.of(toOWLLiteral());
        }

        private OWLLiteral toOWLLiteral() {
            return DataFactory.get().getOWLLiteral(string, OWL2Datatype.XSD_STRING);
        }

        @Nonnull
        @Override
        public Object getValue() {
            return string;
        }


        @Override
        public String toString() {
            return toStringHelper("StringPrimitive")
                    .addValue(string)
                    .toString();
        }

        @Override
        public boolean isString() {
            return true;
        }

        @Override
        public String getValueAsString() {
            return string;
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public double getValueAsDouble() {
            throw new RuntimeException("Not a number");
        }

        @Override
        public boolean isBoolean() {
            return false;
        }

        @Override
        public boolean getValueAsBoolean() {
            throw new RuntimeException("Not a boolean");
        }

        @Override
        public OWLObject toOWLObject() {
            return toOWLLiteral();
        }
    }

    public static class BooleanPrimitive extends FormDataPrimitive {

        protected static final BooleanPrimitive BOOLEAN_TRUE_PRIMITIVE = new BooleanPrimitive(true);

        protected static final BooleanPrimitive BOOLEAN_FALSE_PRIMITIVE = new BooleanPrimitive(false);

        private Boolean bool;

        public BooleanPrimitive(Boolean bool) {
            this.bool = bool;
        }

        @GwtSerializationConstructor
        private BooleanPrimitive() {
        }

        @Override
        public Optional<OWLEntity> asOWLEntity() {
            return Optional.empty();
        }

        @Override
        public Optional<IRI> asIRI() {
            return Optional.empty();
        }

        @Override
        public Optional<OWLLiteral> asLiteral() {
            return Optional.of(toOWLLiteral());
        }

        private OWLLiteral toOWLLiteral() {
            return DataFactory.getOWLLiteral(bool);
        }

        @Nonnull
        @Override
        public Object getValue() {
            return bool;
        }

        @Override
        public boolean isString() {
            return false;
        }

        @Override
        public String getValueAsString() {
            throw new RuntimeException("Not a String");
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public double getValueAsDouble() {
            throw new RuntimeException("Not a number");
        }

        @Override
        public boolean isBoolean() {
            return true;
        }

        @Override
        public boolean getValueAsBoolean() {
            return bool;
        }

        @Override
        public OWLObject toOWLObject() {
            return toOWLLiteral();
        }
    }

    public static class LiteralPrimitive extends FormDataPrimitive {

        private OWLLiteral literal;

        public LiteralPrimitive(OWLLiteral literal) {
            this.literal = literal;
        }

        @GwtSerializationConstructor
        public LiteralPrimitive() {
        }

        @Override
        public Optional<OWLEntity> asOWLEntity() {
            return Optional.empty();
        }

        @Override
        public Optional<IRI> asIRI() {
            return Optional.empty();
        }

        @Nonnull
        @Override
        public Object getValue() {
            return literal;
        }

        @Override
        public Optional<OWLLiteral> asLiteral() {
            return Optional.of(literal);
        }

        @Override
        public String toString() {
            return toStringHelper("LiteralPrimitive")
                    .addValue(literal.getLiteral())
                    .addValue(literal.getDatatype())
                    .addValue(literal.getLang())
                    .toString();
        }

        @Override
        public boolean isString() {
            return false;
        }

        @Override
        public String getValueAsString() {
            throw new RuntimeException("Not a String");
        }

        @Override
        public boolean isNumber() {
            return false;
        }

        @Override
        public double getValueAsDouble() {
            throw new RuntimeException("Not a number");
        }

        @Override
        public boolean isBoolean() {
            return false;
        }

        @Override
        public boolean getValueAsBoolean() {
            throw new RuntimeException("Not a boolean");
        }

        @Override
        public OWLObject toOWLObject() {
            return literal;
        }
    }
}
