package edu.stanford.bmir.protege.web.shared.match;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.match.criteria.*;
import org.junit.Test;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jun 2018
 */
public class RootCriteria_Serialization_TestCase {

    @Test
    public void shouldSerialize_EntityAnnotationCriteria_AtLeastOne() throws IOException {
        testSerialization(
                EntityAnnotationCriteria.get(
                        AnnotationComponentsCriteria.get(
                                AnyAnnotationPropertyCriteria.get(),
                                AnyAnnotationValueCriteria.get()
                        ),
                        AnnotationPresence.AT_LEAST_ONE
                )
        );
    }

    @Test
    public void shouldSerialize_EntityAnnotationCriteria_AtMostOne() throws IOException {
        testSerialization(
                EntityAnnotationCriteria.get(
                        AnnotationComponentsCriteria.get(
                                AnyAnnotationPropertyCriteria.get(),
                                AnyAnnotationValueCriteria.get()
                        ),
                        AnnotationPresence.AT_MOST_ONE
                )
        );
    }

    @Test
    public void shouldSerialize_EntityAnnotationCriteria_None() throws IOException {
        testSerialization(
                EntityAnnotationCriteria.get(
                        AnnotationComponentsCriteria.get(
                                AnyAnnotationPropertyCriteria.get(),
                                AnyAnnotationValueCriteria.get()
                        ),
                        AnnotationPresence.NONE
                )
        );
    }

    @Test
    public void shouldSerialize_EntityIsDeprecatedCriteria() throws IOException {
        testSerialization(EntityIsDeprecatedCriteria.get());
    }

    @Test
    public void shouldSerialize_EntityIsNoteDeprecatedCriteria() throws IOException {
        testSerialization(EntityIsNotDeprecatedCriteria.get());
    }

    @Test
    public void shouldSerialize_EntityHasNonUniqueLangTagsCriteria() throws IOException {
        testSerialization(EntityHasNonUniqueLangTagsCriteria.get(
                AnyAnnotationPropertyCriteria.get()
        ));
    }

    @Test
    public void shouldSerialize_EntityHasConflictingBooleanAnnotationValuesCriteria() throws IOException {
        testSerialization(
                EntityHasConflictingBooleanAnnotationValuesCriteria.get(
                        AnyAnnotationPropertyCriteria.get()
                )
        );
    }

    @Test
    public void shouldSerialize_EntityAnnotationValuesAreNotDisjointCriteria() throws IOException {
        testSerialization(
                EntityAnnotationValuesAreNotDisjointCriteria.get(
                        AnyAnnotationPropertyCriteria.get(),
                        AnyAnnotationPropertyCriteria.get()
                )
        );
    }

    @Test
    public void shouldSerialize_EntityTypeOneOfCriteria() throws IOException {
        testSerialization(
                EntityTypeIsOneOfCriteria.get(
                        ImmutableSet.of(EntityType.CLASS, EntityType.NAMED_INDIVIDUAL)
                )
        );
    }

    @Test
    public void shouldSerialize_IsNotBuiltInEntityCriteria() throws IOException {
        testSerialization(
                IsNotBuiltInEntityCriteria.get()
        );
    }

    @Test
    public void shouldSerialize_SubClassOfCriteria() throws IOException {
        testSerialization(
                SubClassOfCriteria.get(new OWLClassImpl(OWLRDFVocabulary.OWL_THING.getIRI()),
                                       HierarchyFilterType.ALL)
        );
    }

    private static <V extends RootCriteria> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, RootCriteria.class);
    }

}
