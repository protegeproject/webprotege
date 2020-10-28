package edu.stanford.bmir.protege.web.server.index.impl;

import edu.stanford.bmir.protege.web.server.index.OntologyIdIndex;
import edu.stanford.bmir.protege.web.server.owlapi.OwlApiOntologyDocumentTempOWLOntologyIDTranslator;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-20
 */
public class OntologyIdIndexImpl implements OntologyIdIndex {

    @Inject
    public OntologyIdIndexImpl() {
    }

    @Override
    public OWLOntologyID getOntologyId(OntologyDocumentId ontId) {
        // TODO: TEMP
        return OwlApiOntologyDocumentTempOWLOntologyIDTranslator.toOWLOntologyID(ontId);
    }
}
