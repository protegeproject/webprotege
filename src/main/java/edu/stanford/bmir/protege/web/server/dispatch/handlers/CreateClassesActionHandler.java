package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassesResult;
import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.change.ChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.change.CreateClassesChangeGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractHasProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectReadPermissionValidator;
import edu.stanford.bmir.protege.web.server.msg.OWLMessageFormatter;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.ObjectPath;
import org.semanticweb.owlapi.model.OWLClass;

import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class CreateClassesActionHandler extends AbstractHasProjectActionHandler<CreateClassesAction, CreateClassesResult> {

    @Override
    public Class<CreateClassesAction> getActionClass() {
        return CreateClassesAction.class;
    }

    @Override
    protected RequestValidator<CreateClassesAction> getAdditionalRequestValidator(CreateClassesAction action, RequestContext requestContext) {
        return UserHasProjectReadPermissionValidator.get();
    }

    @Override
    protected CreateClassesResult execute(CreateClassesAction action, OWLAPIProject project, ExecutionContext executionContext) {
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

    private ChangeDescriptionGenerator<Set<OWLClass>> createChangeText(OWLAPIProject project, CreateClassesAction action) {
        return new FixedMessageChangeDescriptionGenerator<Set<OWLClass>>(OWLMessageFormatter.formatMessage("Created {0} as subclasses of {1}", project, action.getBrowserTexts(), action.getSuperClass()));
    }
}
