package edu.stanford.bmir.protege.web.server.events;

import edu.stanford.bmir.protege.web.server.hierarchy.EntityHierarchyNodeRenderer;
import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyChangeComputer;
import edu.stanford.bmir.protege.web.server.hierarchy.HierarchyProvider;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.AnnotationPropertyHierarchyParentAddedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.AnnotationPropertyHierarchyParentRemovedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyChangedEvent;
import edu.stanford.bmir.protege.web.shared.hierarchy.EntityHierarchyNode;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.protege.gwt.graphtree.shared.graph.*;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;

import javax.inject.Inject;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId.ANNOTATION_PROPERTY_HIERARCHY;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
* Matthew Horridge
* Stanford Center for Biomedical Informatics Research
* 22/05/15
*/
public class OWLAnnotationPropertyHierarchyChangeComputer extends HierarchyChangeComputer<OWLAnnotationProperty> {

    private final EntityHierarchyNodeRenderer renderer;

    private final HierarchyProvider<OWLAnnotationProperty> hierarchyProvider;

    @Inject
    public OWLAnnotationPropertyHierarchyChangeComputer(ProjectId projectId, HierarchyProvider<OWLAnnotationProperty> hierarchyProvider, EntityHierarchyNodeRenderer renderer) {
        super(projectId, EntityType.ANNOTATION_PROPERTY, hierarchyProvider, ANNOTATION_PROPERTY_HIERARCHY, renderer);
        this.renderer = checkNotNull(renderer);
        this.hierarchyProvider = checkNotNull(hierarchyProvider);
    }

    @Override
    protected Collection<? extends ProjectEvent<?>> createRemovedEvents(OWLAnnotationProperty child, OWLAnnotationProperty parent) {
        RemoveEdge<EntityHierarchyNode> removeEdge = new RemoveEdge<>(new GraphEdge<>(
                new GraphNode<>(renderer.render(parent)),
                new GraphNode<>(renderer.render(child))
        ));
        return asList(
                new EntityHierarchyChangedEvent(getProjectId(), ANNOTATION_PROPERTY_HIERARCHY, new GraphModelChangedEvent<>(singletonList(removeEdge))),
                new AnnotationPropertyHierarchyParentRemovedEvent(getProjectId(), child, parent, ANNOTATION_PROPERTY_HIERARCHY)
        );
    }

    @Override
    protected Collection<? extends ProjectEvent<?>> createAddedEvents(OWLAnnotationProperty child, OWLAnnotationProperty parent) {
        AddEdge<EntityHierarchyNode> addEdge = new AddEdge<>(new GraphEdge<>(
                new GraphNode<>(renderer.render(parent), hierarchyProvider.getChildren(parent).isEmpty()),
                new GraphNode<>(renderer.render(child), hierarchyProvider.getChildren(child).isEmpty())
        ));
        return asList(
                new EntityHierarchyChangedEvent(getProjectId(), ANNOTATION_PROPERTY_HIERARCHY, new GraphModelChangedEvent<>(singletonList(addEdge))),
                new AnnotationPropertyHierarchyParentAddedEvent(getProjectId(), child, parent, ANNOTATION_PROPERTY_HIERARCHY)
        );
    }
}
