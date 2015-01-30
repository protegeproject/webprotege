package edu.stanford.bmir.protege.web.server.shortform;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.HasPrefixedName;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.util.IRIShortFormProvider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/01/15
 */
@RunWith(MockitoJUnitRunner.class)
public class WebProtegeShortFormProvider_TestCase {

    public static final String SHORT_FORM = "SHORT_FORM";
    public static final String PREFIXED_FORM = "PREFIXED_FORM";

    @Mock
    private IRI iri;

    @Mock
    private OWLEntity entity;

    @Mock(extraInterfaces = HasPrefixedName.class)
    private OWLEntity entityWithPrefixedName;

    @Mock
    private IRIShortFormProvider iriShortFormProvider;

    private WebProtegeShortFormProvider shortFromProvider;



    @Before
    public void setUp() throws Exception {
        when(entity.getIRI()).thenReturn(iri);
        when(iriShortFormProvider.getShortForm(iri)).thenReturn(SHORT_FORM);
        shortFromProvider = new WebProtegeShortFormProvider(iriShortFormProvider);

        when(((HasPrefixedName) entityWithPrefixedName).getPrefixedName()).thenReturn(PREFIXED_FORM);
    }

    @Test
    public void shouldReturnIRIShortForm() {
        assertThat(shortFromProvider.getShortForm(entity), is(SHORT_FORM));
    }

    @Test
    public void shouldReturnPrefixedForm() {
        assertThat(shortFromProvider.getShortForm(entityWithPrefixedName), is(PREFIXED_FORM));
    }
}
