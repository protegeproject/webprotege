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
import edu.stanford.bmir.protege.web.shared.frame.State;
import org.junit.Test;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class IndividualFramePropertyMapper_TestCase {

    private static final String INDIVIDUAL_IRI = "http://example.org/Device1";
    private static final String TEMP_PROPERTY = "http://example.org/hasTemperature";
    private static final String RELATED_PROPERTY = "http://example.org/relatedTo";

    @Test
    public void shouldMapLiteralValuesToStringMap() {
        OWLNamedIndividual subject = DataFactory.getOWLNamedIndividual(INDIVIDUAL_IRI);
        PlainNamedIndividualFrame frame = PlainNamedIndividualFrame.get(
                subject,
                ImmutableSet.of(),
                ImmutableSet.of(),
                ImmutableSet.of(
                        PlainPropertyLiteralValue.get(
                                DataFactory.getOWLDataProperty(TEMP_PROPERTY),
                                DataFactory.getOWLLiteral("23.5"),
                                State.ASSERTED)
                )
        );

        Map<String, String> map = IndividualFramePropertyMapper.toPropertyMap(frame);
        assertThat(map, hasEntry(TEMP_PROPERTY, "23.5"));
    }

    @Test
    public void shouldSkipDerivedValues() {
        OWLNamedIndividual subject = DataFactory.getOWLNamedIndividual(INDIVIDUAL_IRI);
        PlainNamedIndividualFrame frame = PlainNamedIndividualFrame.get(
                subject,
                ImmutableSet.of(),
                ImmutableSet.of(),
                ImmutableSet.of(
                        PlainPropertyLiteralValue.get(
                                DataFactory.getOWLDataProperty(TEMP_PROPERTY),
                                DataFactory.getOWLLiteral("1"),
                                State.DERIVED)
                )
        );

        assertThat(IndividualFramePropertyMapper.toPropertyMap(frame).isEmpty(), is(true));
    }

    @Test
    public void shouldPreserveObjectPropertyKindOnUpdate() {
        OWLNamedIndividual subject = DataFactory.getOWLNamedIndividual(INDIVIDUAL_IRI);
        PlainNamedIndividualFrame from = PlainNamedIndividualFrame.get(
                subject,
                ImmutableSet.of(),
                ImmutableSet.of(),
                ImmutableSet.of(
                        PlainPropertyIndividualValue.get(
                                DataFactory.getOWLObjectProperty(RELATED_PROPERTY),
                                DataFactory.getOWLNamedIndividual("http://example.org/Other"),
                                State.ASSERTED)
                )
        );

        ImmutableSet<PlainPropertyValue> updated = IndividualFramePropertyMapper.toPropertyValues(
                from,
                Collections.singletonMap(RELATED_PROPERTY, "http://example.org/NewOther")
        );

        assertThat(updated, hasSize(1));
        PlainPropertyValue only = updated.iterator().next();
        assertThat(only, instanceOf(PlainPropertyIndividualValue.class));
        assertThat(((PlainPropertyIndividualValue) only).getValue().getIRI().toString(),
                   is("http://example.org/NewOther"));
    }

    @Test
    public void shouldMergePatchValues() {
        OWLNamedIndividual subject = DataFactory.getOWLNamedIndividual(INDIVIDUAL_IRI);
        PlainNamedIndividualFrame from = PlainNamedIndividualFrame.get(
                subject,
                ImmutableSet.of(),
                ImmutableSet.of(),
                ImmutableSet.of(
                        PlainPropertyLiteralValue.get(
                                DataFactory.getOWLDataProperty(TEMP_PROPERTY),
                                DataFactory.getOWLLiteral("20"),
                                State.ASSERTED)
                )
        );

        Map<String, String> patch = new LinkedHashMap<>();
        patch.put("http://example.org/hasStatus", "RUNNING");

        ImmutableSet<PlainPropertyValue> merged =
                IndividualFramePropertyMapper.mergePropertyValues(from, patch);
        Map<String, String> asMap = IndividualFramePropertyMapper.toPropertyMap(
                PlainNamedIndividualFrame.get(subject, ImmutableSet.of(), ImmutableSet.of(), merged)
        );

        assertThat(asMap, hasEntry(TEMP_PROPERTY, "20"));
        assertThat(asMap, hasEntry("http://example.org/hasStatus", "RUNNING"));
    }

    @Test
    public void shouldDefaultNewPropertyToLiteral() {
        OWLNamedIndividual subject = DataFactory.getOWLNamedIndividual(INDIVIDUAL_IRI);
        PlainNamedIndividualFrame from = PlainNamedIndividualFrame.get(
                subject,
                ImmutableSet.of(),
                ImmutableSet.of(),
                ImmutableSet.of()
        );

        ImmutableSet<PlainPropertyValue> values = IndividualFramePropertyMapper.toPropertyValues(
                from,
                Collections.singletonMap(TEMP_PROPERTY, "42")
        );

        assertThat(values, hasSize(1));
        assertThat(values.iterator().next(), instanceOf(PlainPropertyLiteralValue.class));
    }

    @Test
    public void shouldPreserveClassPropertyKindOnUpdate() {
        String typeProperty = "http://example.org/hasType";
        OWLNamedIndividual subject = DataFactory.getOWLNamedIndividual(INDIVIDUAL_IRI);
        PlainNamedIndividualFrame from = PlainNamedIndividualFrame.get(
                subject,
                ImmutableSet.of(),
                ImmutableSet.of(),
                ImmutableSet.of(
                        PlainPropertyClassValue.get(
                                DataFactory.getOWLObjectProperty(typeProperty),
                                DataFactory.getOWLClass("http://example.org/OldType"),
                                State.ASSERTED)
                )
        );

        ImmutableSet<PlainPropertyValue> updated = IndividualFramePropertyMapper.toPropertyValues(
                from,
                Collections.singletonMap(typeProperty, "http://example.org/NewType")
        );

        assertThat(updated, hasSize(1));
        PlainPropertyValue only = updated.iterator().next();
        assertThat(only, instanceOf(PlainPropertyClassValue.class));
        assertThat(((PlainPropertyClassValue) only).getValue().getIRI().toString(),
                   is("http://example.org/NewType"));
    }

    @Test
    public void shouldPreserveDatatypePropertyKindOnUpdate() {
        String datatypeProperty = "http://example.org/hasDatatype";
        OWLNamedIndividual subject = DataFactory.getOWLNamedIndividual(INDIVIDUAL_IRI);
        PlainNamedIndividualFrame from = PlainNamedIndividualFrame.get(
                subject,
                ImmutableSet.of(),
                ImmutableSet.of(),
                ImmutableSet.of(
                        PlainPropertyDatatypeValue.get(
                                DataFactory.getOWLDataProperty(datatypeProperty),
                                DataFactory.getOWLDatatype("http://www.w3.org/2001/XMLSchema#string"),
                                State.ASSERTED)
                )
        );

        ImmutableSet<PlainPropertyValue> updated = IndividualFramePropertyMapper.toPropertyValues(
                from,
                Collections.singletonMap(datatypeProperty, "http://www.w3.org/2001/XMLSchema#integer")
        );

        assertThat(updated, hasSize(1));
        PlainPropertyValue only = updated.iterator().next();
        assertThat(only, instanceOf(PlainPropertyDatatypeValue.class));
        assertThat(((PlainPropertyDatatypeValue) only).getValue().getIRI().toString(),
                   is("http://www.w3.org/2001/XMLSchema#integer"));
    }

    @Test
    public void shouldPreserveAnnotationPropertyKindOnUpdate() {
        String labelProperty = "http://www.w3.org/2000/01/rdf-schema#label";
        OWLNamedIndividual subject = DataFactory.getOWLNamedIndividual(INDIVIDUAL_IRI);
        PlainNamedIndividualFrame from = PlainNamedIndividualFrame.get(
                subject,
                ImmutableSet.of(),
                ImmutableSet.of(),
                ImmutableSet.of(
                        PlainPropertyAnnotationValue.get(
                                DataFactory.getOWLAnnotationProperty(labelProperty),
                                DataFactory.getOWLLiteral("old"),
                                State.ASSERTED)
                )
        );

        ImmutableSet<PlainPropertyValue> updated = IndividualFramePropertyMapper.toPropertyValues(
                from,
                Collections.singletonMap(labelProperty, "new")
        );

        assertThat(updated, hasSize(1));
        PlainPropertyValue only = updated.iterator().next();
        assertThat(only, instanceOf(PlainPropertyAnnotationValue.class));
        assertThat(IndividualFramePropertyMapper.toPropertyMap(
                PlainNamedIndividualFrame.get(subject, ImmutableSet.of(), ImmutableSet.of(), updated)
        ), hasEntry(labelProperty, "new"));
    }
}
