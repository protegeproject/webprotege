package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyChangeComputer;
import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyProvider;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.hierarchy.ObjectPropertyHierarchyParentAddedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.ObjectPropertyHierarchyParentRemovedEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;

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
    protected Collection<? extends ProjectEvent<?>> createRemovedEvents(OWLObjectProperty child, OWLObjectProperty parent) {
        return Collections.singletonList(
                new ObjectPropertyHierarchyParentRemovedEvent(getProjectId(), child, parent, HierarchyId.OBJECT_PROPERTY_HIERARCHY)
        );
    }

    @Override
    protected Collection<? extends ProjectEvent<?>> createAddedEvents(OWLObjectProperty child, OWLObjectProperty parent) {
        return Collections.singletonList(
                new ObjectPropertyHierarchyParentAddedEvent(getProjectId(), child, parent, HierarchyId.OBJECT_PROPERTY_HIERARCHY)
        );
    }
}
