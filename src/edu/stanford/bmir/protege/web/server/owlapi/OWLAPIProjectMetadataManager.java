package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.server.MetaProjectManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectDetails;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.UnknownProjectException;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import edu.stanford.smi.protege.server.metaproject.PropertyValue;
import edu.stanford.smi.protege.server.metaproject.User;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/08/2012
 */
public class OWLAPIProjectMetadataManager {

    private static final OWLAPIProjectMetadataManager instance = new OWLAPIProjectMetadataManager();

    private static final String PROJECT_TYPE_PROPERTY_NAME = "projectType";

    private static final String CREATION_TIME_PROPERTY_NAME = "creationTime";

    private static final String LAST_MODIFIED_AT_PROPERTY_NAME = "lastModifiedAt";

    private static final String LAST_MODIFIED_BY_PROPERTY_NAME = "lastModifiedBy";

    private static final String IN_TRASH_SLOT_NAME = "inTrash";

    private static ReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();
    
    private static Lock READ_LOCK = READ_WRITE_LOCK.readLock();
    
    private static Lock WRITE_LOCK = READ_WRITE_LOCK.writeLock();

    public static final String DEFAULT_LANGUAGE_PROPERTY_NAME = "defaultLanguage";

    private OWLAPIProjectMetadataManager() {
    }

    public static OWLAPIProjectMetadataManager getManager() {
        return instance;
    }


    // ProjectData
    
    public synchronized Set<ProjectId> getProjects() {
        try {
            READ_LOCK.lock();
            MetaProject metaProject = getMetaProject();
            Set<ProjectInstance> projectInstances = metaProject.getProjects();
            Set<ProjectId> result = new HashSet<ProjectId>(projectInstances.size());
            for(ProjectInstance pi : projectInstances) {
                String projectName = pi.getName();
                result.add(ProjectId.get(projectName));
            }
            return result;
        }
        finally {
            READ_LOCK.unlock();
        }
    }
    
    private void save() {
        try {
            WRITE_LOCK.lock();
            ArrayList errors = new ArrayList();
            getMetaProject().save(errors);
            for(Object o : errors) {
                System.out.println(o);
            }
        }
        finally {
            WRITE_LOCK.unlock();
        }
    }

    private MetaProject getMetaProject() {
        MetaProjectManager mpm = MetaProjectManager.getManager();
        return mpm.getMetaProject();
    }


    public String getName(ProjectId projectId) {
        return getDisplayName(projectId);
    }

    public OWLAPIProjectType getType(ProjectId projectId) throws UnknownProjectException {
        try {
            READ_LOCK.lock();
            String defaultProjectTypeName = OWLAPIProjectType.getDefaultProjectType().getProjectTypeName();
            String projectType = getPropertyValue(projectId, PROJECT_TYPE_PROPERTY_NAME, defaultProjectTypeName);
            if(projectType.equals(OWLAPIProjectType.getOBOProjectType().getProjectTypeName())) {
                return OWLAPIProjectType.getOBOProjectType();
            }
            else {
                return OWLAPIProjectType.getDefaultProjectType();
            }
        }
        finally {
            READ_LOCK.unlock();
        }
    }


    public void setProjectType(ProjectId projectId, OWLAPIProjectType projectType) throws UnknownProjectException {
        setPropertyValue(projectId, PROJECT_TYPE_PROPERTY_NAME, projectType.getProjectTypeName());
    }

    public String getDisplayName(ProjectId projectId) {
        ProjectInstance pi = getProjectInstance(projectId);
        Slot displayNameSlot = pi.getProtegeInstance().getKnowledgeBase().getSlot("displayName");
        if(displayNameSlot == null) {
            return projectId.getId();
        }
        else {
            return pi.getProtegeInstance().getDirectOwnSlotValue(displayNameSlot).toString();
        }
    }


    public String getDescription(ProjectId projectId) {
        ProjectInstance pi = getProjectInstance(projectId);
        String result = pi.getDescription();
        if(result == null) {
            return "";
        }
        return result;
    }
    
    public void setDescription(ProjectId projectId, String description) {
        ProjectInstance pi = getProjectInstance(projectId);
        pi.setDescription(description);
    }

    public long getCreatedTime(ProjectId projectId) {
        return getLongProperty(projectId, CREATION_TIME_PROPERTY_NAME, 0l);
    }


    public long getLastModifiedTime(ProjectId projectId) {
        return getLongProperty(projectId, LAST_MODIFIED_AT_PROPERTY_NAME, 0l);
    }

    public void setLastModifiedTime(ProjectId projectId, long lastModified) {
//        setLongPropertyValue(projectId, LAST_MODIFIED_AT_PROPERTY_NAME, lastModified);
    }

    public UserId getLastModifiedBy(ProjectId projectId) {
        String userName = getPropertyValue(projectId, LAST_MODIFIED_BY_PROPERTY_NAME, null);
        if(userName == null) {
            return UserId.getGuest();
        }
        else {
            return UserId.getUserId(userName);
        }
    }

    public void setLastModifiedBy(ProjectId projectId, UserId userId) {
//        setPropertyValue(projectId, LAST_MODIFIED_BY_PROPERTY_NAME, userId.getUserName());
    }

    public List<UserId> getOwners(ProjectId projectId) {
        final UserId userId = getOwner(projectId);
        return Arrays.asList(userId);
    }

    private UserId getOwner(ProjectId projectId) {
        ProjectInstance pi = getProjectInstance(projectId);
        User owner = pi.getOwner();
        return UserId.getUserId(owner.getName());
    }

    public ProjectDetails getProjectDetails(ProjectId projectId) {
        String displayName = getDisplayName(projectId);
        String description = getDescription(projectId);
        UserId owner = getOwner(projectId);
        boolean inTrash = isInTrash(projectId);
        return new ProjectDetails(projectId, displayName, description, owner, inTrash);
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void setPropertyValue(ProjectId projectId, String propertyName, String propertyValue) {
        try {
            WRITE_LOCK.lock();
            ProjectInstance pi = getProjectInstance(projectId);
            MetaProject mp = getMetaProject();
            PropertyValue pv = mp.createPropertyValue(propertyName, propertyValue);
            Set<PropertyValue> propertyValues = new HashSet<PropertyValue>(pi.getPropertyValues());
            for(Iterator<PropertyValue> it = propertyValues.iterator(); it.hasNext(); ) {
                PropertyValue curPv = it.next();
                if(curPv.getPropertyName().equals(propertyName)) {
                    it.remove();
                }
            }
            propertyValues.add(pv);
            pi.setPropertyValues(propertyValues);
//            Should be threaded off
            save();
        }
        finally {
            WRITE_LOCK.unlock();
        }
    }


    private void setLongPropertyValue(ProjectId projectId, String propertyName, long value) {
        setPropertyValue(projectId, propertyName, Long.toString(value));
    }
    
    private String getPropertyValue(ProjectId projectId, String propertyName, String defaultValue) {
        try {
            READ_LOCK.lock();
            ProjectInstance pi = getProjectInstance(projectId);
            String value = pi.getPropertyValue(propertyName);
            if(value == null) {
                return defaultValue;
            }
            return value;
        }
        finally {
            READ_LOCK.unlock();
        }
    }

    private long getLongProperty(ProjectId projectId, String propertyName, Long defaultValue) {
        String value = getPropertyValue(projectId, propertyName, null);
        if(value == null) {
            return defaultValue;
        }
        else {
            return Long.parseLong(value);
        }
    }


    private ProjectInstance getProjectInstance(ProjectId projectId) throws UnknownProjectException {
        try {
            READ_LOCK.lock();
            MetaProject metaProject = getMetaProject();
            ProjectInstance pi = metaProject.getProject(projectId.getId());
            if(pi == null) {
                throw new UnknownProjectException(projectId);
            }
            return pi;
        }
        finally {
            READ_LOCK.unlock();
        }
    }

    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Special trash collection


    public boolean isInTrash(ProjectId projectId) {
        try {
            READ_LOCK.lock();
            ProjectInstance pi = getProjectInstance(projectId);
            Instance instance = pi.getProtegeInstance();
            KnowledgeBase knowledgeBase = instance.getKnowledgeBase();
            Slot inTrashSlot = knowledgeBase.getSlot(IN_TRASH_SLOT_NAME);
            if(inTrashSlot == null) {
                return false;
            }
            Object val = instance.getOwnSlotValue(inTrashSlot);
            if(!(val instanceof Boolean)) {
                return false;
            }
            return (Boolean) val;
        }
        finally {
            READ_LOCK.unlock();
        }
    }

    public void setInTrash(ProjectId projectId, boolean b) {
        try {
            WRITE_LOCK.lock();
            ProjectInstance pi = getProjectInstance(projectId);
            Instance instance = pi.getProtegeInstance();
            KnowledgeBase knowledgeBase = instance.getKnowledgeBase();
            Slot inTrashSlot = knowledgeBase.getSlot(IN_TRASH_SLOT_NAME);
            if (inTrashSlot != null) {
                instance.setOwnSlotValue(inTrashSlot, b);
            }
            save();
        }
        finally {
            WRITE_LOCK.unlock();
        }
    }


}
