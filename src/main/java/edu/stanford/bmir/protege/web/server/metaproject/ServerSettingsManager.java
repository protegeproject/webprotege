package edu.stanford.bmir.protege.web.server.metaproject;

import edu.stanford.smi.protege.server.metaproject.Operation;

import java.util.Collection;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public interface ServerSettingsManager {
    Collection<Operation> getAllowedServerOperations(String userName);

    boolean allowsCreateUser();
}
