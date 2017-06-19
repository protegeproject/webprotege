package edu.stanford.bmir.protege.web.server.project;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import edu.stanford.bmir.protege.web.client.project.NewProjectSettings;
import edu.stanford.bmir.protege.web.server.inject.ApplicationComponent;
import edu.stanford.bmir.protege.web.server.inject.project.ProjectComponent;
import edu.stanford.bmir.protege.web.server.inject.project.ProjectModule;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.shared.project.ProjectAlreadyExistsException;
import edu.stanford.bmir.protege.web.shared.project.ProjectDocumentNotFoundException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/03/2012
 */
public class ProjectCache {

    private final Interner<ProjectId> projectIdInterner;


    private final ReadWriteLock projectMapReadWriteLoc = new ReentrantReadWriteLock();

    private final Lock READ_LOCK = projectMapReadWriteLoc.readLock();

    private final Lock WRITE_LOCK = projectMapReadWriteLoc.writeLock();

    private Map<ProjectId, ProjectComponent> projectId2ProjectComponent = new ConcurrentHashMap<>();

    private final ReadWriteLock LAST_ACCESS_LOCK = new ReentrantReadWriteLock();

    private final ReadWriteLock PROJECT_ID_LOCK = new ReentrantReadWriteLock();

    private Map<ProjectId, Long> lastAccessMap = new HashMap<>();

    private final ProjectImporterFactory projectImporterFactory;



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

    private final ApplicationComponent applicationComponent;

    @Inject
    public ProjectCache(@Nonnull ApplicationComponent applicationComponent,
                        @Nonnull ProjectImporterFactory projectImporterFactory,
                        @Nonnull WebProtegeLogger logger) {
        this.applicationComponent = checkNotNull(applicationComponent);
        this.logger = checkNotNull(logger);
        this.projectImporterFactory = checkNotNull(projectImporterFactory);
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
            return new ArrayList<>(lastAccessMap.keySet());
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




    public Project getProject(ProjectId projectId) throws ProjectDocumentNotFoundException {
        return getProjectInternal(projectId, AccessMode.NORMAL);
    }

    public Optional<Project> getProjectIfActive(ProjectId projectId) {
        try {
            READ_LOCK.lock();
            if(!isActive(projectId)) {
                return Optional.empty();
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

    private Project getProjectInternal(ProjectId projectId, AccessMode accessMode) {
        // Per project lock
        synchronized (getInternedProjectId(projectId)) {
            try {
                ProjectComponent projectComponent = getProjectInjector(projectId);
                if (accessMode == AccessMode.NORMAL) {
                    logProjectAccess(projectId);
                }
                return projectComponent.getProject();
            }
            catch (OWLParserException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ProjectComponent getProjectInjector(ProjectId projectId) {
        ProjectComponent projectComponent = projectId2ProjectComponent.get(projectId);
        if (projectComponent == null) {
            logger.info("Request for unloaded project %s.", projectId.getId());
            projectComponent = applicationComponent.getProjectComponent(new ProjectModule(projectId));
            projectId2ProjectComponent.put(projectId, projectComponent);
        }
        return projectComponent;
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

    public Project getProject(NewProjectSettings newProjectSettings) throws ProjectAlreadyExistsException, OWLOntologyCreationException, OWLOntologyStorageException, IOException {
        ProjectId projectId = ProjectIdFactory.getFreshProjectId();
        if(newProjectSettings.hasSourceDocument()) {
            ProjectImporter importer = projectImporterFactory.getProjectImporter(projectId);
            importer.createProjectFromSources(newProjectSettings.getSourceDocumentId(), newProjectSettings.getProjectOwner());
        }
        return getProjectInternal(projectId, AccessMode.NORMAL);
    }

    public void purge(ProjectId projectId) {
        try {
            WRITE_LOCK.lock();
            LAST_ACCESS_LOCK.writeLock().lock();
            ProjectComponent projectComponent = projectId2ProjectComponent.remove(projectId);
            lastAccessMap.remove(projectId);
            Project project  = projectComponent.getProject();
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
    private long getLastAccessTime(ProjectId projectId) {
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
}
