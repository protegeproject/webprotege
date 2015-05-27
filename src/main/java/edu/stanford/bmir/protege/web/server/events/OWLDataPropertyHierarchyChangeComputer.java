package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyChangeComputer;
import edu.stanford.bmir.protege.web.server.hierarchy.OWLObjectHierarchyProvider;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.hierarchy.DataPropertyHierarchyParentAddedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyChangedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLDataProperty;

import javax.inject.Inject;

/**
* Matthew Horridge
* Stanford Center for Biomedical Informatics Research
* 22/05/15
*/
public class OWLDataPropertyHierarchyChangeComputer extends HierarchyChangeComputer<OWLDataProperty> {

    @Inject
    public OWLDataPropertyHierarchyChangeComputer(ProjectId projectId, OWLObjectHierarchyProvider<OWLDataProperty> hierarchyProvider) {
        super(projectId, EntityType.DATA_PROPERTY, hierarchyProvider, HierarchyId.DATA_PROPERTY_HIERARCHY);
    }

    @Override
    protected HierarchyChangedEvent<OWLDataProperty, ?> createRemovedEvent(OWLDataProperty child, OWLDataProperty parent) {
        return new DataPropertyHierarchyParentAddedEvent(getProjectId(), child, parent, HierarchyId.DATA_PROPERTY_HIERARCHY);
    }

    @Override
    protected HierarchyChangedEvent<OWLDataProperty, ?> createAddedEvent(OWLDataProperty child, OWLDataProperty parent) {
        return new DataPropertyHierarchyParentAddedEvent(getProjectId(), child, parent, HierarchyId.DATA_PROPERTY_HIERARCHY);
    }
}
