package edu.stanford.bmir.protege.web.shared.obo;

import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Oct 2018
 */
public class OboId_TestCase {

    @Test
    public void shouldGetOboId() {
        IRI iri = IRI.create("http://purl.obolibrary.org/obo/GO_1234567");
        Optional<String> id = OboId.getOboId(iri);
        assertThat(id, is(Optional.of("GO:1234567")));
    }

    @Test
    public void shouldNotGetOboId() {
        IRI iri = IRI.create("http://purl.obolibrary.org/obo/partOf");
        Optional<String> id = OboId.getOboId(iri);
        assertThat(id, is(Optional.empty()));
    }
}
