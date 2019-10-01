package edu.stanford.bmir.protege.web.server.renderer;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.mansyntax.render.IRIOrdinalProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 04/10/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class IRIOrdinalProvider_TestCase {

    private IRIOrdinalProvider provider;

    @Mock
    private IRI firstIRI, secondIRI;

    @Mock
    private IRI otherIRI;


    @Before
    public void setUp() throws Exception {
        ImmutableList<IRI> list = ImmutableList.of(firstIRI, secondIRI);
        provider = new IRIOrdinalProvider(list);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new IRIOrdinalProvider(null);
    }

    @Test
    public void shouldReturnDefaultIndexWithRDFSLabelFirst() {
        IRIOrdinalProvider iriOrdinalProvider = IRIOrdinalProvider.withDefaultAnnotationPropertyOrdering();
        int labelIndex = iriOrdinalProvider.getIndex(OWLRDFVocabulary.RDFS_LABEL.getIRI());
        assertThat(labelIndex, is(0));
    }

    @Test
    public void shouldReturnZero() {
        assertThat(provider.getIndex(firstIRI), is(0));
    }

    @Test
    public void shouldReturnOne() {
        assertThat(provider.getIndex(secondIRI), is(1));
    }

    @Test
    public void shouldReturnTwo() {
        assertThat(provider.getIndex(otherIRI), is(2));
    }

}
