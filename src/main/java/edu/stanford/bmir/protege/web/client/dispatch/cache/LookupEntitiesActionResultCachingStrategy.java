package edu.stanford.bmir.protege.web.client.dispatch.cache;

import edu.stanford.bmir.protege.web.shared.entity.EntityLookupResult;
import edu.stanford.bmir.protege.web.shared.entity.LookupEntitiesAction;
import edu.stanford.bmir.protege.web.shared.entity.LookupEntitiesResult;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/11/2013
 */
public class LookupEntitiesActionResultCachingStrategy extends AbstractResultCachingStrategy<LookupEntitiesAction, LookupEntitiesResult, OWLEntity> {

    public LookupEntitiesActionResultCachingStrategy(ProjectId projectId) {
        super(projectId);
    }

    @Override
    public Class<LookupEntitiesAction> getActionClass() {
        return LookupEntitiesAction.class;
    }

    @Override
    public boolean shouldCache(LookupEntitiesAction action, LookupEntitiesResult result) {
        return false;//!result.getEntityLookupResults().isEmpty();
    }

    @Override
    public Collection<OWLEntity> getInvalidationKeys(LookupEntitiesAction action, LookupEntitiesResult result) {
        List<OWLEntity> entities = new ArrayList<OWLEntity>();
        for(EntityLookupResult res : result.getEntityLookupResults()) {
            OWLEntity entity = res.getOWLEntityData().getEntity();
            entities.add(entity);
        }
        return entities;
    }

    @Override
    public void registerEventHandlers() {
        registerProjectEventHandler(ClassFrameChangedEvent.TYPE, new ClassFrameChangedEventHandler() {
            @Override
            public void classFrameChanged(ClassFrameChangedEvent event) {
                fireResultsInvalidatedEvent(event.getEntity());
            }
        });
        registerProjectEventHandler(ObjectPropertyFrameChangedEvent.TYPE, new ObjectPropertyFrameChangedEventHandler() {
            @Override
            public void objectPropertyFrameChanged(ObjectPropertyFrameChangedEvent event) {
                fireResultsInvalidatedEvent(event.getEntity());
            }
        });
        registerProjectEventHandler(DataPropertyFrameChangedEvent.TYPE, new DataPropertyFrameChangedEventHandler() {
            @Override
            public void dataPropertyFrameChanged(DataPropertyFrameChangedEvent event) {
                fireResultsInvalidatedEvent(event.getEntity());
            }
        });
        registerProjectEventHandler(AnnotationPropertyFrameChangedEvent.TYPE, new AnnotationPropertyFrameChangedEventHandler() {
            @Override
            public void annotationPropertyFrameChanged(AnnotationPropertyFrameChangedEvent event) {
                fireResultsInvalidatedEvent(event.getEntity());
            }
        });
        registerProjectEventHandler(NamedIndividualFrameChangedEvent.TYPE, new NamedIndividualFrameChangedEventHandler() {
            @Override
            public void namedIndividualFrameChanged(NamedIndividualFrameChangedEvent event) {
                fireResultsInvalidatedEvent(event.getEntity());
            }
        });
        registerProjectEventHandler(DatatypeFrameChangedEvent.TYPE, new DatatypeFrameChangedEventHandler() {
            @Override
            public void datatypeFrameChanged(DatatypeFrameChangedEvent event) {
                fireResultsInvalidatedEvent(event.getEntity());
            }
        });
    }
}
