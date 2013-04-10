package edu.stanford.bmir.protege.web.shared.dispatch;

import edu.stanford.bmir.protege.web.shared.HasOperationFailedMessage;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/01/2013
 * <p>
 *     The basic interface for actions that are sent to the dispatch service
 * </p>
 */
public interface Action<R extends Result> extends Serializable {
}
