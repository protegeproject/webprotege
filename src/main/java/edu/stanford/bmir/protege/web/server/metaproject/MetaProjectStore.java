package edu.stanford.bmir.protege.web.server.metaproject;

import com.google.common.base.Stopwatch;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.smi.protege.server.metaproject.MetaProject;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/06/2012
 */
@Singleton
public class MetaProjectStore {

    private final WebProtegeLogger logger;

    public static final int SAVE_META_PROJECT_DELAY = 60000;

    private static Lock lock = new ReentrantLock();

    private TimerTask saveTask;

    private final Timer saveTimer = new Timer();

    @Inject
    public MetaProjectStore(WebProtegeLogger logger) {
        this.logger = logger;
    }

    /**
     * Causes the meta project to be written to disk immediately.  The calling thread will block until the
     * project has been written to disk.
     * @param metaProject The meta project.  Not {@code null}.
     */
    public void saveMetaProjectNow(MetaProject metaProject) {
        try {
            lock.lock();
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
            lock.unlock();
        }
    }

    /**
     * Requests that the meta project is saved to disk.  The project will be scheduled for being saved.  Any pending
     * save requests will be cancelled.
     * @param metaProject The meta project to be saved.  Not {@code null}.
     */
    public void saveMetaProject(final MetaProject metaProject) {
        try {
            lock.lock();
            logger.info("Request to save meta-project received. Scheduling save task.");
            if (saveTask != null) {
                saveTask.cancel();
            }
            saveTask = new SaveMetaProjectTask(metaProject);
            saveTimer.schedule(saveTask, SAVE_META_PROJECT_DELAY);
        } finally {
            lock.unlock();
        }

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
