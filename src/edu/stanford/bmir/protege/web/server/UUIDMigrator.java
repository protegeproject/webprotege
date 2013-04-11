package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.server.logging.WebProtegeLogger;
import edu.stanford.bmir.protege.web.server.logging.WebProtegeLoggerManager;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIMetaProjectStore;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectFileStore;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.server.metaproject.MetaProject;
import edu.stanford.smi.protege.server.metaproject.ProjectInstance;
import edu.stanford.smi.protege.server.metaproject.impl.MetaProjectImpl;

import java.io.File;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/04/2013
 * <p>
 *     A one-shot project renaming migrator.  This migrator changes project names to UUIDs.  Existing names are preserved
 *     in a displaySlot.  The migrator will not run if the displaySlot exists in the specified metaproject.
 * </p>
 * <p>
 *     For each project instance pi, pi is renamed using a freshly generated UUID.  The previous name for pi is set
 *     as an own slot value in the displayName slot.  The project data location pi is updated on disk.
 * </p>
 */
public class UUIDMigrator {


    private static final String DISPLAY_NAME_SLOT_NAME = "displayName";

    private static final String POLICY_CONTROLLED_OBJECT_CLS_NAME = "PolicyControlledObject";

    private MetaProjectManager mpm;

    private MetaProject metaProject;

    public UUIDMigrator(MetaProjectManager mpm) {
        this.mpm = mpm;
        this.metaProject = mpm.getMetaProject();
    }

    /**
     * Runs this migrator on the metaproject specified in the constructor.  The migrator will not run if the specified
     * project has already been migrated.  As part of the migration process, project locations will be changed on disk.
     */
    public void runMigrator() {
        migrateToUUIDIfNecessary();
    }

    private void migrateToUUIDIfNecessary() {
        // Convert name to display name and generate a UUID for the name
        MetaProjectImpl metaProjectImpl = (MetaProjectImpl) metaProject;
        KnowledgeBase kb = metaProjectImpl.getKnowledgeBase();
        if (isInUUIDFormat(kb)) {
            return;
        }
        performUUIDMigration(metaProjectImpl, kb);
    }

    private void performUUIDMigration(MetaProjectImpl metaProjectImpl, KnowledgeBase kb) {
        WebProtegeLoggerManager.get(UUIDMigrator.class).info("Migrating metaproject to use UUID based format");
        createDisplayNameSlot(kb);
        for (ProjectInstance pi : metaProjectImpl.getProjects()) {
            migrateProjectInstance(kb, pi);
        }
        OWLAPIMetaProjectStore.getStore().saveMetaProject(mpm);
    }

    private void migrateProjectInstance(KnowledgeBase kb, ProjectInstance pi) {
        final Instance protegeInstance = pi.getProtegeInstance();
        final String existingName = pi.getName();
        final Slot displayNameSlot = kb.getSlot(DISPLAY_NAME_SLOT_NAME);

        final ProjectId newProjectId = ProjectIdFactory.getFreshProjectId();
        protegeInstance.rename(newProjectId.getId());
        protegeInstance.setOwnSlotValue(displayNameSlot, existingName);
        moveProjectDirectoryOnDisk(existingName, newProjectId);
    }

    private void moveProjectDirectoryOnDisk(String oldProjectName, ProjectId newProjectId) {
        final String escapedOldProjectName = oldProjectName.replace(" ", "-");
        final File newProjectDirectory = OWLAPIProjectFileStore.getProjectFileStore(newProjectId).getProjectDirectory();
        final File oldEscapedProjectDirectory = new File(newProjectDirectory.getParent(), escapedOldProjectName);
        if (oldEscapedProjectDirectory.exists()) {
            oldEscapedProjectDirectory.renameTo(newProjectDirectory);
        }
        else {
            File oldProjectDirectory = new File(newProjectDirectory.getParent(), oldProjectName);
            if(oldProjectDirectory.exists()) {
                oldProjectDirectory.renameTo(newProjectDirectory);
            }
        }
    }

    private Slot createDisplayNameSlot(KnowledgeBase kb) {
        final Slot displayNameSlot = kb.createSlot(DISPLAY_NAME_SLOT_NAME);
        final Cls policyControlledObjectCls = kb.getCls(POLICY_CONTROLLED_OBJECT_CLS_NAME);
        policyControlledObjectCls.addDirectTemplateSlot(displayNameSlot);
        return displayNameSlot;
    }

    private boolean isInUUIDFormat(KnowledgeBase kb) {
        return kb.getSlot(DISPLAY_NAME_SLOT_NAME) != null;
    }

}
