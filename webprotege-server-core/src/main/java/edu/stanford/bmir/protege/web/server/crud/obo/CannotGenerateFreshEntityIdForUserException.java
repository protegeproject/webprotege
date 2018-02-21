package edu.stanford.bmir.protege.web.server.crud.obo;

import edu.stanford.bmir.protege.web.server.crud.CannotGenerateFreshEntityIdException;
import edu.stanford.bmir.protege.web.shared.crud.oboid.UserIdRange;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 10/09/2014
 *
 * Thrown when an id cannot be generated for a particular user.  This happens when the user has used up their
 * allotted range of ids.
 */
public class CannotGenerateFreshEntityIdForUserException extends CannotGenerateFreshEntityIdException {

    private final UserIdRange userIdRange;

    public CannotGenerateFreshEntityIdForUserException(UserIdRange userIdRange) {
        super("Cannot generate fresh entity id for user "
                      + userIdRange.getUserId().getUserName()
                      + ".  Allocated id range (" + userIdRange.getStart() + " - " + userIdRange.getEnd() + ") has been exhausted.");
        this.userIdRange = checkNotNull(userIdRange);
    }

    /**
     * Gets the range for the user for whom an id cannot be allocated
     * @return The UserIdRange.  Not {@code null}.
     */
    public UserIdRange getUserIdRange() {
        return userIdRange;
    }
}
