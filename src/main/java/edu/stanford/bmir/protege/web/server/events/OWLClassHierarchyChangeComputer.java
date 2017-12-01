package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyChangeComputer;
import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyProvider;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;

/**
* Matthew Horridge
* Stanford Center for Biomedical Informatics Research
* 22/05/15
*/
public class OWLClassHierarchyChangeComputer extends HierarchyChangeComputer<OWLClass> {

    @Inject
    public OWLClassHierarchyChangeComputer(ProjectId projectId, HierarchyProvider<OWLClass> hierarchyProvider) {
        super(projectId, EntityType.CLASS, hierarchyProvider, HierarchyId.CLASS_HIERARCHY);
    }

    @Override
    protected Collection<? extends ProjectEvent<?>> createRemovedEvents(OWLClass child, OWLClass parent) {
        return Collections.singletonList(
                new ClassHierarchyParentRemovedEvent(getProjectId(), child, parent, HierarchyId.CLASS_HIERARCHY)
        );
    }

    @Override
    protected Collection<? extends ProjectEvent<?>> createAddedEvents(OWLClass child, OWLClass parent) {
        return Collections.singletonList(
                new ClassHierarchyParentAddedEvent(getProjectId(), child, parent, HierarchyId.CLASS_HIERARCHY)
        );
    }
}
