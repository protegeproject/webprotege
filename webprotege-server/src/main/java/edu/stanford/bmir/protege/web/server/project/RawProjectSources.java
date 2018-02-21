package edu.stanford.bmir.protege.web.server.project;

import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;

import java.io.IOException;
import java.util.Collection;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
public interface RawProjectSources {

    /**
     * Gets the ontology document sources.
     * @return The document source.  Not {@code null}.
     */
    Collection<OWLOntologyDocumentSource> getDocumentSources();

    /**
     * Gets an ontology IRI mapper, which should be used during loading so that imports etc. are loaded
     * correctly.
     * @return An IRI mapper.  Not {@code null}.
     */
    OWLOntologyIRIMapper getOntologyIRIMapper();

    /**
     * Cleans up any temporary files associated with this set of raw project resources.
     */
    void cleanUpTemporaryFiles() throws IOException;

}
