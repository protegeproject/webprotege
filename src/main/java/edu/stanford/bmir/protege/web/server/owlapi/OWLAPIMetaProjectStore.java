package edu.stanford.bmir.protege.web.server.owlapi;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.smi.protege.server.metaproject.MetaProject;

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

    private final WebProtegeLogger logger;

    public static final int SAVE_META_PROJECT_DELAY = 60000;

    private static OWLAPIMetaProjectStore instance = new OWLAPIMetaProjectStore();

    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    
    private static Lock readLock = readWriteLock.readLock();
    
    private static Lock writeLock = readWriteLock.writeLock();

    private TimerTask saveTask;

    private final Timer saveTimer = new Timer();

    private OWLAPIMetaProjectStore() {
        logger = WebProtegeInjector.get().getInstance(WebProtegeLogger.class);
    }

    public static OWLAPIMetaProjectStore getStore() {
        return instance;
    }

    public void saveMetaProjectNow(MetaProject metaProject) {
        try {
            writeLock.lock();
            logger.info("Saving meta-project now.");
            final Stopwatch stopwatch = Stopwatch.createStarted();
            List<?> errors = new ArrayList<>();
            metaProject.save(errors);
            if(!errors.isEmpty()) {
                throw new RuntimeException("Failed to save meta-project: " + errors.toString());
            }
            stopwatch.stop();
            logger.info("Saved meta-project in %d ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        }
        finally {
            writeLock.unlock();
        }
    }

    public void saveMetaProject(final MetaProject metaProject) {
        writeLock.lock();
        logger.info("Request to save meta-project received. Scheduling save task.");
        if (saveTask != null) {
            saveTask.cancel();
        }
        saveTask = new SaveMetaProjectTask(metaProject);
        saveTimer.schedule(saveTask, SAVE_META_PROJECT_DELAY);
        writeLock.unlock();
    }




    private class SaveMetaProjectTask extends TimerTask {

        private final MetaProject metaProject;

        private SaveMetaProjectTask(MetaProject mpm) {
            this.metaProject = mpm;
        }

        @Override
        public void run() {
            saveMetaProjectNow(metaProject);
        }
    }
}
