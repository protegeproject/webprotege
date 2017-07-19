package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassResult;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.hierarchy.ClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.msg.OWLMessageFormatter;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.ObjectPath;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_CLASS;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class CreateClassActionHandler extends AbstractProjectChangeHandler<OWLClass, CreateClassAction, CreateClassResult> {

    private static final Logger logger = LoggerFactory.getLogger(CreateClassActionHandler.class);

    @Nonnull
    private final OWLOntology rootOntology;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final ClassHierarchyProvider classHierarchyProvider;

    @Inject
    public CreateClassActionHandler(@Nonnull AccessManager accessManager,
                                    @Nonnull EventManager<ProjectEvent<?>> eventManager,
                                    @Nonnull HasApplyChanges applyChanges,
                                    @Nonnull OWLOntology rootOntology,
                                    @Nonnull RenderingManager renderingManager,
                                    @Nonnull ClassHierarchyProvider classHierarchyProvider) {
        super(accessManager, eventManager, applyChanges);
        this.rootOntology = rootOntology;
        this.renderingManager = renderingManager;
        this.classHierarchyProvider = classHierarchyProvider;
    }

    @Override
    public Class<CreateClassAction> getActionClass() {
        return CreateClassAction.class;
    }

    @Nonnull
    @Override
    protected Iterable<BuiltInAction> getRequiredExecutableBuiltInActions() {
        return Arrays.asList(CREATE_CLASS, EDIT_ONTOLOGY);
    }

    @Override
    protected ChangeListGenerator<OWLClass> getChangeListGenerator(final CreateClassAction action,
                                                                   ExecutionContext executionContext) {
        return new CreateClassChangeGenerator(action.getBrowserText(), action.getSuperClass(), rootOntology);
    }

    @Override
    protected ChangeDescriptionGenerator<OWLClass> getChangeDescription(CreateClassAction action,
                                                                        ExecutionContext executionContext) {
        return new FixedMessageChangeDescriptionGenerator<>(OWLMessageFormatter.formatMessage("Created {0} as a subclass of {1}", renderingManager, action.getBrowserText(), action.getSuperClass()));
    }

    @Override
    protected CreateClassResult createActionResult(ChangeApplicationResult<OWLClass> changeApplicationResult,
                                                   CreateClassAction action,
                                                   ExecutionContext executionContext,
                                                   EventList<ProjectEvent<?>> eventList) {
        final OWLClass subclass = changeApplicationResult.getSubject().get();
        final ObjectPath<OWLClass> pathToRoot = getPathToRoot(subclass, action.getSuperClass());
        return new CreateClassResult(renderingManager.getRendering(subclass),
                                     pathToRoot,
                                     eventList);
    }

    private ObjectPath<OWLClass> getPathToRoot(OWLClass subClass, OWLClass superClass) {
        Set<List<OWLClass>> paths = classHierarchyProvider.getPathsToRoot(subClass);
        if(paths.isEmpty()) {
            logger.info("[WARNING] Path to root not found for SubClass: %s and SuperClass: %", superClass);
            return new ObjectPath<>();
        }
        ObjectPath<OWLClass> pathToRoot = null;
        for(List<OWLClass> path : paths) {
            if(path.size() > 1 && path.get(path.size() - 2).equals(superClass)) {
                pathToRoot = new ObjectPath<>(path);
                break;
            }
        }
        if(pathToRoot == null) {
            pathToRoot = new ObjectPath<>();
        }
        return pathToRoot;
    }


}
