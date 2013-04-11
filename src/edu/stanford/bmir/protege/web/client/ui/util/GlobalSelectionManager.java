package edu.stanford.bmir.protege.web.client.ui.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

public class GlobalSelectionManager {

    private static Map<ProjectId, Collection<EntityData>> ont2sel = new HashMap<ProjectId, Collection<EntityData>>();

    private static Map<ProjectId, String> ont2tab = new HashMap<ProjectId, String>();

    public static Collection<EntityData> getGlobalSelection(ProjectId projectId) {
        return ont2sel.get(projectId);
    }

    public static void setGlobalSelection(ProjectId projectId, Collection<EntityData> sel) {
        ont2sel.put(projectId, sel);
    }

    public static String getTab(ProjectId projectId) {
        return ont2tab.get(projectId);
    }

    public static void setTab(ProjectId projectId, String tab) {
        ont2tab.put(projectId, tab);
    }

}
