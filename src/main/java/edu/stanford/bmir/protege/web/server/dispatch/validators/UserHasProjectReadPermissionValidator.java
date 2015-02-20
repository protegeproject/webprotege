package edu.stanford.bmir.protege.web.server.dispatch.validators;

import edu.stanford.bmir.protege.web.client.ui.constants.OntologyShareAccessConstants;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.metaproject.ProjectPermissionsManager;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.smi.protege.server.metaproject.Operation;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class UserHasProjectReadPermissionValidator<A extends Action<?> & HasProjectId> implements RequestValidator<A> {

    public static <A extends Action<?> & HasProjectId> UserHasProjectReadPermissionValidator<A> get() {
        return new UserHasProjectReadPermissionValidator<A>();
    }

    @Override
    public RequestValidationResult validateAction(A action, RequestContext requestContext) {
        // TODO: NEEDS FIXING
        return RequestValidationResult.getValid();
//        ProjectId projectId = action.getProjectId();
//        ProjectPermissionsManager mpm = MetaProjectManager.getManager();
//        Collection<Operation> ops = mpm.getAllowedOperations(projectId.getId(), requestContext.getUserId().getUserName());
//        for(Operation op : ops) {
//            if(op.getName().equals(OntologyShareAccessConstants.PROJECT_READ_ONLY_ACCESS_OPERATION)) {
//                return RequestValidationResult.getValid();
//            }
//        }
//        return RequestValidationResult.getInvalid("Required read permission of project");

    }
}
