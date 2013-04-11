package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.NewProjectSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectAlreadyExistsException;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectDocumentNotFoundException;
import edu.stanford.smi.protege.util.Log;
import org.semanticweb.owlapi.io.OWLParserException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/03/2012
 */
public class OWLAPIProjectCache {



    // No need for this to be a concurrent hash map.  All access is controlled by this class.
    private Map<ProjectId, OWLAPIProject> projectId2ProjectMap = new HashMap<ProjectId, OWLAPIProject>();

    private ReadWriteLock lastAccessReadWriteLock = new ReentrantReadWriteLock();

    private Map<ProjectId, Long> lastAccessMap = new HashMap<ProjectId, Long>();


    /**
     * The period between purge checks (in ms).  Every 30 seconds.
     */
    public static final int PURGE_CHECK_PERIOD_MS = 30 * 1000;

    /**
     *  Ellapsed time from the last access after which a project should be considered dormant (and should therefore
     *  be purged).  This can interact with the frequency with which clients poll the project event queue (which is
     *  be default every 10 seconds).
     */
    private static final long DORMANT_PROJECT_TIME_MS = 1 * 60 * 1000;
    
    public OWLAPIProjectCache() {
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                purgeDormantProjects();
            }
        }, 0, PURGE_CHECK_PERIOD_MS);
    }
    
    
    private synchronized void purgeDormantProjects() {
        for(ProjectId projectId : new ArrayList<ProjectId>(lastAccessMap.keySet())) {
            long time = getLastAccessTime(projectId);
            long lastAccessTimeDiff = System.currentTimeMillis() - time;
            if(time == 0 || lastAccessTimeDiff > DORMANT_PROJECT_TIME_MS) {
                purge(projectId);
            }
        }
    }

    public synchronized OWLAPIProject getProject(ProjectId projectId) throws ProjectDocumentNotFoundException {
        try {
            OWLAPIProjectDocumentStore documentStore = OWLAPIProjectDocumentStore.getProjectDocumentStore(projectId);

            OWLAPIProject project = projectId2ProjectMap.get(projectId);
            if (project == null) {
                project = OWLAPIProject.getProject(documentStore);
                projectId2ProjectMap.put(projectId, project);
                Log.getLogger().log(Level.INFO, "Loaded project: " + projectId);
            }
            logProjectAccess(projectId);
            return project;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (OWLParserException e) {
            throw new RuntimeException(e);
        }
    }
    
    public synchronized OWLAPIProject getProject(NewProjectSettings newProjectSettings) throws ProjectAlreadyExistsException {
        try {
            OWLAPIProjectDocumentStore documentStore = OWLAPIProjectDocumentStore.createNewProject(newProjectSettings);
            OWLAPIProject project = OWLAPIProject.getProject(documentStore);
            projectId2ProjectMap.put(project.getProjectId(), project);
            logProjectAccess(project.getProjectId());
            return project;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (OWLParserException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void purge(ProjectId projectId) {
        OWLAPIProject project = projectId2ProjectMap.remove(projectId);
        lastAccessMap.remove(projectId);
        project.dispose();
        Log.getLogger().log(Level.INFO, "Purged project: " + projectId);
    }

    /**
     * Gets the time of last cache access for a given project.
     * @param projectId The project id.
     * @return The time stamp of the last access of the specified project from the cache.  This time stamp will be 0
     * if the project does not exist.
     */
    public long getLastAccessTime(ProjectId projectId) {
        Long timestamp = null;
        try {
            lastAccessReadWriteLock.readLock().lock();
            timestamp = lastAccessMap.get(projectId);
        }
        finally {
            lastAccessReadWriteLock.readLock().unlock();
        }
        if(timestamp == null) {
            return 0;
        }
        else {
            return timestamp;
        }
    }

    protected void logProjectAccess(final ProjectId projectId) {
        try {
            lastAccessReadWriteLock.writeLock().lock();
            long currentTime = System.currentTimeMillis();
            lastAccessMap.put(projectId, currentTime);
        }
        finally {
            lastAccessReadWriteLock.writeLock().unlock();
        }
    }
}
