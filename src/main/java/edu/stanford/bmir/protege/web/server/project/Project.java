package edu.stanford.bmir.protege.web.server.project;

import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.change.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.crud.ProjectEntityCrudKitHandlerCache;
import edu.stanford.bmir.protege.web.server.crud.persistence.ProjectEntityCrudKitSettings;
import edu.stanford.bmir.protege.web.server.crud.persistence.ProjectEntityCrudKitSettingsRepository;
import edu.stanford.bmir.protege.web.server.dispatch.impl.ProjectActionHandlerRegistry;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.hierarchy.OWLAnnotationPropertyHierarchyProvider;
import edu.stanford.bmir.protege.web.server.hierarchy.OWLDataPropertyHierarchyProvider;
import edu.stanford.bmir.protege.web.server.hierarchy.OWLObjectPropertyHierarchyProvider;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.mansyntax.ManchesterSyntaxFrameParser;
import edu.stanford.bmir.protege.web.server.metrics.OWLAPIProjectMetricsManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.server.revision.ProjectChangesManager;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.server.watches.WatchManager;
import edu.stanford.bmir.protege.web.server.watches.WatchedChangesManager;
import edu.stanford.bmir.protege.web.shared.HasDataFactory;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.HasGetEntitiesWithIRI;
import edu.stanford.bmir.protege.web.shared.axiom.*;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.object.*;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.revision.RevisionSummary;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.util.ShortFormProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Comparator;
import java.util.Set;

import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.OWL_DEPRECATED;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/03/2012
 */
@Deprecated
@ProjectSingleton
public class Project {

    private final ProjectId projectId;

    private final EventManager<ProjectEvent<?>> projectEventManager;

    private final OWLOntology ontology;

    private final RevisionManager changeManager;

    @Nonnull
    private final ProjectActionHandlerRegistry actionHandlerRegistry;

    @Inject
    public Project(ProjectId projectId,
                   EventManager<ProjectEvent<?>> projectEventManager,
                   @RootOntology OWLOntology ontology,
                   RevisionManager changeManager,
                   @Nonnull ProjectActionHandlerRegistry actionHandlerRegistry) {
        this.projectId = projectId;
        this.projectEventManager = projectEventManager;
        this.ontology = ontology;
        this.changeManager = changeManager;
        this.actionHandlerRegistry = actionHandlerRegistry;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public EventManager<ProjectEvent<?>> getEventManager() {
        return projectEventManager;
    }

    public RevisionManager getRevisionManager() {
        return changeManager;
    }

    public OWLOntology getRootOntology() {
        return ontology;
    }

    public ProjectActionHandlerRegistry getActionHanderRegistry() {
        return actionHandlerRegistry;
    }
}
