package edu.stanford.bmir.protege.web.server.dispatch.handlers;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassAction;
import edu.stanford.bmir.protege.web.client.dispatch.actions.CreateClassResult;
import edu.stanford.bmir.protege.web.server.change.*;
import edu.stanford.bmir.protege.web.server.dispatch.*;
import edu.stanford.bmir.protege.web.server.dispatch.validators.UserHasProjectWritePermissionValidator;
import edu.stanford.bmir.protege.web.server.msg.OWLMessageFormatter;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.shared.BrowserTextMap;
import edu.stanford.bmir.protege.web.shared.ObjectPath;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.events.EventList;
import org.semanticweb.owlapi.model.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/02/2013
 */
public class CreateClassActionHandler extends AbstractProjectChangeHandler<OWLClass, CreateClassAction, CreateClassResult> {


    @Override
    public Class<CreateClassAction> getActionClass() {
        return CreateClassAction.class;
    }

    @Override
    protected RequestValidator getAdditionalRequestValidator(CreateClassAction action, RequestContext requestContext) {
        return new UserHasProjectWritePermissionValidator();
    }


    @Override
    protected ChangeListGenerator<OWLClass> getChangeListGenerator(final CreateClassAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new CreateClassChangeGenerator(action.getBrowserText(), action.getSuperClass());
//        return new ChangeListGenerator<OWLClass>() {
//            @Override
//            public GeneratedOntologyChanges<OWLClass> generateChanges(OWLAPIProject project, ChangeGenerationContext context) {
//                GeneratedOntologyChanges<Set<OWLClass>> temp = new CreateClassChangeGenerator(Collections.singleton(action.getBrowserText()), Optional.of(action.getSuperClass())).generateChanges(project, context);
//                GeneratedOntologyChanges.Builder<OWLClass> builder = new GeneratedOntologyChanges.Builder<OWLClass>();
//                builder.addAll(temp.getChanges());
//                return builder.build(temp.getResult().get().iterator().next());
//            }
//
//            @Override
//            public OWLClass getRenamedResult(OWLClass result, RenameMap renameMap) {
//                return renameMap.getRenamedEntity(result);
//            }
//        };
    }

    @Override
    protected ChangeDescriptionGenerator<OWLClass> getChangeDescription(CreateClassAction action, OWLAPIProject project, ExecutionContext executionContext) {
        return new FixedMessageChangeDescriptionGenerator<OWLClass>(OWLMessageFormatter.formatMessage("Created {0} as a subclass of {1}", project, action.getBrowserText(), action.getSuperClass()));
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

//    @Override
//    protected CreateClassResult executeProjectChanges(CreateClassAction action, OWLAPIProject project, ExecutionContext executionContext) {
//
//        final UserId userId = executionContext.getUserId();
//        final OWLClass superClass = action.getParent();
//
//        CreateClassChangeGenerator changeListGenerator = new CreateClassChangeGenerator(action.getBrowserText(), superClass);
//        ProjectChangeMessages msg = GWT.create(ProjectChangeMessages.class);
//        String desc = msg.createdClass(action.getBrowserText(), project.getRenderingManager().getBrowserText(action.getParent()));
//        ChangeApplicationResult<OWLClass> result = project.applyChanges(userId, changeListGenerator, desc);
//        OWLClass actualClass = result.getSubject().get();
//
//        ObjectPath<OWLClass> pathToRoot = getPathToRoot(project, actualClass, superClass);
//
//        BrowserTextMap.Builder builder = new BrowserTextMap.Builder();
//        builder.addAll(actualClass);
//        builder.addAll(pathToRoot);
//        final BrowserTextMap browserTextMap = builder.build(project.getRenderingManager());
//
//        return new CreateClassResult(actualClass, pathToRoot, browserTextMap);
//    }




    private ObjectPath<OWLClass> getPathToRoot(OWLAPIProject project, OWLClass subClass, OWLClass superClass) {
        Set<List<OWLClass>> paths = project.getClassHierarchyProvider().getPathsToRoot(subClass);
        ObjectPath<OWLClass> pathToRoot = null;
        for(List<OWLClass> path : paths) {
            if(path.get(path.size() - 2).equals(superClass)) {
                pathToRoot = new ObjectPath<OWLClass>(path);
                break;
            }
        }
        if(pathToRoot == null) {
            throw new RuntimeException("Internal error:  Path to root does not exist.");
        }
        return pathToRoot;
    }


}
