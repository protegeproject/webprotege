package edu.stanford.bmir.protege.web.server.mansyntax.render;

import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 20/02/2014
 */
public class EntityIRICheckerImpl implements EntityIRIChecker {

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex signatureIndex;

    @Inject
    public EntityIRICheckerImpl(@Nonnull EntitiesInProjectSignatureByIriIndex signatureIndex) {
        this.signatureIndex = checkNotNull(signatureIndex);
    }

    @Override
    public boolean isEntityIRI(IRI iri) {
        return iri.isTopEntity() || iri.isBottomEntity() || signatureIndex.getEntitiesInSignature(iri).limit(1).count() == 0;
    }

    @Override
    public Collection<OWLEntity> getEntitiesWithIRI(IRI iri) {
        return signatureIndex.getEntitiesInSignature(iri).collect(Collectors.toSet());
    }
}
