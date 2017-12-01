package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyChangeComputer;
import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyProvider;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.DataPropertyHierarchyParentAddedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLDataProperty;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;

/**
* Matthew Horridge
* Stanford Center for Biomedical Informatics Research
* 22/05/15
*/
public class OWLDataPropertyHierarchyChangeComputer extends HierarchyChangeComputer<OWLDataProperty> {

    @Inject
    public OWLDataPropertyHierarchyChangeComputer(ProjectId projectId, HierarchyProvider<OWLDataProperty> hierarchyProvider) {
        super(projectId, EntityType.DATA_PROPERTY, hierarchyProvider, HierarchyId.DATA_PROPERTY_HIERARCHY);
    }

    @Override
    protected Collection<? extends ProjectEvent<?>> createRemovedEvents(OWLDataProperty child, OWLDataProperty parent) {
        return Collections.singletonList(
                new DataPropertyHierarchyParentAddedEvent(getProjectId(), child, parent, HierarchyId.DATA_PROPERTY_HIERARCHY)
        );
    }

    @Override
    protected Collection<? extends ProjectEvent<?>> createAddedEvents(OWLDataProperty child, OWLDataProperty parent) {
        return Collections.singletonList(
                new DataPropertyHierarchyParentAddedEvent(getProjectId(), child, parent, HierarchyId.DATA_PROPERTY_HIERARCHY)
        );
    }
}
