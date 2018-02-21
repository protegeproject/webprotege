package edu.stanford.bmir.protege.web.server.shortform;

import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;

import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 25 Jul 16
 *
 * Extends the BidirectionalShortFormProviderAdapter and provides public access to the remove() method allowing
 * non-builtin entities to be removed.
 */
public class BidirectionalShortFormProviderAdapterEx extends BidirectionalShortFormProviderAdapter {

    /**
     * Creates a BidirectionalShortFormProvider that maps between the entities
     * that are referenced in the specified ontologies and the short forms of
     * these entities.
     *
     * @param ontologies        The ontologies that contain references to the entities to be
     *                          mapped.
     * @param shortFormProvider The short form provider that should be used to generate the short
     */
    public BidirectionalShortFormProviderAdapterEx(Set<OWLOntology> ontologies, ShortFormProvider shortFormProvider) {
        super(ontologies, shortFormProvider);
    }

    /**
     * Removes an entity and its short form from the cache.  Only non-BuiltIn entities are removed.
     *
     * @param entity The entity to be removed.  The entity will only be removed if {@code OWLEntity.isBuiltIn} returns
     *               {@code false}.
     */
    @Override
    public void remove(OWLEntity entity) {
        if (!entity.isBuiltIn()) {
            super.remove(entity);
        }
    }
}
