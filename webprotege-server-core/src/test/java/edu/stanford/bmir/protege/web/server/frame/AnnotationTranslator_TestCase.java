package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.renderer.ContextRenderer;
import edu.stanford.bmir.protege.web.shared.entity.IRIData;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.frame.*;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
    private OWLAnnotationProperty property;

    @Mock
    private OWLLiteral literal;

    @Mock
    private IRI iri;

    @Before
    public void setUp() {
        translator = new AnnotationTranslator(entitiesIndex);
    }

    @Test
    public void shouldTranslateAnnotationWithLiteralValue() {
        var annotation = new OWLAnnotationImpl(property, literal, Collections.emptySet());
        Set<PlainPropertyValue> propertyValues =  translator.translate(annotation, State.ASSERTED);
        assertThat(propertyValues, Matchers.hasItem(PlainPropertyAnnotationValue.get(property, literal, State.ASSERTED)));
    }

    @Test
    public void shouldTranslateSpecifiedState() {
        var annotation = new OWLAnnotationImpl(property, literal, Collections.emptySet());
        Set<PlainPropertyValue> propertyValues =  translator.translate(annotation, State.DERIVED);
        assertThat(propertyValues, Matchers.hasItem(PlainPropertyAnnotationValue.get(property, literal, State.DERIVED)));
    }

    @Test
    public void shouldIri() {
        var annotation = new OWLAnnotationImpl(property, iri, Collections.emptySet());
        Set<PlainPropertyValue> propertyValues =  translator.translate(annotation, State.ASSERTED);
        assertThat(propertyValues, Matchers.hasItem(PlainPropertyAnnotationValue.get(property, iri, State.ASSERTED)));
    }
}
