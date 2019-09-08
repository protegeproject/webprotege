package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory;
import org.semanticweb.owlapi.model.*;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.SubClassOf;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-07
 */
@RunWith(MockitoJUnitRunner.class)
public class AxiomsByEntityReferenceIndexImpl_TestCase {

    AxiomsByEntityReferenceIndexImpl impl;

    @Mock
    private OWLOntologyID ontologyId;

    private OWLClass subCls = OWLFunctionalSyntaxFactory.Class(mock(IRI.class));

    private OWLClass superCls = OWLFunctionalSyntaxFactory.Class(mock(IRI.class));

    private OWLAxiom axiom = SubClassOf(subCls, superCls);

    @Mock
    private OWLEntityProvider entityProvider;

    @Before
    public void setUp() {
        impl = new AxiomsByEntityReferenceIndexImpl(entityProvider);
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, axiom)));
    }

    @Test
    public void shouldRetrieveAxiomByEntityReferenceForKnownOntology() {
        var axiomStream = impl.getReferencingAxioms(subCls, ontologyId);
        var axioms = axiomStream.collect(toSet());
        assertThat(axioms, hasItem(axiom));
    }

    @Test
    public void shouldRetrieveEmptyStreamForUnknownOntology() {
        var axiomStream = impl.getReferencingAxioms(subCls, mock(OWLOntologyID.class));
        var axioms = axiomStream.collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEForNullOntology() {
        impl.getReferencingAxioms(null, ontologyId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldNPEForNullOntologyId() {
        impl.getReferencingAxioms(subCls, null);
    }
}
