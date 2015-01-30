package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/01/15
 */
public class DefaultLabellingIRIs_TestCase {

    private ImmutableList<IRI> defaultLabellingIRIs;

    @Before
    public void setUp() throws Exception {
        defaultLabellingIRIs = DefaultLabellingIRIs.asImmutableList();
    }

    @Test
    public void shouldContainRDFSLabel() {
        assertThat(defaultLabellingIRIs, hasItem(OWLRDFVocabulary.RDFS_LABEL.getIRI()));
    }

    @Test
    public void shouldContainSKOSPrefLabel() {
        assertThat(defaultLabellingIRIs, hasItem(SKOSVocabulary.PREFLABEL.getIRI()));
    }


}
