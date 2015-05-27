package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyChangeComputer;
import edu.stanford.bmir.protege.web.server.hierarchy.OWLObjectHierarchyProvider;
import edu.stanford.bmir.protege.web.shared.hierarchy.ClassHierarchyParentAddedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.ClassHierarchyParentRemovedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyChangedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;

import javax.inject.Inject;

/**
* Matthew Horridge
* Stanford Center for Biomedical Informatics Research
* 22/05/15
*/
public class OWLClassHierarchyChangeComputer extends HierarchyChangeComputer<OWLClass> {

    @Inject
    public OWLClassHierarchyChangeComputer(ProjectId projectId, OWLObjectHierarchyProvider<OWLClass> hierarchyProvider) {
        super(projectId, EntityType.CLASS, hierarchyProvider, HierarchyId.CLASS_HIERARCHY);
    }

    @Override
    protected HierarchyChangedEvent<OWLClass, ?> createRemovedEvent(OWLClass child, OWLClass parent) {
        return new ClassHierarchyParentRemovedEvent(getProjectId(), child, parent, HierarchyId.CLASS_HIERARCHY);
    }

    @Override
    protected HierarchyChangedEvent<OWLClass, ?> createAddedEvent(OWLClass child, OWLClass parent) {
        return new ClassHierarchyParentAddedEvent(getProjectId(), child, parent, HierarchyId.CLASS_HIERARCHY);
    }
}
