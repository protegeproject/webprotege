package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-20
 */
public class OwlApiOntologyDocumentTempOWLOntologyIDTranslator {

    private static final String PREFIX = "urn:webprotege:ontology:";

    @Nonnull
    public static OWLOntologyID toOWLOntologyID(@Nonnull OntologyDocumentId documentId) {
        return new OWLOntologyID(IRI.create(PREFIX + documentId.getId()));
    }

    /**
     * Gets a previously translated {@link OntologyDocumentId} from an {@link OWLOntologyID}.
     */
    @Nonnull
    public static OntologyDocumentId toOntologyDocumentId(@Nonnull OWLOntologyID ontologyID) {
        if(!ontologyID.getOntologyIRI().isPresent()) {
            throw new IllegalArgumentException();
        }
        var uuidString = ontologyID.getOntologyIRI()
                                   // Has to be there
                                   .get()
                                   .toString()
                                   .substring(PREFIX.length());
        return OntologyDocumentId.get(uuidString);
    }

}
