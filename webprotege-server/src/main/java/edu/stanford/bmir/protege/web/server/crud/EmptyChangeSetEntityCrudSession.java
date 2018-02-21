package edu.stanford.bmir.protege.web.server.crud;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 15/09/2014
 */
public final class EmptyChangeSetEntityCrudSession implements ChangeSetEntityCrudSession {

    private static final EmptyChangeSetEntityCrudSession SESSION = new EmptyChangeSetEntityCrudSession();

    private EmptyChangeSetEntityCrudSession() {
    }

    /**
     * Gets the empty session.  The session cannot be augmented with any information.  This is for handlers that
     * do not require any session information over the course of change set.
     * @return An immutable ChangeSetEntityCrudSession.
     */
    public static ChangeSetEntityCrudSession get() {
        return SESSION;
    }


}
