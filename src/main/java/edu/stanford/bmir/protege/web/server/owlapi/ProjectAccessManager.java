package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.events.HasPostEvents;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.event.UserStartingViewingProjectEvent;
import edu.stanford.bmir.protege.web.shared.event.UserStoppedViewingProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/03/2013
 */
public class ProjectAccessManager implements HasDispose {

    public static final int PURGE_PERIOD = 60000;

    private ProjectId projectId;

    private Map<UserId, Long> userIdAccessTimeMap = new HashMap<UserId, Long>();

    private Timer purgeTimer = new Timer(true);


    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private Lock readLock = readWriteLock.readLock();

    private Lock writeLock = readWriteLock.writeLock();

    private HasPostEvents<ProjectEvent<?>> postEvents;

    private final WebProtegeLogger logger;

    @Inject
    public ProjectAccessManager(ProjectId projectId, HasPostEvents<ProjectEvent<?>> postEvents) {
        this.projectId = checkNotNull(projectId);
        this.postEvents = checkNotNull(postEvents);
        this.logger = WebProtegeInjector.get().getInstance(WebProtegeLogger.class);
        purgeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                purgeUsers();
            }
        }, PURGE_PERIOD, PURGE_PERIOD);
    }

    public void logAccessForUser(UserId userId) {
        try {
            writeLock.lock();
            Long previousTime = userIdAccessTimeMap.put(userId, System.currentTimeMillis());
            if(previousTime == null) {
                // First viewing for this user
                if(!userId.isGuest()) {
                    postEvents.postEvent(new UserStartingViewingProjectEvent(projectId, userId));
                }
                logStats();
            }
        }
        finally {
            writeLock.unlock();
        }
    }

    private void logStats() {
        logger.info("There are %d users now viewing %s", userIdAccessTimeMap.size(), projectId);
    }


    private void purgeUsers() {
        try {
            final long currentTime = System.currentTimeMillis();
            writeLock.lock();
            int currentUserCount = userIdAccessTimeMap.size();
            for(UserId userId : new ArrayList<>(userIdAccessTimeMap.keySet())) {
                Long timeStamp = userIdAccessTimeMap.get(userId);
                if(currentTime - timeStamp > PURGE_PERIOD) {
                    userIdAccessTimeMap.remove(userId);
                    postEvents.postEvent(new UserStoppedViewingProjectEvent(projectId, userId));
                }
            }
            if(userIdAccessTimeMap.size() != currentUserCount) {
                logStats();
            }
        }
        finally {
            writeLock.unlock();
        }

    }

    @Override
    public void dispose() {
        purgeTimer.cancel();
    }
}
