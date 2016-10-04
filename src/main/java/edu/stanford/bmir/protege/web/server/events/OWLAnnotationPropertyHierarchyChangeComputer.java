package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyChangeComputer;
import edu.stanford.bmir.protege.web.server.hierarchy.OWLObjectHierarchyProvider;
import edu.stanford.bmir.protege.web.shared.hierarchy.AnnotationPropertyHierarchyParentAddedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.AnnotationPropertyHierarchyParentRemovedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyChangedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.inject.Inject;

/**
* Matthew Horridge
* Stanford Center for Biomedical Informatics Research
* 22/05/15
*/
public class OWLAnnotationPropertyHierarchyChangeComputer extends HierarchyChangeComputer<OWLAnnotationProperty> {

    @Inject
    public OWLAnnotationPropertyHierarchyChangeComputer(ProjectId projectId, OWLObjectHierarchyProvider<OWLAnnotationProperty> hierarchyProvider) {
        super(projectId, EntityType.ANNOTATION_PROPERTY, hierarchyProvider, HierarchyId.ANNOTATION_PROPERTY_HIERARCHY);
    }

    @Override
    protected HierarchyChangedEvent<OWLAnnotationProperty, ?> createRemovedEvent(OWLAnnotationProperty child, OWLAnnotationProperty parent) {
        return new AnnotationPropertyHierarchyParentRemovedEvent(getProjectId(), child, parent, HierarchyId.ANNOTATION_PROPERTY_HIERARCHY);
    }

    @Override
    protected HierarchyChangedEvent<OWLAnnotationProperty, ?> createAddedEvent(OWLAnnotationProperty child, OWLAnnotationProperty parent) {
        return new AnnotationPropertyHierarchyParentAddedEvent(getProjectId(), child, parent, HierarchyId.ANNOTATION_PROPERTY_HIERARCHY);
    }
}
