package edu.stanford.bmir.protege.web.server.user;

import org.mongodb.morphia.Datastore;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Mar 2017
 */
public class UserActivityManagerProvider implements Provider<UserActivityManager> {

    private Datastore datastore;

    @Inject
    public UserActivityManagerProvider(Datastore datastore) {
        this.datastore = datastore;
    }

    @Override
    public UserActivityManager get() {
        UserActivityManager userActivityManager = new UserActivityManager(datastore);
        userActivityManager.ensureIndexes();
        return userActivityManager;
    }
}
