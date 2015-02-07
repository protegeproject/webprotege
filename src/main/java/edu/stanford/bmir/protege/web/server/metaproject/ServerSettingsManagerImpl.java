package edu.stanford.bmir.protege.web.server.metaproject;

import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.shared.app.ClientApplicationProperties;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;
import edu.stanford.smi.protege.server.metaproject.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class ServerSettingsManagerImpl implements ServerSettingsManager {

    private MetaProject metaProject;

    @Inject
    public ServerSettingsManagerImpl(MetaProject metaProject) {
        this.metaProject = metaProject;
    }

    @Override
    public Collection<Operation> getAllowedServerOperations(String userName) {
        if (userName == null) {
            return Collections.emptySet();
        }
        User user = metaProject.getUser(userName);
        if(user == null) {
            return Collections.emptySet();
        }
        Policy policy = metaProject.getPolicy();
        ServerInstance firstServerInstance = policy.getFirstServerInstance();
        if (firstServerInstance == null) {
            return Collections.emptySet();
        }
        Collection<Operation> allowedOps = new ArrayList<Operation>();
        for (Operation op : policy.getKnownOperations()) {
            if (policy.isOperationAuthorized(user, op, firstServerInstance)) {
                allowedOps.add(op);
            }
        }
        return allowedOps;
    }


    @Override
    public boolean allowsCreateUser() {
        ClientApplicationProperties clientApplicationProperties = WebProtegeProperties.get().getClientApplicationProperties();
        com.google.common.base.Optional<String> propertyValue = clientApplicationProperties.getPropertyValue(WebProtegePropertyName.USER_ACCOUNT_CREATION_ENABLED);
        return "true".equalsIgnoreCase(propertyValue.or("true"));
    }

}
