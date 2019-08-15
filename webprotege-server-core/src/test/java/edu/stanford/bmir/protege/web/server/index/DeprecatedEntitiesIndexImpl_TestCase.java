package edu.stanford.bmir.protege.web.server.index;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-15
 */
@RunWith(MockitoJUnitRunner.class)
public class DeprecatedEntitiesIndexImpl_TestCase {

    private DeprecatedEntitiesIndexImpl impl;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private AnnotationAssertionAxiomsBySubjectIndex annotationAssertionsIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLEntity entity;

    @Mock
    private IRI entityIri;

    @Mock
    private OWLAnnotationAssertionAxiom annotationAssertion;

    @Before
    public void setUp() {
        impl = new DeprecatedEntitiesIndexImpl(projectOntologiesIndex, annotationAssertionsIndex);

        when(projectOntologiesIndex.getOntologyIds())
                .thenReturn(Stream.of(ontologyId));

        when(annotationAssertionsIndex.getAxiomsForSubject(any(), any()))
                .thenReturn(Stream.empty());
        when(annotationAssertionsIndex.getAxiomsForSubject(entityIri, ontologyId))
                .thenReturn(Stream.of(annotationAssertion));

        when(entity.getIRI())
                .thenReturn(entityIri);
    }

    @Test
    public void shouldNotFindEntityToBeDeprecated() {
        var deprecated = impl.isDeprecated(entity);
        assertThat(deprecated, Matchers.is(false));
    }

    @Test
    public void shouldFindEntityToBeDeprecated() {
        when(annotationAssertion.isDeprecatedIRIAssertion())
                .thenReturn(true);
        var deprecated = impl.isDeprecated(entity);
        assertThat(deprecated, Matchers.is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfEntityIsNull() {
        impl.isDeprecated(null);
    }
}
