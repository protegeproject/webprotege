package edu.stanford.bmir.protege.web.server.project;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import edu.stanford.bmir.protege.web.server.dispatch.impl.ProjectActionHandlerRegistry;
import edu.stanford.bmir.protege.web.server.events.EventManager;
import edu.stanford.bmir.protege.web.server.inject.ProjectComponent;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.csv.DocumentId;
import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import edu.stanford.bmir.protege.web.shared.project.NewProjectSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectAlreadyExistsException;
import edu.stanford.bmir.protege.web.shared.project.ProjectDocumentNotFoundException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
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
@ApplicationSingleton
public class ProjectCache implements HasDispose {

    private static final Logger logger = LoggerFactory.getLogger(ProjectCache.class);

    private final Interner<ProjectId> projectIdInterner;

    private final ReadWriteLock projectMapReadWriteLoc = new ReentrantReadWriteLock();

    private final Lock readLock = projectMapReadWriteLoc.readLock();

    private final Lock writeLock = projectMapReadWriteLoc.writeLock();

    private final Map<ProjectId, ProjectComponent> projectId2ProjectComponent = new ConcurrentHashMap<>();

    private final ReadWriteLock lastAccessLock = new ReentrantReadWriteLock();

    private final Map<ProjectId, Long> lastAccessMap = new HashMap<>();

    private final ProjectImporterFactory projectImporterFactory;

    /**
     * Elapsed time from the last access after which a project should be considered dormant (and should therefore
     * be purged).  This can interact with the frequency with which clients poll the project event queue (which is
     * be default every 10 seconds).
     */
    private final long dormantProjectTime;

    private final ProjectComponentFactory projectComponentFactory;

    @Inject
    public ProjectCache(@Nonnull ProjectComponentFactory projectComponentFactory,
                        @Nonnull ProjectImporterFactory projectImporterFactory,
                        @DormantProjectTime  long dormantProjectTime) {
        this.projectComponentFactory = checkNotNull(projectComponentFactory);
        this.projectImporterFactory = checkNotNull(projectImporterFactory);
        projectIdInterner = Interners.newWeakInterner();
        this.dormantProjectTime = dormantProjectTime;
        logger.info("Dormant project time: {} milliseconds", dormantProjectTime);
    }

    public ProjectActionHandlerRegistry getActionHandlerRegistry(ProjectId projectId) {
        return getProjectInternal(projectId, AccessMode.NORMAL, InstantiationMode.EAGER).getActionHandlerRegistry();
    }


    /**
     * Gets the list of cached project ids.
     * @return A list of cached project ids.
     */
    private List<ProjectId> getCachedProjectIds() {
        try {

            readLock.lock();
            return new ArrayList<>(lastAccessMap.keySet());
        }
        finally {
            readLock.unlock();
        }
    }

    /**
     * Purges projects that have not been access for some given period of time
     */
    public void purgeDormantProjects() {
        // No locking needed
        for (ProjectId projectId : getCachedProjectIds()) {
            long time = getLastAccessTime(projectId);
            long lastAccessTimeDiff = System.currentTimeMillis() - time;
            if (time == 0 || lastAccessTimeDiff > dormantProjectTime) {
                purge(projectId);
            }
        }
    }

    public void purgeAllProjects() {
        logger.info("Purging all loaded projects");
        for (ProjectId projectId : getCachedProjectIds()) {
            purge(projectId);
        }
    }

    public void ensureProjectIsLoaded(ProjectId projectId) throws ProjectDocumentNotFoundException {
        var projectComponent = getProjectInternal(projectId, AccessMode.NORMAL, InstantiationMode.EAGER);
        logger.info("Loaded {}", projectComponent.getProjectId());
    }

    public RevisionManager getRevisionManager(ProjectId projectId) {
        return getProjectInternal(projectId, AccessMode.NORMAL, InstantiationMode.LAZY).getRevisionManager();
    }

    @Nonnull
    public Optional<EventManager<ProjectEvent<?>>> getProjectEventManagerIfActive(@Nonnull ProjectId projectId) {
        try {
            readLock.lock();
            boolean active = isActive(projectId);
            if(!active) {
                return Optional.empty();
            }
            else {
                return Optional.of(getProjectInternal(projectId, AccessMode.QUIET, InstantiationMode.LAZY))
                        .map(ProjectComponent::getEventManager);
            }
        }
        finally {
            readLock.unlock();
        }
    }

    private enum AccessMode {
        NORMAL,
        QUIET
    }

    private ProjectComponent getProjectInternal(ProjectId projectId, AccessMode accessMode, InstantiationMode instantiationMode) {
        // Per project lock
        synchronized (getInternedProjectId(projectId)) {
            try {
                ProjectComponent projectComponent = getProjectInjector(projectId, instantiationMode);
                if (accessMode == AccessMode.NORMAL) {
                    logProjectAccess(projectId);
                }
                return projectComponent;
            }
            catch (OWLParserException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ProjectComponent getProjectInjector(ProjectId projectId, InstantiationMode instantiationMode) {
        ProjectComponent projectComponent = projectId2ProjectComponent.get(projectId);
        if (projectComponent == null) {
            logger.info("Request for unloaded project {}.", projectId.getId());
            Stopwatch stopwatch = Stopwatch.createStarted();
            projectComponent = projectComponentFactory.createProjectComponent(projectId);
            if(instantiationMode == InstantiationMode.EAGER) {
                // Force instantiation of certain objects in the project graph.
                // This needs to be done in a nicer way, but this approach works for now.
                projectComponent.init();
            }
            stopwatch.stop();
            logger.info("{} Instantiated project component in {} ms",
                        projectId,
                        stopwatch.elapsed(TimeUnit.MILLISECONDS));
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

    public ProjectId getProject(NewProjectSettings newProjectSettings) throws ProjectAlreadyExistsException, OWLOntologyCreationException, IOException {
        ProjectId projectId = ProjectIdFactory.getFreshProjectId();
        Optional<DocumentId> sourceDocumentId = newProjectSettings.getSourceDocumentId();
        if(sourceDocumentId.isPresent()) {
            ProjectImporter importer = projectImporterFactory.create(projectId);
            importer.createProjectFromSources(sourceDocumentId.get(), newProjectSettings.getProjectOwner());
        }
        return getProjectInternal(projectId, AccessMode.NORMAL, InstantiationMode.EAGER).getProjectId();
    }

    public void purge(ProjectId projectId) {
        try {
            writeLock.lock();
            lastAccessLock.writeLock().lock();
            var projectComponent = projectId2ProjectComponent.remove(projectId);
            if(projectComponent != null) {
                var projectDisposableObjectManager = projectComponent.getDisposablesManager();
                projectDisposableObjectManager.dispose();
            }
            lastAccessMap.remove(projectId);
        }
        finally {
            final int projectsBeingAccessed = lastAccessMap.size();
            lastAccessLock.writeLock().unlock();
            writeLock.unlock();
            logger.info("Purged project: {}.  {} projects are now being accessed.", projectId.getId(), projectsBeingAccessed);
        }
    }

    public boolean isActive(ProjectId projectId) {
        try {
            readLock.lock();
            return projectId2ProjectComponent.containsKey(projectId) && lastAccessMap.containsKey(projectId);
        }
        finally {
            readLock.unlock();
        }
    }

    /**
     * Gets the time of last cache access for a given project.
     * @param projectId The project id.
     * @return The time stamp of the last access of the specified project from the cache.  This time stamp will be 0
     *         if the project does not exist.
     */
    private long getLastAccessTime(ProjectId projectId) {
        Long timestamp;
        try {
            lastAccessLock.readLock().lock();
            timestamp = lastAccessMap.get(projectId);
        }
        finally {
            lastAccessLock.readLock().unlock();
        }
        return Objects.requireNonNullElse(timestamp, 0L);
    }

    private void logProjectAccess(final ProjectId projectId) {
        try {
            lastAccessLock.writeLock().lock();
            long currentTime = System.currentTimeMillis();
            int currentSize = lastAccessMap.size();
            lastAccessMap.put(projectId, currentTime);
            if(lastAccessMap.size() > currentSize) {
                logger.info("{} projects are now being accessed", lastAccessMap.size());
            }
        }
        finally {
            lastAccessLock.writeLock().unlock();
        }
    }

    @Override
    public void dispose() {
        purgeAllProjects();
    }
}
