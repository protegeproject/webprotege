package edu.stanford.bmir.protege.web.client.ui.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;

public class GlobalSelectionManager {

    private static Map<String, Collection<EntityData>> ont2sel = new HashMap<String, Collection<EntityData>>();

    private static Map<String, String> ont2tab = new HashMap<String, String>();

    public static Collection<EntityData> getGlobalSelection(String ont) {
        return ont2sel.get(ont);
    }

    public static void setGlobalSelection(String ont, Collection<EntityData> sel) {
        ont2sel.put(ont, sel);
    }

    public static String getTab(String ont) {
        return ont2tab.get(ont);
    }

    public static void setTab(String ont, String tab) {
        ont2tab.put(ont, tab);
    }

}
