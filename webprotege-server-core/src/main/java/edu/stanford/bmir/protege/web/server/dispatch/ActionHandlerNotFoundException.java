package edu.stanford.bmir.protege.web.server.dispatch;

import edu.stanford.bmir.protege.web.shared.dispatch.Action;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/02/2013
 * <p>
 *     Describes the case where an {@link ActionHandler} could not be found to execute a given action.
 * </p>
 */
public class ActionHandlerNotFoundException extends RuntimeException {

    private Action<?> action;

    /**
     * Constructs an ActionHandlerNotFoundException for the specified action.
     * @param action The action for which a handler could not be found.  Not {@code null}.
     * @throws NullPointerException if {@code action} is {@code null}.
     */
    public ActionHandlerNotFoundException(Action<?> action) {
        this.action = checkNotNull(action);
    }

    public Action<?> getAction() {
        return action;
    }

    /**
     * Returns the detail message string of this throwable.
     * @return the detail message string of this <tt>Throwable</tt> instance
     *         (which may be <tt>null</tt>).
     */
    @Override
    public String getMessage() {
        return "Action handler not found: " + action.getClass().getName();
    }
}
