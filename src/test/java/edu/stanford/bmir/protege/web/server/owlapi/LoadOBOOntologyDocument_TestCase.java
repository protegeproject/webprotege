package edu.stanford.bmir.protege.web.server.owlapi;


import org.junit.Test;
import org.semanticweb.owlapi.formats.OBODocumentFormat;
import org.semanticweb.owlapi.model.*;

import java.io.IOException;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 08/08/2014
 */
public class LoadOBOOntologyDocument_TestCase {


    private static final String SOURCE_DOCUMENT = "/ontologies/obo/go.fragment.obo";

    @Test
    public void shouldLoadOBOOntology() throws IOException, OWLOntologyCreationException {
        URL url = LoadOBOOntologyDocument_TestCase.class.getResource(SOURCE_DOCUMENT);
        OWLOntologyManager man = WebProtegeOWLManager.createOWLOntologyManager();
        OWLOntology ont = man.loadOntologyFromOntologyDocument(IRI.create(url));
        OWLDocumentFormat format = man.getOntologyFormat(ont);
        assertThat(format instanceof OBODocumentFormat, is(true));
    }
}
