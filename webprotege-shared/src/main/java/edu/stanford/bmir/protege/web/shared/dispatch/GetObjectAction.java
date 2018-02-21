package edu.stanford.bmir.protege.web.shared.dispatch;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 * <p>
 *     An {@link Action} that retrieves some object from the server.  The retrieved object is wrapped up in the
 *     {@link GetObjectResult} which is returned.
 * </p>
 */
public interface GetObjectAction<T> extends Action<GetObjectResult<T>> {

}
