package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.AnnotationAxiomsByIriReferenceIndex;
import edu.stanford.bmir.protege.web.server.index.AxiomsByEntityReferenceIndex;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import java.util.Collections;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
@RunWith(MockitoJUnitRunner.class)
public class AxiomsByReferenceIndexImpl_TestCase {

    private AxiomsByReferenceIndexImpl impl;

    @Mock
    private AxiomsByEntityReferenceIndex axiomsByEntityReferenceIndex;

    @Mock
    private AnnotationAxiomsByIriReferenceIndex axiomsByIriReferenceIndex;

    @Mock
    private OntologyDocumentId ontologyDocumentId;

    @Mock
    private OWLEntity entity;

    @Mock
    private IRI entityIri;

    @Mock
    private OWLAxiom entityRefAxiom;

    @Mock
    private OWLAnnotationAxiom iriRefAxiom;

    @Before
    public void setUp() {
        when(entity.getIRI()).thenReturn(entityIri);
        when(axiomsByEntityReferenceIndex.getReferencingAxioms(any(), any())).thenReturn(Stream.empty());
        when(axiomsByEntityReferenceIndex.getReferencingAxioms(entity,
                                                               ontologyDocumentId)).thenReturn(Stream.of(entityRefAxiom));
        when(axiomsByIriReferenceIndex.getReferencingAxioms(any(), any())).thenReturn(Stream.empty());
        when(axiomsByIriReferenceIndex.getReferencingAxioms(entityIri,
                                                            ontologyDocumentId)).thenReturn(Stream.of(iriRefAxiom));
        impl = new AxiomsByReferenceIndexImpl(axiomsByEntityReferenceIndex, axiomsByIriReferenceIndex);
    }

    @Test
    public void shouldGetDependencies() {
        assertThat(impl.getDependencies(), containsInAnyOrder(axiomsByEntityReferenceIndex, axiomsByIriReferenceIndex));
    }

    @Test
    public void shouldGetAxiomsByReference() {
        var referencingAxiomsStream = impl.getReferencingAxioms(Collections.singleton(entity), ontologyDocumentId);
        var referencingAxioms = referencingAxiomsStream.collect(toSet());
        assertThat(referencingAxioms, hasItems(entityRefAxiom, iriRefAxiom));
    }

    @Test
    public void shouldGetEmptyStreamForUnknownOntologyId() {
        var referencingAxiomsStream = impl.getReferencingAxioms(Collections.singleton(entity), mock(OntologyDocumentId.class));
        var axiomsCount = referencingAxiomsStream.count();
        assertThat(axiomsCount, is(0L));
    }

    @Test
    public void shouldGetEmptyStreamForEmptyEntitiesSet() {
        var referencingAxiomsStream = impl.getReferencingAxioms(Collections.emptySet(), ontologyDocumentId);
        var axiomsCount = referencingAxiomsStream.count();
        assertThat(axiomsCount, is(0L));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullEntitiesSet() {
        impl.getReferencingAxioms(null, ontologyDocumentId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionForNullOntologyId() {
        impl.getReferencingAxioms(Collections.singleton(entity), null);
    }
}
