package edu.stanford.bmir.protege.web.server.owlapi;

import com.google.common.base.Optional;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.inject.Injector;
import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.server.ProjectIdFactory;
import edu.stanford.bmir.protege.web.server.events.EventTranslatorModule;
import edu.stanford.bmir.protege.web.server.inject.WebProtegeInjector;
import edu.stanford.bmir.protege.web.server.inject.project.ProjectModule;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerEx;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerEx;
import edu.stanford.bmir.protege.web.server.watches.WatchEventManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectAlreadyExistsException;
import edu.stanford.bmir.protege.web.shared.project.ProjectDocumentNotFoundException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/03/2012
 */
public class OWLAPIProjectCache {

    private final Interner<ProjectId> projectIdInterner;


    private final ReadWriteLock projectMapReadWriteLoc = new ReentrantReadWriteLock();

    private final Lock READ_LOCK = projectMapReadWriteLoc.readLock();

    private final Lock WRITE_LOCK = projectMapReadWriteLoc.writeLock();

//    private Map<ProjectId, OWLAPIProject> projectId2ProjectMa = new ConcurrentHashMap<>();

    private Map<ProjectId, Injector> projectId2ProjectInjector = new ConcurrentHashMap<>();


    private final ReadWriteLock LAST_ACCESS_LOCK = new ReentrantReadWriteLock();

    private final ReadWriteLock PROJECT_ID_LOCK = new ReentrantReadWriteLock();

    private Map<ProjectId, Long> lastAccessMap = new HashMap<ProjectId, Long>();



    /**
     * The period between purge checks (in ms).  Every 30 seconds.
     */
    public static final int PURGE_CHECK_PERIOD_MS = 30 * 1000;

    /**
     * Elapsed time from the last access after which a project should be considered dormant (and should therefore
     * be purged).  This can interact with the frequency with which clients poll the project event queue (which is
     * be default every 10 seconds).
     */
    private static final long DORMANT_PROJECT_TIME_MS = 3 * 60 * 1000;

    private final WebProtegeLogger logger;


    @Inject
    public OWLAPIProjectCache(WebProtegeLogger logger) {
        this.logger = logger;
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                purgeDormantProjects();
            }
        }, 0, PURGE_CHECK_PERIOD_MS);
        projectIdInterner = Interners.newWeakInterner();

    }


    /**
     * Gets the list of cached project ids.
     * @return A list of cached project ids.
     */
    private List<ProjectId> getCachedProjectIds() {
        try {

            READ_LOCK.lock();
            return new ArrayList<ProjectId>(lastAccessMap.keySet());
        }
        finally {
            READ_LOCK.unlock();
        }
    }


    private void purgeDormantProjects() {
        // No locking needed
        for (ProjectId projectId : getCachedProjectIds()) {
            long time = getLastAccessTime(projectId);
            long lastAccessTimeDiff = System.currentTimeMillis() - time;
            if (time == 0 || lastAccessTimeDiff > DORMANT_PROJECT_TIME_MS) {
                purge(projectId);
            }
        }
    }




    public OWLAPIProject getProject(ProjectId projectId) throws ProjectDocumentNotFoundException {
        return getProjectInternal(projectId, AccessMode.NORMAL);
    }

    public Optional<OWLAPIProject> getProjectIfActive(ProjectId projectId) {
        try {
            READ_LOCK.lock();
            if(!isActive(projectId)) {
                return Optional.absent();
            }
            else {
                return Optional.of(getProjectInternal(projectId, AccessMode.QUIET));
            }
        }
        finally {
            READ_LOCK.unlock();
        }

    }

    private enum AccessMode {
        NORMAL,
        QUIET
    }

    private OWLAPIProject getProjectInternal(ProjectId projectId, AccessMode accessMode) {
        // Per project lock
        synchronized (getInternedProjectId(projectId)) {
            try {
                Injector projectInjector = getProjectInjector(projectId);
                if (accessMode == AccessMode.NORMAL) {
                    logProjectAccess(projectId);
                }
                return projectInjector.getInstance(OWLAPIProject.class);
            }
            catch (OWLParserException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Injector getProjectInjector(ProjectId projectId) {
        Injector projectInjector = projectId2ProjectInjector.get(projectId);
        if (projectInjector == null) {
            logger.info("Request for unloaded project. Loading %s.", projectId.getId());
            WebProtegeInjector appInjector = WebProtegeInjector.get();
            projectInjector = appInjector.createChildInjector(
                    new ProjectModule(projectId),
                    new EventTranslatorModule());
            //  projectId2ProjectMap.put(projectId, project);
            projectId2ProjectInjector.put(projectId, projectInjector);
            WebProtegeLoggerEx loggerEx = new WebProtegeLoggerEx(logger);
            loggerEx.logMemoryUsage();
        }
        return projectInjector;
    }

    /**
     * Gets an interned {@link ProjectId} that is equal to the specified {@link ProjectId}.
     * @param projectId The project id to intern.
     * @return The interned project Id.  Not {@code null}.
     */
    private ProjectId getInternedProjectId(ProjectId projectId) {
        // The interner is thread safe.
        return projectIdInterner.intern(projectId);
    }

    public OWLAPIProject getProject(NewProjectSettings newProjectSettings) throws ProjectAlreadyExistsException, OWLOntologyCreationException, OWLOntologyStorageException, IOException {
        ProjectId projectId = ProjectIdFactory.getFreshProjectId();
        if(newProjectSettings.hasSourceDocument()) {
            ProjectImporter importer = new ProjectImporter(projectId);
            importer.createProjectFromSources(newProjectSettings.getSourceDocumentId(), newProjectSettings.getProjectOwner());
        }
        return getProjectInternal(projectId, AccessMode.NORMAL);
//        try {
//            OWLAPIProjectDocumentStore documentStore = OWLAPIProjectDocumentStore.createNewProject(newProjectSettings);
//            return getProject(documentStore.getProjectId());
//        }
//        catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public void purge(ProjectId projectId) {
        try {
            WRITE_LOCK.lock();
            LAST_ACCESS_LOCK.writeLock().lock();
            Injector projectInjector = projectId2ProjectInjector.remove(projectId);
            lastAccessMap.remove(projectId);
            OWLAPIProject project  = projectInjector.getInstance(OWLAPIProject.class);
            project.dispose();
        }
        finally {
            final int projectsBeingAccessed = lastAccessMap.size();
            LAST_ACCESS_LOCK.writeLock().unlock();
            WRITE_LOCK.unlock();
            logger.info("Purged project: %s.  %d projects are now being accessed.", projectId.getId(), projectsBeingAccessed);
        }
    }

    public boolean isActive(ProjectId projectId) {
        try {
            READ_LOCK.lock();
            return lastAccessMap.containsKey(projectId);
        }
        finally {
            READ_LOCK.unlock();
        }
    }

    /**
     * Gets the time of last cache access for a given project.
     * @param projectId The project id.
     * @return The time stamp of the last access of the specified project from the cache.  This time stamp will be 0
     *         if the project does not exist.
     */
    public long getLastAccessTime(ProjectId projectId) {
        Long timestamp = null;
        try {
            LAST_ACCESS_LOCK.readLock().lock();
            timestamp = lastAccessMap.get(projectId);
        }
        finally {
            LAST_ACCESS_LOCK.readLock().unlock();
        }
        if (timestamp == null) {
            return 0;
        }
        else {
            return timestamp;
        }
    }

    private void logProjectAccess(final ProjectId projectId) {
        try {
            LAST_ACCESS_LOCK.writeLock().lock();
            long currentTime = System.currentTimeMillis();
            int currentSize = lastAccessMap.size();
            lastAccessMap.put(projectId, currentTime);
            if(lastAccessMap.size() > currentSize) {
                logger.info("%d projects are now being accessed", lastAccessMap.size());
            }
        }
        finally {
            LAST_ACCESS_LOCK.writeLock().unlock();
        }
    }

//    private void logMemoryUsage(ProjectId projectId) {
//        Runtime runtime = Runtime.getRuntime();
//        long totalMemoryBytes = runtime.totalMemory();
//        long freeMemoryBytes = runtime.freeMemory();
//        long freeMemoryMB = toMB(freeMemoryBytes);
//        long usedMemoryBytes = totalMemoryBytes - freeMemoryBytes;
//        long usedMemoryMB = toMB(usedMemoryBytes);
//        long totalMemoryMB = toMB(totalMemoryBytes);
//        double percentageUsed = (100.0 * usedMemoryBytes) / totalMemoryBytes;
//        LOGGER.info("Using %d MB of %d MB (%.2f%%) [%d MB free]", usedMemoryMB, totalMemoryMB, percentageUsed, freeMemoryMB);
//    }
//
//    private long toMB(long bytes) {
//        return bytes / (1024 * 1024);
//    }
}
