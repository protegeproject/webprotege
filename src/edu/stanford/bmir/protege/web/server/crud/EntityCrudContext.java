package edu.stanford.bmir.protege.web.server.crud;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.HasDataFactory;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 */
public class EntityCrudContext implements HasDataFactory {

    public OWLOntology getTargetOntology() {
        return null;
    }

    public OWLDataFactory getDataFactory() {
        return null;
    }

    public Optional<String> getTargetLanguage() {
        return Optional.absent();
    }
}
