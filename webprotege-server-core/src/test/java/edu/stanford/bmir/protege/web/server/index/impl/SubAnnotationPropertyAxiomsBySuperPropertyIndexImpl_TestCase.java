package edu.stanford.bmir.protege.web.server.index.impl;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.*;

import static java.util.stream.Collectors.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-17
 */
@RunWith(MockitoJUnitRunner.class)
public class SubAnnotationPropertyAxiomsBySuperPropertyIndexImpl_TestCase {

    private SubAnnotationPropertyAxiomsBySuperPropertyIndexImpl impl;

    @Mock
    private OntologyDocumentId ontologyDocumentId;

    @Mock
    private OWLSubAnnotationPropertyOfAxiom axiom;

    @Mock
    private OWLAnnotationProperty property;

    @Before
    public void setUp() {
        when(axiom.getSuperProperty())
                .thenReturn(property);
        impl = new SubAnnotationPropertyAxiomsBySuperPropertyIndexImpl();
        impl.applyChanges(ImmutableList.of(AddAxiomChange.of(ontologyDocumentId, axiom)));
    }

    @Test
    public void shouldGetAxiomForSuperProperty() {
        var axioms = impl.getAxiomsForSuperProperty(property, ontologyDocumentId).collect(toSet());
        assertThat(axioms, contains(axiom));
    }

    @Test
    public void shouldNotGetAxiomForOtherSuperProperty() {
        var axioms = impl.getAxiomsForSuperProperty(mock(OWLAnnotationProperty.class), ontologyDocumentId).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @Test
    public void shouldNotGetAxiomsForUnknownOntology() {
        var axioms = impl.getAxiomsForSuperProperty(property, mock(OntologyDocumentId.class)).collect(toSet());
        assertThat(axioms.isEmpty(), is(true));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfOntologyIsNull() {
        impl.getAxiomsForSuperProperty(property, null);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfPropertyIsNull() {
        impl.getAxiomsForSuperProperty(null, ontologyDocumentId);
    }
}
