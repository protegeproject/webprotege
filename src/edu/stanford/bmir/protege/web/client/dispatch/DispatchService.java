package edu.stanford.bmir.protege.web.client.dispatch;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.DispatchServiceResultContainer;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 */
@RemoteServiceRelativePath("dispatchservice")
public interface DispatchService extends RemoteService  {

//    <A extends Action<R>, R extends Result> R executeAction(A action) throws ActionExecutionException;
    DispatchServiceResultContainer executeAction(Action<?> action) throws ActionExecutionException, PermissionDeniedException;


}
