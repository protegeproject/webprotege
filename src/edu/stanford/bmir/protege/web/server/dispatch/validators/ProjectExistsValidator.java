package edu.stanford.bmir.protege.web.server.dispatch.validators;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.server.dispatch.RequestContext;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidationResult;
import edu.stanford.bmir.protege.web.server.dispatch.RequestValidator;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectDocumentStore;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public class ProjectExistsValidator<A extends Action<R> & HasProjectId, R extends Result> implements RequestValidator<A> {

    @Override
    public RequestValidationResult validateAction(A action, RequestContext requestContext) {
        ProjectId projectId = action.getProjectId();
        OWLAPIProjectDocumentStore ds = OWLAPIProjectDocumentStore.getProjectDocumentStore(projectId);
        if(ds.exists()) {
            return RequestValidationResult.getValid();
        }
        else {
            return RequestValidationResult.getInvalid("Project does not exist");
        }
    }
}
