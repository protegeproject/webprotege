package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntityProvider;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Class;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.SubClassOf;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-16
 */
@RunWith(MockitoJUnitRunner.class)
public class AxiomsByEntityReferenceIndexImpl_ContainsEntityInOntologyAxiomsSignature_TestCase {

    private AxiomsByEntityReferenceIndexImpl impl;

    @Mock
    private OWLEntityProvider entityProvider;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private IRI subClsIri;

    @Mock
    private IRI superClsIri;

    private OWLClass subCls;

    private OWLClass superCls;

    @Before
    public void setUp() {
        subCls = Class(subClsIri);
        superCls = Class(superClsIri);
        var axiom = SubClassOf(subCls, superCls);
        impl = new AxiomsByEntityReferenceIndexImpl(entityProvider);
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyId, axiom)));
    }

    @Test
    public void shouldContainSubClass() {
        assertThat(impl.containsEntityInOntologyAxiomsSignature(subCls, ontologyId), is(true));
    }

    @Test
    public void shouldContainSuperClass() {
        assertThat(impl.containsEntityInOntologyAxiomsSignature(superCls, ontologyId), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfEntityIsNull() {
        impl.containsEntityInOntologyAxiomsSignature(null, ontologyId);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyIdIsNull() {
        impl.containsEntityInOntologyAxiomsSignature(subCls, null);
    }

    @Test
    public void shouldReturnFalseForUnknownEntity() {
        assertThat(impl.containsEntityInOntologyAxiomsSignature(Class(mock(IRI.class)), ontologyId), is(false));
    }

    @Test
    public void shouldReturnFalseForUnknowOntology() {
        assertThat(impl.containsEntityInOntologyAxiomsSignature(subCls, mock(OWLOntologyID.class)), is(false));
    }
}
