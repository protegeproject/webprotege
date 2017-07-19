package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyChangeComputer;
import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyProvider;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyChangedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.hierarchy.ObjectPropertyHierarchyParentAddedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.ObjectPropertyHierarchyParentRemovedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.inject.Inject;

/**
* Matthew Horridge
* Stanford Center for Biomedical Informatics Research
* 22/05/15
*/
public class OWLObjectPropertyHierarchyChangeComputer extends HierarchyChangeComputer<OWLObjectProperty> {

    @Inject
    public OWLObjectPropertyHierarchyChangeComputer(ProjectId projectId, HierarchyProvider<OWLObjectProperty> hierarchyProvider) {
        super(projectId, EntityType.OBJECT_PROPERTY, hierarchyProvider, HierarchyId.OBJECT_PROPERTY_HIERARCHY);
    }

    @Override
    protected HierarchyChangedEvent<OWLObjectProperty, ?> createRemovedEvent(OWLObjectProperty child, OWLObjectProperty parent) {
        return new ObjectPropertyHierarchyParentRemovedEvent(getProjectId(), child, parent, HierarchyId.OBJECT_PROPERTY_HIERARCHY);
    }

    @Override
    protected HierarchyChangedEvent<OWLObjectProperty, ?> createAddedEvent(OWLObjectProperty child, OWLObjectProperty parent) {
        return new ObjectPropertyHierarchyParentAddedEvent(getProjectId(), child, parent, HierarchyId.OBJECT_PROPERTY_HIERARCHY);
    }
}
