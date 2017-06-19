package edu.stanford.bmir.protege.web.shared.dispatch;

import com.google.gwt.user.client.rpc.InvocationException;

import java.util.Optional;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 18/07/2013
 * <p>
 *     A marker interface for actions which can tolerate {@link InvocationException}
 *     exceptions (these exceptions arise when communication with the server is lost).
 * </p>
 */
public interface InvocationExceptionTolerantAction {

    /**
     * Called by the dispatch framework when this action could not be handled due to an {@link InvocationException}.
     * @param ex The exception.  Not {@code null}.
     * @return An optional string that represents an end user error facing message.  If the String value is present
     * then it will be retrieved and displayed to the user.  Not {@code null}.
     */
    Optional<String> handleInvocationException(InvocationException ex);
}
