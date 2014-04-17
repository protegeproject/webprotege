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

    private OWLOntology targetOntology;

    private OWLDataFactory dataFactory;

    private PrefixedNameExpander prefixedNameExpander;

    public EntityCrudContext(OWLOntology targetOntology, OWLDataFactory dataFactory, PrefixedNameExpander prefixedNameExpander) {
        this.targetOntology = targetOntology;
        this.dataFactory = dataFactory;
        this.prefixedNameExpander = prefixedNameExpander;
    }

    public OWLOntology getTargetOntology() {
        return targetOntology;
    }

    public OWLDataFactory getDataFactory() {
        return dataFactory;
    }

    public PrefixedNameExpander getPrefixedNameExpander() {
        return prefixedNameExpander;
    }

    public Optional<String> getTargetLanguage() {
        return Optional.absent();
    }
}
