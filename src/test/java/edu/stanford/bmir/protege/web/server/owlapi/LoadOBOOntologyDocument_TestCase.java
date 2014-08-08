package edu.stanford.bmir.protege.web.server.owlapi;


import edu.stanford.bmir.protege.web.server.owlapi.manager.WebProtegeOWLManager;
import org.coode.owlapi.obo.parser.OBOOntologyFormat;
import org.junit.Test;
import org.semanticweb.owlapi.model.*;

import java.io.BufferedInputStream;
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
        BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
        OWLOntology ont = man.loadOntologyFromOntologyDocument(inputStream);
        inputStream.close();
        OWLOntologyFormat ontologyFormat = man.getOntologyFormat(ont);
        assertThat(ontologyFormat instanceof OBOOntologyFormat, is(true));
    }
}
