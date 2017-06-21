package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesResult;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.hierarchy.AssertedClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.msg.OWLMessageFormatter;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.server.renderer.RenderingManager;
import edu.stanford.bmir.protege.web.shared.ObjectPath;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.CREATE_CLASS;
import static edu.stanford.bmir.protege.web.shared.access.BuiltInAction.EDIT_ONTOLOGY;
import static java.util.stream.Collectors.toSet;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class CreateClassesActionHandler extends AbstractHasProjectActionHandler<CreateClassesAction, CreateClassesResult> {

    @Nonnull
    private final AssertedClassHierarchyProvider classHierarchyProvider;

    @Nonnull
    private final RenderingManager renderingManager;

    @Nonnull
    private final HasApplyChanges changeApplicator;

    @Nonnull
    @RootOntology
    private final OWLOntology rootOntology;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Inject
    public CreateClassesActionHandler(@Nonnull AccessManager accessManager,
                                      @Nonnull AssertedClassHierarchyProvider classHierarchyProvider,
                                      @Nonnull RenderingManager renderingManager,
                                      @Nonnull HasApplyChanges changeApplicator,
                                      @Nonnull OWLOntology rootOntology,
                                      @Nonnull OWLDataFactory dataFactory) {
        super(accessManager);
        this.classHierarchyProvider = classHierarchyProvider;
        this.renderingManager = renderingManager;
        this.changeApplicator = changeApplicator;
        this.rootOntology = rootOntology;
        this.dataFactory = dataFactory;
    }

    @Override
    public Class<CreateClassesAction> getActionClass() {
        return CreateClassesAction.class;
    }

    @Nonnull
    @Override
    protected List<BuiltInAction> getRequiredExecutableBuiltInActions() {
        return Arrays.asList(CREATE_CLASS, EDIT_ONTOLOGY);
    }

    @Override
    public CreateClassesResult execute(CreateClassesAction action, ExecutionContext executionContext) {
        Set<List<OWLClass>> paths = classHierarchyProvider.getPathsToRoot(action.getSuperClass());
        if(paths.isEmpty()) {
            throw new IllegalStateException("Class does not exist in hierarchy: " + renderingManager
                                                                                           .getBrowserText(action.getSuperClass()));
        }
        ObjectPath<OWLClass> pathToRoot = new ObjectPath<OWLClass>(paths.iterator().next());

        final CreateClassesChangeGenerator gen = new CreateClassesChangeGenerator(action.getBrowserTexts(),
                                                                                  Optional.of(action.getSuperClass()),
                                                                                  rootOntology,
                                                                                  dataFactory);
        ChangeApplicationResult<Set<OWLClass>> result = changeApplicator.applyChanges(executionContext.getUserId(), gen, createChangeText(action));

        Set<OWLClass> createdClasses = result.getSubject().get();

        Set<OWLClassData> classData = createdClasses.stream()
                                                  .map(cls -> renderingManager.getRendering(cls))
                                                  .collect(toSet());
        return new CreateClassesResult(pathToRoot, classData);
    }

    private ChangeDescriptionGenerator<Set<OWLClass>> createChangeText(CreateClassesAction action) {
        action.getBrowserTexts();
        String msg;
        if(action.getBrowserTexts().size() > 1) {
            msg = "Added {0} as subclasses of {1}";
        }
        else {
            msg = "Added {0} as a subclass of {1}";
        }
        return new FixedMessageChangeDescriptionGenerator<>(OWLMessageFormatter.formatMessage(msg, renderingManager, action.getBrowserTexts(), action.getSuperClass()));
    }
}
