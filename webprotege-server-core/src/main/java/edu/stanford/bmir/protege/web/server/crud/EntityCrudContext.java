package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.shared.HasDataFactory;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

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

    public EntityCrudContext(@Nonnull UserId userId,
                             @Nonnull OWLOntology targetOntology,
                             @Nonnull OWLDataFactory dataFactory,
                             @Nonnull PrefixedNameExpander prefixedNameExpander) {
        this.userId = checkNotNull(userId);
        this.targetOntology = checkNotNull(targetOntology);
        this.dataFactory = checkNotNull(dataFactory);
        this.prefixedNameExpander = checkNotNull(prefixedNameExpander);
    }

    @Nonnull
    public OWLOntology getTargetOntology() {
        return targetOntology;
    }

    @Nonnull
    public UserId getUserId() {
        return userId;
    }

    @Nonnull
    public OWLDataFactory getDataFactory() {
        return dataFactory;
    }

    @Nonnull
    public PrefixedNameExpander getPrefixedNameExpander() {
        return prefixedNameExpander;
    }

    @Nonnull
    public Optional<String> getTargetLanguage() {
        return Optional.empty();
    }
}
