package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.event.UserStartingViewingProjectEvent;
import edu.stanford.bmir.protege.web.shared.event.UserStoppedViewingProjectEvent;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/03/2013
 */
public class ProjectAccessManager implements HasDispose {

    public static final int PURGE_PERIOD = 60000;

    private OWLAPIProject project;

    private Map<UserId, Long> userIdAccessTimeMap = new HashMap<UserId, Long>();

    private Timer purgeTimer = new Timer(true);


    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private Lock readLock = readWriteLock.readLock();

    private Lock writeLock = readWriteLock.writeLock();


    public ProjectAccessManager(OWLAPIProject project) {
        this.project = project;
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
                // First viewing
                if(!userId.isGuest()) {
                    project.getEventManager().postEvent(new UserStartingViewingProjectEvent(project.getProjectId(), userId));
                }
            }
        }
        finally {
            writeLock.unlock();
        }
    }


    private void purgeUsers() {
        try {
            final long currentTime = System.currentTimeMillis();
            writeLock.lock();
            for(UserId userId : new ArrayList<UserId>(userIdAccessTimeMap.keySet())) {
                Long timeStamp = userIdAccessTimeMap.get(userId);
                if(currentTime - timeStamp > PURGE_PERIOD) {
                    userIdAccessTimeMap.remove(userId);
                    project.getEventManager().postEvent(new UserStoppedViewingProjectEvent(project.getProjectId(), userId));
                }
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
