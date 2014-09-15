package edu.stanford.bmir.protege.web.server.crud;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.HasDataFactory;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 */
public class EntityCrudContext implements HasDataFactory {

    private final OWLOntology targetOntology;

    private final OWLDataFactory dataFactory;

    private final PrefixedNameExpander prefixedNameExpander;

    private final UserId userId;

    public EntityCrudContext(UserId userId, OWLOntology targetOntology, OWLDataFactory dataFactory, PrefixedNameExpander prefixedNameExpander) {
        this.userId = userId;
        this.targetOntology = targetOntology;
        this.dataFactory = dataFactory;
        this.prefixedNameExpander = prefixedNameExpander;
    }

    public UserId getUserId() {
        return userId;
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
