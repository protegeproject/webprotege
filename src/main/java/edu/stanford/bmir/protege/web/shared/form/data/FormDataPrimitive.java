package edu.stanford.bmir.protege.web.shared.form.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.server.form.FormDataPrimitiveSerializer;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static java.util.Collections.singletonList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 31/03/16
 */
@JsonSerialize(using = FormDataPrimitiveSerializer.class)
public abstract class FormDataPrimitive extends FormDataValue {

    @GwtSerializationConstructor
    private FormDataPrimitive() {
    }

    @Nonnull
    public abstract Object getValue();

    @JsonIgnore
    public abstract FormDataPrimitive getSimplified();

    public static FormDataPrimitive get(OWLEntity entity) {
        return new OWLEntityPrimitive(entity);
    }

    public abstract boolean isString();

    public abstract String getValueAsString();

    public abstract boolean isNumber();

    public static FormDataPrimitive get(IRI iri) {
        return new IRIPrimitive(iri);
    }

    public static FormDataPrimitive get(String plainString) {
        return new StringPrimitive(plainString);
    }

    public static FormDataPrimitive get(String plainString, String lang) {
        return new LiteralPrimitive(DataFactory.getOWLLiteral(plainString, lang));
    }

    public static FormDataPrimitive get(OWLLiteral literal) {
        return new LiteralPrimitive(literal);
    }

    public static FormDataPrimitive get(Number number) {
        return new NumberPrimitive(number);
    }

    public static FormDataPrimitive get(boolean b) {
        return new BooleanPrimitive(b);
    }

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

    public abstract double getValueAsDouble();

    public abstract boolean isBoolean();

    public abstract boolean getValueAsBoolean();

    public static class OWLEntityPrimitive extends FormDataPrimitive {

        @Nonnull
        private OWLEntity entity;

        public OWLEntityPrimitive(@Nonnull OWLEntity entity) {
            this.entity = entity;
        }

        private OWLEntityPrimitive() {
        }

        @Override
        public Optional<IRI> asIRI() {
            return Optional.empty();
        }

        @Nonnull
        @Override
        public Object getValue() {
            return entity;
        }

        @Override
        public FormDataPrimitive getSimplified() {
            return this;
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
    }

    public static class IRIPrimitive extends FormDataPrimitive {

        private IRI iri;

        public IRIPrimitive(IRI iri) {
            this.iri = iri;
        }

        private IRIPrimitive() {
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
        public FormDataPrimitive getSimplified() {
            return this;
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
    }

    public static class NumberPrimitive extends FormDataPrimitive {

        private double number;

        public NumberPrimitive(Number number) {
            this.number = number.doubleValue();
        }

        private NumberPrimitive() {
        }

        @Override
        public Optional<IRI> asIRI() {
            return Optional.empty();
        }

        @Override
        public Optional<OWLLiteral> asLiteral() {
            return Optional.of(DataFactory.get().getOWLLiteral(number));
        }

        @Nonnull
        @Override
        public Object getValue() {
            return number;
        }

        @Override
        public FormDataPrimitive getSimplified() {
            return this;
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
        public Optional<IRI> asIRI() {
            return Optional.empty();
        }

        @Override
        public Optional<OWLLiteral> asLiteral() {
            return Optional.of(DataFactory.get().getOWLLiteral(string));
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
        public FormDataPrimitive getSimplified() {
            return this;
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
    }

    public static class BooleanPrimitive extends FormDataPrimitive {

        private Boolean bool;

        public BooleanPrimitive(Boolean bool) {
            this.bool = bool;
        }

        private BooleanPrimitive() {
        }

        @Override
        public Optional<IRI> asIRI() {
            return Optional.empty();
        }

        @Override
        public Optional<OWLLiteral> asLiteral() {
            return Optional.of(DataFactory.getOWLLiteral(bool));
        }

        @Nonnull
        @Override
        public Object getValue() {
            return bool;
        }

        @Override
        public FormDataPrimitive getSimplified() {
            return this;
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
    }

    public static class LiteralPrimitive extends FormDataPrimitive {

        private OWLLiteral literal;

        public LiteralPrimitive(OWLLiteral literal) {
            this.literal = literal;
        }

        public LiteralPrimitive() {
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
        public FormDataPrimitive getSimplified() {
            if(literal.isRDFPlainLiteral() && !literal.hasLang()) {
                return FormDataPrimitive.get(literal.getLiteral());
            }
            if(literal.getDatatype().isString()) {
                return FormDataPrimitive.get(literal.getLiteral());
            }
            if(literal.isBoolean()) {
                return FormDataPrimitive.get(literal.parseBoolean());
            }
            if(literal.isDouble()) {
                try {
                    return FormDataPrimitive.get(literal.parseDouble());
                } catch (NumberFormatException e) {
                    return this;
                }
            }
            if(literal.isInteger()) {
                try {
                    return FormDataPrimitive.get(literal.parseInteger());
                } catch (NumberFormatException e) {
                    return this;
                }
            }
            if(literal.isFloat()) {
                try {
                    return FormDataPrimitive.get(literal.parseFloat());
                } catch (NumberFormatException e) {
                    return this;
                }
            }
            return this;
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
    }
}
