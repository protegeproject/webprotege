package edu.stanford.bmir.protege.web.server.owlapi.change;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomSubjectProvider;
import org.semanticweb.owlapi.change.AxiomChangeData;
import org.semanticweb.owlapi.change.OWLOntologyChangeRecord;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/05/15
 */
public class EntitiesByRevisionCache {

    private final AxiomSubjectProvider axiomSubjectProvider;

    private final HasContainsEntityInSignature hasContainsEntityInSignature;

    private final OWLDataFactory dataFactory;

    private final Map<Revision, ImmutableSet<OWLEntity>> entity2Revisions = new HashMap<>();

    @Inject
    public EntitiesByRevisionCache(AxiomSubjectProvider axiomSubjectProvider, HasContainsEntityInSignature hasContainsEntityInSignature, OWLDataFactory dataFactory) {
        this.axiomSubjectProvider = axiomSubjectProvider;
        this.hasContainsEntityInSignature = hasContainsEntityInSignature;
        this.dataFactory = dataFactory;
    }

    public boolean containsEntity(Revision revision, OWLEntity entity) {
        return getEntities(revision).contains(entity);
    }

    public ImmutableSet<OWLEntity> getEntities(Revision revision) {
        ImmutableSet<OWLEntity> cachedEntities = entity2Revisions.get(revision);
        if(cachedEntities != null) {
            return cachedEntities;
        }
        ImmutableSet<OWLEntity> entitiesToCache = getEntitiesInternal(revision);
        entity2Revisions.put(revision, entitiesToCache);
        return entitiesToCache;
    }

    private ImmutableSet<OWLEntity> getEntitiesInternal(Revision revision) {
        ImmutableSet.Builder<OWLEntity> result = ImmutableSet.builder();
        Set<IRI> iris = new HashSet<>();
        for (OWLOntologyChangeRecord change : revision) {
            if (change.getData() instanceof AxiomChangeData) {
                OWLAxiom ax = ((AxiomChangeData) change.getData()).getAxiom();
                Optional<? extends OWLObject> subject = axiomSubjectProvider.getSubject(ax);
                if (subject.isPresent()) {
                    if (subject.get() instanceof OWLEntity) {
                        result.add((OWLEntity) subject.get());
                    }
                    else if (subject.get() instanceof IRI) {
                        iris.add((IRI) subject.get());
                    }
                }
            }
        }
        for (IRI iri : iris) {
            for(EntityType<?> entityType : EntityType.values()) {
                OWLEntity entity = dataFactory.getOWLEntity(entityType, iri);
                if(hasContainsEntityInSignature.containsEntityInSignature(entity)) {
                    result.add(entity);
                }
            }
        }
        return result.build();
    }
}
