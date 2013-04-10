package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.server.SharingSettingsManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/10/2012
 */
public class EntityIdGeneratorSettingsManager {
    
    private static final ReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();
    
    private static final Lock READ_LOCK = READ_WRITE_LOCK.readLock();

    private static final Lock WRITE_LOCK = READ_WRITE_LOCK.writeLock();

    public static final String DEFAULT_BASE_PREFIX = "http://webprotege.stanford.edu/ontologies/";

    public static final int DEFAULT_ID_LENGTH = 7;

    private Map<ProjectId, EntityIdGeneratorSettings> settingsMap = new HashMap<ProjectId, EntityIdGeneratorSettings>();
    
    private static final EntityIdGeneratorSettingsManager instance = new EntityIdGeneratorSettingsManager();

    private EntityIdGeneratorSettingsManager() {
    }
    
    
    public static EntityIdGeneratorSettingsManager getManager() {
        return instance;
    }
    
    public EntityIdGeneratorSettings getSettingsForProject(ProjectId projectId) {
        if(projectId == null) {
            throw new NullPointerException("projectId must not be null");
        }
        try {
            READ_LOCK.lock();
            EntityIdGeneratorSettings settings = settingsMap.get(projectId);
            if(settings == null) {
                settings = createFreshSettings(projectId);
            }
            return settings;
        }
        finally {
            READ_LOCK.unlock();
        }
    }

    /**
     * Creates a fresh settings object, whose values are based on the specified ProjectId and the sharing settings
     * for the project.  Each project writer will have a blank range in the settings object.
     * @param projectId The projectId that identifies the project for which the settings are to be created.
     * @return A fresh EntityIdGeneratorSettings object for the project.
     */
    private EntityIdGeneratorSettings createFreshSettings(ProjectId projectId) {
        OWLAPIProjectManager pm = OWLAPIProjectManager.getProjectManager();
        OWLAPIProject project = pm.getProject(projectId);
        OWLOntology rootOntology = project.getRootOntology();
        OWLOntologyManager man = rootOntology.getOWLOntologyManager();
        OWLOntologyFormat format = man.getOntologyFormat(rootOntology);
        String base = getDefaultBase(projectId);
        if(format.isPrefixOWLOntologyFormat()) {
            String defaultPrefix = format.asPrefixOWLOntologyFormat().getDefaultPrefix();
            if(defaultPrefix != null) {
                base = defaultPrefix;
            }
        }
        EntityIdGeneratorSettings settings = new EntityIdGeneratorSettings(base, projectId.getSuggestedAcronym(), DEFAULT_ID_LENGTH, Collections.<EntityIdUserRange>emptyList());
        settings = ensureIdRangesForProjectWriters(projectId, settings);
        return settings;
    }

    private String getDefaultBase(ProjectId projectId) {
        return DEFAULT_BASE_PREFIX + projectId.getSuggestedURLPathElementName();
    }

    /**
     * Ensures that each UserId which is a project writer for the specified project has a range setting.
     * @param projectId
     * @param settings
     * @return
     */
    private EntityIdGeneratorSettings ensureIdRangesForProjectWriters(ProjectId projectId, EntityIdGeneratorSettings settings) {
        SharingSettingsManager sharingSettingsManager = SharingSettingsManager.getManager();
        ProjectSharingSettings sharingSettings = sharingSettingsManager.getProjectSharingSettings(projectId);

        for(UserSharingSetting sharingSetting : sharingSettings.getSharingSettings()) {
            if(sharingSetting.getSharingSetting() == SharingSetting.EDIT) {
                UserId userId = sharingSetting.getUserId();
                settings = settings.ensureRangeForUser(userId);
            }
        }
        return settings;
    }


    public void setSettingsForProject(EntityIdGeneratorSettings settings, ProjectId projectId) {
        if(settings == null) {
            throw new NullPointerException("settings must not be null");
        }
        if(projectId == null) {
            throw new NullPointerException("projectId must not be null");
        }
        try {
            WRITE_LOCK.lock();
            settingsMap.put(projectId, settings);
        }
        finally {
            WRITE_LOCK.unlock();
        }
    }
}
