package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassResult;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectChangeHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectWritePermissionValidator;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.msg.OWLMessageFormatter;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.ObjectPath;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.semanticweb.owlapi.model.OWLClass;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class CreateClassActionHandler extends AbstractProjectChangeHandler<OWLClass, CreateClassAction, CreateClassResult> {

    private final WebProtegeLogger logger;

    @Inject
    public CreateClassActionHandler(OWLAPIProjectManager projectManager, WebProtegeLogger logger) {
        super(projectManager);
        this.logger = logger;
    }

    @Override
    public Class<CreateClassAction> getActionClass() {
        return CreateClassAction.class;
    }

    @Override
    protected RequestValidator<CreateClassAction> getAdditionalRequestValidator(CreateClassAction action, RequestContext requestContext) {
        return UserHasProjectWritePermissionValidator.get();
    }


    @Override
    protected ChangeListGenerator<OWLClass> getChangeListGenerator(final CreateClassAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new CreateClassChangeGenerator(action.getBrowserText(), action.getSuperClass());
    }

    @Override
    protected ChangeDescriptionGenerator<OWLClass> getChangeDescription(CreateClassAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new FixedMessageChangeDescriptionGenerator<>(OWLMessageFormatter.formatMessage("Created {0} as a subclass of {1}", project, action.getBrowserText(), action.getSuperClass()));
    }

    @Override
    protected CreateClassResult createActionResult(ChangeApplicationResult<OWLClass> changeApplicationResult, CreateClassAction action, OWLAPIProject project, ExecutionContext executionContext, EventList<ProjectEvent<?>> eventList) {
        final OWLClass subclass = changeApplicationResult.getSubject().get();
        final ObjectPath<OWLClass> pathToRoot = getPathToRoot(project, subclass, action.getSuperClass());
        BrowserTextMap.Builder browserTextMap = new BrowserTextMap.Builder();
        browserTextMap.add(subclass);
        browserTextMap.addAll(pathToRoot);
        return new CreateClassResult(subclass, pathToRoot, browserTextMap.build(project.getRenderingManager()), eventList);
    }

    private ObjectPath<OWLClass> getPathToRoot(OWLAPIProject project, OWLClass subClass, OWLClass superClass) {
        Set<List<OWLClass>> paths = project.getClassHierarchyProvider().getPathsToRoot(subClass);
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
