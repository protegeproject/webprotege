package edu.stanford.bmir.protege.web.server.integration.service;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.frame.PlainNamedIndividualFrame;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyClassValue;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyDatatypeValue;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyIndividualValue;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyLiteralValue;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyValueVisitor;
import edu.stanford.bmir.protege.web.shared.frame.State;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Maps between REST property maps and OWL named-individual frame property values.
 */
public final class IndividualFramePropertyMapper {

    private IndividualFramePropertyMapper() {
    }

    @Nonnull
    public static Map<String, String> toPropertyMap(@Nonnull PlainNamedIndividualFrame frame) {
        Objects.requireNonNull(frame, "frame");
        return toPropertyMap(frame.getPropertyValues());
    }

    /**
     * Flattens asserted property values to {@code propertyIri → string}. Derived values are skipped.
     * Duplicate keys keep the last value (same limitation as the individual REST map).
     */
    @Nonnull
    public static Map<String, String> toPropertyMap(@Nonnull Iterable<? extends PlainPropertyValue> propertyValues) {
        Objects.requireNonNull(propertyValues, "propertyValues");
        Map<String, String> properties = new LinkedHashMap<>();
        for (PlainPropertyValue propertyValue : propertyValues) {
            if (propertyValue.getState() == State.DERIVED) {
                continue;
            }
            properties.put(propertyIri(propertyValue), propertyValue.accept(VALUE_TO_STRING));
        }
        return properties;
    }

    /**
     * Builds a replacement set of asserted property values from a string map.
     * Existing property kinds are preserved when the property already appears on {@code from}.
     */
    @Nonnull
    public static ImmutableSet<PlainPropertyValue> toPropertyValues(
            @Nonnull PlainNamedIndividualFrame from,
            @Nonnull Map<String, String> properties) {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(properties, "properties");

        Map<String, PlainPropertyValue> existingByProperty = new LinkedHashMap<>();
        for (PlainPropertyValue propertyValue : from.getPropertyValues()) {
            if (propertyValue.getState() == State.DERIVED) {
                continue;
            }
            existingByProperty.put(propertyIri(propertyValue), propertyValue);
        }

        ImmutableSet.Builder<PlainPropertyValue> builder = ImmutableSet.builder();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            String propertyIri = entry.getKey();
            String value = entry.getValue() == null ? "" : entry.getValue();
            PlainPropertyValue existing = existingByProperty.get(propertyIri);
            builder.add(createPropertyValue(propertyIri, value, existing));
        }
        return builder.build();
    }

    /**
     * Merges {@code patch} into existing asserted values (patch keys overwrite).
     */
    @Nonnull
    public static ImmutableSet<PlainPropertyValue> mergePropertyValues(
            @Nonnull PlainNamedIndividualFrame from,
            @Nonnull Map<String, String> patch) {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(patch, "patch");

        Map<String, String> merged = toPropertyMap(from);
        merged.putAll(patch);
        return toPropertyValues(from, merged);
    }

    @Nonnull
    private static PlainPropertyValue createPropertyValue(@Nonnull String propertyIri,
                                                          @Nonnull String value,
                                                          PlainPropertyValue existing) {
        if (existing instanceof PlainPropertyIndividualValue) {
            return PlainPropertyIndividualValue.get(
                    DataFactory.getOWLObjectProperty(propertyIri),
                    DataFactory.getOWLNamedIndividual(value),
                    State.ASSERTED);
        }
        if (existing instanceof PlainPropertyClassValue) {
            return PlainPropertyClassValue.get(
                    DataFactory.getOWLObjectProperty(propertyIri),
                    DataFactory.getOWLClass(value),
                    State.ASSERTED);
        }
        if (existing instanceof PlainPropertyDatatypeValue) {
            return PlainPropertyDatatypeValue.get(
                    DataFactory.getOWLDataProperty(propertyIri),
                    DataFactory.getOWLDatatype(value),
                    State.ASSERTED);
        }
        if (existing instanceof PlainPropertyAnnotationValue) {
            return PlainPropertyAnnotationValue.get(
                    DataFactory.getOWLAnnotationProperty(propertyIri),
                    DataFactory.getOWLLiteral(value),
                    State.ASSERTED);
        }
        return PlainPropertyLiteralValue.get(
                DataFactory.getOWLDataProperty(propertyIri),
                DataFactory.getOWLLiteral(value),
                State.ASSERTED);
    }

    @Nonnull
    private static String propertyIri(@Nonnull PlainPropertyValue propertyValue) {
        return propertyValue.accept(PROPERTY_IRI);
    }

    private static final PlainPropertyValueVisitor<String> PROPERTY_IRI = new PlainPropertyValueVisitor<String>() {
        @Override
        public String visit(PlainPropertyClassValue propertyValue) {
            return propertyValue.getProperty().getIRI().toString();
        }

        @Override
        public String visit(PlainPropertyAnnotationValue propertyValue) {
            return propertyValue.getProperty().getIRI().toString();
        }

        @Override
        public String visit(PlainPropertyDatatypeValue propertyValue) {
            return propertyValue.getProperty().getIRI().toString();
        }

        @Override
        public String visit(PlainPropertyIndividualValue propertyValue) {
            return propertyValue.getProperty().getIRI().toString();
        }

        @Override
        public String visit(PlainPropertyLiteralValue propertyValue) {
            return propertyValue.getProperty().getIRI().toString();
        }
    };

    private static final PlainPropertyValueVisitor<String> VALUE_TO_STRING = new PlainPropertyValueVisitor<String>() {
        @Override
        public String visit(PlainPropertyClassValue propertyValue) {
            return propertyValue.getValue().getIRI().toString();
        }

        @Override
        public String visit(PlainPropertyAnnotationValue propertyValue) {
            OWLAnnotationValue value = propertyValue.getValue();
            if (value instanceof OWLLiteral) {
                return ((OWLLiteral) value).getLiteral();
            }
            if (value instanceof IRI) {
                return value.toString();
            }
            return value.toString();
        }

        @Override
        public String visit(PlainPropertyDatatypeValue propertyValue) {
            return propertyValue.getValue().getIRI().toString();
        }

        @Override
        public String visit(PlainPropertyIndividualValue propertyValue) {
            return propertyValue.getValue().getIRI().toString();
        }

        @Override
        public String visit(PlainPropertyLiteralValue propertyValue) {
            return propertyValue.getValue().getLiteral();
        }
    };
}
