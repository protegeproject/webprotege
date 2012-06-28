package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.model.event.OntologyEvent;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.util.Log;

import java.util.List;
import java.util.logging.Level;

/*
 * Synchronization Issues:
 *     Callers who retrieve a ServerProject object may get one that is not already initialized.  When the 
 *     ServerProject object is initialized both the project (setProject) and the name (setProjectName) must be
 *     set with the ServerProject lock held.
 */

public class ServerProject<P> {

	private P project;
	private String projectName;
	private ServerEventManager eventManager;

	/*
	 * This call is synchronized so that its implementation will run either before or 
	 * after the synchronized block that initializes this server project.
	 */
	public synchronized boolean isLoaded() { //TODO: check if you need both conditions
    	return project != null && eventManager != null && projectName != null;
    }

    synchronized void setProject(P project) {
		this.project = project;
		eventManager = new ServerEventManager(this);
	}

	public void setProjectName(String name) {
        this.projectName = name;
    }

    public P getProject() {
    	return project;
    }

    public String getProjectName() {
        return projectName;
    }

    public int getServerVersion(){
    	return eventManager.getServerRevision();
    }

    public List<OntologyEvent> getEvents(long fromVersion) {
		return eventManager.getEvents(fromVersion);
	}

	public void dispose() {
		eventManager.dispose();
		try {
            if (Project.class.isAssignableFrom(project.getClass())){
                ((Project)project).dispose();
            }
		} catch (Exception e) {
			Log.getLogger().log(Level.WARNING, "Errors at disposing remote project", e);
		}
	}

}
