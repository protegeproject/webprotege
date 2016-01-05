package edu.stanford.bmir.protege.web.client.permissions;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/12/15
 */
public interface LoggedInUserProjectPermissionChecker {

    boolean hasWritePermission();

    boolean hasReadPermission();
}
