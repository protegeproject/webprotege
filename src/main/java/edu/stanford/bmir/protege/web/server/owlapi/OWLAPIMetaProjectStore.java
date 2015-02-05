package edu.stanford.bmir.protege.web.server.owlapi;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.server.metaproject.MetaProjectManager;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
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

    private static final WebProtegeLogger logger = WebProtegeLoggerManager.get(OWLAPIMetaProjectStore.class);

    public static final int SAVE_META_PROJECT_DELAY = 60000;

    private static OWLAPIMetaProjectStore instance = new OWLAPIMetaProjectStore();

    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    
    private static Lock readLock = readWriteLock.readLock();
    
    private static Lock writeLock = readWriteLock.writeLock();

    private TimerTask saveTask;

    private final Timer saveTimer = new Timer();

    private OWLAPIMetaProjectStore() {
    }

    public static OWLAPIMetaProjectStore getStore() {
        return instance;
    }

    public void saveMetaProjectNow(MetaProjectManager metaProjectManager) {
        logger.info("Saving meta-project now.");
        final Stopwatch stopwatch = Stopwatch.createStarted();
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
            stopwatch.stop();
            logger.info("Saved meta-project in %d ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    public void saveMetaProject(final MetaProjectManager metaProjectManager) {
        logger.info("Request to save meta-project received. Scheduling save task.");
        writeLock.lock();
        if (saveTask != null) {
            saveTask.cancel();
        }
        saveTask = new SaveMetaProjectTask(metaProjectManager);
        saveTimer.schedule(saveTask, SAVE_META_PROJECT_DELAY);
        writeLock.unlock();
    }




    private class SaveMetaProjectTask extends TimerTask {

        private MetaProjectManager mpm;

        private SaveMetaProjectTask(MetaProjectManager mpm) {
            this.mpm = mpm;
        }

        @Override
        public void run() {
            saveMetaProjectNow(mpm);
        }
    }
}
