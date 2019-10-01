package edu.stanford.bmir.protege.web.server.change;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLOntologyChange;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-29
 */
@RunWith(MockitoJUnitRunner.class)
public class OwlOntologyChangeTranslator_TestCase {

    private OwlOntologyChangeTranslator translator;

    @Mock
    private OwlOntologyChangeTranslatorVisitor visitor;

    @Mock
    private OWLOntologyChange change;

    @Mock
    private OntologyChange ontologyChange;

    @Before
    public void setUp() {
        translator = new OwlOntologyChangeTranslator(visitor);
        when(change.accept(visitor))
                .thenReturn(ontologyChange);
    }

    @Test
    public void shouldGetChange() {
        var result = translator.toOntologyChange(change);
        assertThat(result, is(ontologyChange));
    }
}
