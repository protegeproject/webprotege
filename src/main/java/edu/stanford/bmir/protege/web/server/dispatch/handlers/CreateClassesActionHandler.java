package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesResult;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.change.CreateClassesChangeGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.msg.OWLMessageFormatter;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.ObjectPath;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import org.semanticweb.owlapi.model.OWLClass;

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
 * Date: 22/02/2013
 */
public class CreateClassesActionHandler extends AbstractHasProjectActionHandler<CreateClassesAction, CreateClassesResult> {

    @Inject
    public CreateClassesActionHandler(ProjectManager projectManager,
                                      AccessManager accessManager) {
        super(projectManager, accessManager);
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
    protected CreateClassesResult execute(CreateClassesAction action, Project project, ExecutionContext executionContext) {
        Set<List<OWLClass>> paths = project.getClassHierarchyProvider().getPathsToRoot(action.getSuperClass());
        if(paths.isEmpty()) {
            throw new IllegalStateException("Class does not exist in hierarchy: " + project.getRenderingManager().getBrowserText(action.getSuperClass()));
        }
        ObjectPath<OWLClass> pathToRoot = new ObjectPath<OWLClass>(paths.iterator().next());

        final CreateClassesChangeGenerator gen = new CreateClassesChangeGenerator(action.getBrowserTexts(), Optional.of(action.getSuperClass()));
        ChangeApplicationResult<Set<OWLClass>> result = project.applyChanges(executionContext.getUserId(), gen, createChangeText(project, action));

        Set<OWLClass> createdClasses = result.getSubject().get();

        BrowserTextMap browserTextMap = BrowserTextMap.build(project.getRenderingManager(), action.getSuperClass(), createdClasses);

        return new CreateClassesResult(pathToRoot, createdClasses, browserTextMap);
    }

    private ChangeDescriptionGenerator<Set<OWLClass>> createChangeText(Project project, CreateClassesAction action) {
        action.getBrowserTexts();
        String msg;
        if(action.getBrowserTexts().size() > 1) {
            msg = "Added {0} as subclasses of {1}";
        }
        else {
            msg = "Added {0} as a subclass of {1}";
        }
        return new FixedMessageChangeDescriptionGenerator<>(OWLMessageFormatter.formatMessage(msg, project, action.getBrowserTexts(), action.getSuperClass()));
    }
}
