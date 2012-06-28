package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.SharingSettingsService;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectSharingSettings;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/06/2012
 */
public class SharingSettingsManager implements SharingSettingsService {


    private static final SharingSettingsManager instance = new SharingSettingsManager();

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    
    private Lock readLock = readWriteLock.readLock();
    
    private Lock writeLock = readWriteLock.writeLock();

    private SharingSettingsServiceImplP3Delegate delegate = new SharingSettingsServiceImplP3Delegate();


    private SharingSettingsManager() {

    }

    public static SharingSettingsManager getManager() {
        return instance;
    }

    public ProjectSharingSettings getProjectSharingSettings(ProjectId projectId) {
        try {
            readLock.lock();
            return delegate.getProjectSharingSettings(projectId);
        }
        finally {
            readLock.unlock();
        }
    }

    public void updateSharingSettings(ProjectSharingSettings projectSharingSettings) {
        try {
            writeLock.lock();
            delegate.updateSharingSettings(projectSharingSettings);
        }
        finally {
            writeLock.unlock();
        }
    }
}
