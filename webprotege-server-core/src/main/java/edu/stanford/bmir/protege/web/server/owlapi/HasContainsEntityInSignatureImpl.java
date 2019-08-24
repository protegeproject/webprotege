package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureIndex;
import org.semanticweb.owlapi.model.HasContainsEntityInSignature;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 02/06/15
 */
public class HasContainsEntityInSignatureImpl implements HasContainsEntityInSignature {

    private final EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex;

    @Inject
    public HasContainsEntityInSignatureImpl(EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex) {
        this.entitiesInProjectSignatureIndex = entitiesInProjectSignatureIndex;
    }

    @Override
    public boolean containsEntityInSignature(@Nonnull OWLEntity owlEntity) {
        return entitiesInProjectSignatureIndex.containsEntityInSignature(owlEntity);
    }
}
