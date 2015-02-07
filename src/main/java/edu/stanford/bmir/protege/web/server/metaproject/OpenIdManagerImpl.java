package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.ui.openid.constants.OpenIdConstants;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.PropertyValue;
import edu.stanford.smi.protege.server.metaproject.User;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/02/15
 */
public class OpenIdManagerImpl implements OpenIdManager {

    private MetaProject metaProject;


    @Inject
    public OpenIdManagerImpl(MetaProject metaProject) {
        this.metaProject = metaProject;
    }

    @Override
    public Optional<UserId> getUserAssociatedWithOpenId(String userOpenId) {
        if (userOpenId == null) {
            return Optional.absent();
        }
        Set<User> users = metaProject.getUsers();
        for (User u : users) {
            Collection<PropertyValue> propertyValues = u.getPropertyValues();
            for (PropertyValue propertyValue : propertyValues) {
                if (propertyValue.getPropertyName().startsWith(OpenIdConstants.OPENID_PROPERTY_PREFIX) && propertyValue.getPropertyName().endsWith(OpenIdConstants.OPENID_PROPERTY_URL_SUFFIX)) {
                    if (propertyValue.getPropertyValue().trim().equalsIgnoreCase(userOpenId)) {
                        return Optional.of(UserId.getUserId(u.getName()));
                    }

                }
            }
        }
        OWLAPIMetaProjectStore.getStore().saveMetaProject(metaProject);
        return Optional.absent();
    }
}
