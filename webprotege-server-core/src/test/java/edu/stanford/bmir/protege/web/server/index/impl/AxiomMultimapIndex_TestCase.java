package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-07
 */
@RunWith(MockitoJUnitRunner.class)
public class AxiomMultimapIndex_TestCase {

    private final KeyValueExtractor<OWLClass, OWLSubClassOfAxiom> extractor = this::extractSubClassIfNamed;

    private AxiomMultimapIndex<OWLClass, OWLSubClassOfAxiom> index;

    private Multimap<Key<OWLClass>, OWLSubClassOfAxiom> backingMap =
            MultimapBuilder.hashKeys()
                           .arrayListValues()
                           .build();

    @Mock
    private OWLClass subCls;

    @Mock
    private OWLSubClassOfAxiom axiom;

    @Mock
    private OWLOntologyID ontologyId;

    private OWLClass extractSubClassIfNamed(OWLSubClassOfAxiom sca) {
        return sca.getSubClass()
                  .isNamed() ? sca.getSubClass()
                                  .asOWLClass() : null;
    }

    @Before
    public void setUp() {
        index = new AxiomMultimapIndex<>(backingMap,
                                         extractor,
                                         OWLSubClassOfAxiom.class);
        when(axiom.getSubClass())
                .thenReturn(subCls);

        when(subCls.isNamed())
                .thenReturn(true);

        when(subCls.asOWLClass())
                .thenReturn(subCls);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyIdIsNull() {
        index.getAxioms(subCls, null);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfValueIsNull() {
        index.getAxioms(null, ontologyId);
    }

    @Test
    public void shouldGetEmptyStreamBeforeAnyChange() {
        var axiomsStream = index.getAxioms(subCls, ontologyId);
        assertThat(axiomsStream.count(), is(0L));
    }

    @Test
    public void shouldContainAxiomInBackingMapAfterAdd() {
        var chg = AddAxiomChange.of(ontologyId, axiom);
        index.handleOntologyChanges(List.of(chg));
        assertThat(backingMap.values(), contains(axiom));
    }

    @Test
    public void shouldNotContainAxiomInBackingMapAfterRemove() {
        var chg = AddAxiomChange.of(ontologyId, axiom);
        index.handleOntologyChanges(List.of(chg));
        assertThat(backingMap.values(), contains(axiom));
        var remChg = RemoveAxiomChange.of(ontologyId, axiom);
        index.handleOntologyChanges(List.of(remChg));
        assertThat(backingMap.values(), not(contains(axiom)));

    }

    @Test
    public void shouldNotAddDuplicates() {
        var chg = AddAxiomChange.of(ontologyId, axiom);
        index.handleOntologyChanges(List.of(chg, chg));
        assertThat(backingMap.size(), is(1));
    }

    @Test
    public void shouldNotAddAxiomsWithNullKeyValue() {
        var otherCls = mock(OWLClassExpression.class);
        var subClassOfAxiom = mock(OWLSubClassOfAxiom.class);
        when(subClassOfAxiom.getSubClass())
                .thenReturn(otherCls);
        when(otherCls.isAnonymous())
                .thenReturn(true);
        index.handleOntologyChanges(List.of(AddAxiomChange.of(ontologyId, subClassOfAxiom)));
        assertThat(backingMap.size(), is(0));
    }

    @Test
    public void shouldIgnoreIrrelevantChanges() {
        var otherAxiom = mock(OWLClassAssertionAxiom.class);
        index.handleOntologyChanges(List.of(AddAxiomChange.of(ontologyId, otherAxiom)));
        assertThat(index.getAxioms(subCls, ontologyId)
                        .count(), is(0L));
    }
}
