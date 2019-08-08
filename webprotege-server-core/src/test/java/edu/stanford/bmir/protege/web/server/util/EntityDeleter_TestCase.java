package edu.stanford.bmir.protege.web.server.util;

import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.server.change.OntologyChangeFactory;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import java.util.Collections;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-08
 */
@RunWith(MockitoJUnitRunner.class)
public class EntityDeleter_TestCase {

    private EntityDeleter entityDeleter;

    @Mock
    private ReferenceFinder referenceFinder;

    @Mock
    private OntologyChangeFactory ontologyChangeFactory;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLEntity entity;

    @Mock
    private ReferenceFinder.ReferenceSet referenceSet;

    @Mock
    private OWLAxiom axiom;

    @Mock
    private OWLAnnotation ontologyAnnotation;

    @Mock
    private RemoveAxiom removeAxiom;

    @Mock
    private RemoveOntologyAnnotation removeOntologyAnnotation;

    @Before
    public void setUp() {
        when(projectOntologiesIndex.getOntologyIds())
                .thenReturn(Stream.of(ontologyId));

        when(referenceFinder.getReferenceSet(Collections.singleton(entity), ontologyId))
                .thenReturn(referenceSet);

        when(referenceSet.getOntologyId())
                .thenReturn(ontologyId);
        when(referenceSet.getReferencingAxioms())
                .thenReturn(ImmutableSet.of(axiom));
        when(referenceSet.getReferencingOntologyAnnotations())
                .thenReturn(ImmutableSet.of(ontologyAnnotation));

        when(ontologyChangeFactory.createRemoveAxiom(ontologyId, axiom))
                .thenReturn(removeAxiom);

        when(ontologyChangeFactory.createRemoveOntologyAnnotation(ontologyId, ontologyAnnotation))
                .thenReturn(removeOntologyAnnotation);

        entityDeleter = new EntityDeleter(referenceFinder,
                                          ontologyChangeFactory,
                                          projectOntologiesIndex);


    }

    @Test
    public void shouldGetOntologyChangesToDeleteEntity() {
        var changes = entityDeleter.getChangesToDeleteEntities(Collections.singleton(entity));
        assertThat(changes, hasItems(removeAxiom, removeOntologyAnnotation));
    }

    @Test
    public void shouldGetEmptyChangesForEmptyEntitiesSet() {
        var changes = entityDeleter.getChangesToDeleteEntities(Collections.emptySet());
        assertThat(changes.isEmpty(), is(true));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeForNullEntitiesSet() {
        entityDeleter.getChangesToDeleteEntities(null);
    }
}
