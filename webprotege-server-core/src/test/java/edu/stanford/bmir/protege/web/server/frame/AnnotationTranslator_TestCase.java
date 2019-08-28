package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.frame.PropertyAnnotationValue;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValue;
import edu.stanford.bmir.protege.web.shared.frame.State;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLLiteral;
import uk.ac.manchester.cs.owl.owlapi.OWLAnnotationImpl;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-14
 */
@RunWith(MockitoJUnitRunner.class)
public class AnnotationTranslator_TestCase {

    private AnnotationTranslator translator;

    @Mock
    private EntitiesInProjectSignatureByIriIndex entitiesIndex;

    @Mock
    private ContextRenderer renderer;

    @Mock
    private OWLAnnotationProperty property;

    @Mock
    private OWLAnnotationPropertyData propertyData;

    @Mock
    private OWLLiteral literal;

    @Mock
    private OWLPrimitiveData literalData;

    @Mock
    private IRI iri;

    @Mock
    private IRIData iriData;

    @Mock
    private OWLClass cls;

    @Mock
    private OWLClassData clsData;

    @Before
    public void setUp() {
        translator = new AnnotationTranslator(entitiesIndex, renderer);

        when(renderer.getAnnotationPropertyData(property))
                .thenReturn(propertyData);
        when(renderer.getAnnotationValueData(literal))
                .thenReturn(literalData);
        when(renderer.getAnnotationValueData(iri))
                .thenReturn(iriData);
        when(renderer.getEntityData(cls))
                .thenReturn(clsData);
    }

    @Test
    public void shouldTranslateAnnotationWithLiteralValue() {
        var annotation = new OWLAnnotationImpl(property, literal, Collections.emptySet());
        Set<PropertyValue> propertyValues =  translator.translate(annotation, State.ASSERTED);
        assertThat(propertyValues, Matchers.hasItem(PropertyAnnotationValue.get(propertyData, literalData, State.ASSERTED)));
    }

    @Test
    public void shouldTranslateSpecifiedState() {
        var annotation = new OWLAnnotationImpl(property, literal, Collections.emptySet());
        Set<PropertyValue> propertyValues =  translator.translate(annotation, State.DERIVED);
        assertThat(propertyValues, Matchers.hasItem(PropertyAnnotationValue.get(propertyData, literalData, State.DERIVED)));
    }

    @Test
    public void shouldTranslateIriNotInSignature() {
        when(entitiesIndex.getEntityInSignature(iri))
                .thenReturn(Stream.empty());
        var annotation = new OWLAnnotationImpl(property, iri, Collections.emptySet());
        Set<PropertyValue> propertyValues =  translator.translate(annotation, State.ASSERTED);
        assertThat(propertyValues, Matchers.hasItem(PropertyAnnotationValue.get(propertyData, iriData, State.ASSERTED)));
    }

    @Test
    public void shouldTranslateIriInSignature() {
        when(entitiesIndex.getEntityInSignature(iri))
                .thenReturn(Stream.of(cls));
        var annotation = new OWLAnnotationImpl(property, iri, Collections.emptySet());
        Set<PropertyValue> propertyValues =  translator.translate(annotation, State.ASSERTED);
        assertThat(propertyValues, Matchers.hasItem(PropertyAnnotationValue.get(propertyData, clsData, State.ASSERTED)));
    }
}
