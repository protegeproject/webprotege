package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.OWLOntologyID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-20
 */
public interface OntologyIdIndex {


    /**
     * Returns the {@link OWLOntologyID} of the document specified by {@link OntologyDocumentId}.
     * @param ontId This may be an anonymous ontology.
     */
    OWLOntologyID getOntologyId(OntologyDocumentId ontId);
}
