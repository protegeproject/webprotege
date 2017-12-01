package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyChangeComputer;
import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyProvider;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.AnnotationPropertyHierarchyParentAddedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.AnnotationPropertyHierarchyParentRemovedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;

/**
* Matthew Horridge
* Stanford Center for Biomedical Informatics Research
* 22/05/15
*/
public class OWLAnnotationPropertyHierarchyChangeComputer extends HierarchyChangeComputer<OWLAnnotationProperty> {

    @Inject
    public OWLAnnotationPropertyHierarchyChangeComputer(ProjectId projectId, HierarchyProvider<OWLAnnotationProperty> hierarchyProvider) {
        super(projectId, EntityType.ANNOTATION_PROPERTY, hierarchyProvider, HierarchyId.ANNOTATION_PROPERTY_HIERARCHY);
    }

    @Override
    protected Collection<? extends ProjectEvent<?>> createRemovedEvents(OWLAnnotationProperty child, OWLAnnotationProperty parent) {
        return Collections.singletonList(
                new AnnotationPropertyHierarchyParentRemovedEvent(getProjectId(), child, parent, HierarchyId.ANNOTATION_PROPERTY_HIERARCHY)
        );
    }

    @Override
    protected Collection<? extends ProjectEvent<?>> createAddedEvents(OWLAnnotationProperty child, OWLAnnotationProperty parent) {
        return Collections.singletonList(
                new AnnotationPropertyHierarchyParentAddedEvent(getProjectId(), child, parent, HierarchyId.ANNOTATION_PROPERTY_HIERARCHY)
        );
    }
}
