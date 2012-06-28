package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.MetaProjectManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/06/2012
 */
public class OWLAPIMetaProjectStore {

    private static OWLAPIMetaProjectStore instance = new OWLAPIMetaProjectStore();

    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    
    private static Lock readLock = readWriteLock.readLock();
    
    private static Lock writeLock = readWriteLock.writeLock();

    private OWLAPIMetaProjectStore() {
    }

    public static OWLAPIMetaProjectStore getStore() {
        return instance;
    }

    public void saveMetaProject(MetaProjectManager metaProjectManager) {
        try {
            writeLock.lock();
            List<?> errors = new ArrayList<Object>();
            metaProjectManager.getMetaProject().save(errors);
            if(!errors.isEmpty()) {
                throw new RuntimeException("Problem saving meta project: " + errors.toString());
            }
        }
        finally {
            writeLock.unlock();
        }
    }
}
