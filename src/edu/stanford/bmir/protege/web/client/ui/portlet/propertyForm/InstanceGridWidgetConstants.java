package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import java.util.Map;

import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectConfiguration;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

public class InstanceGridWidgetConstants {

    /*
     * Defaults for grid action names
     */
    public static final String ADD_NEW_VALUE = "Add new value";
    public static final String ADD_EXISTING_VALUE = "Add existing value";
    public static final String REPLACE_WITH_NEW_VALUE = "Replace with new value";
    public static final String REPLACE_WITH_EXISTING_VALUE = "Replace with existing value";

    public static final String ADD_ICON = "images/add.png";
    public static final String REPLACE_ICON = "images/arrow_refresh_small.png";

    /*
     * Properties used in configuration.xml to override defaults
     */
    public static final String ADD_NEW_VALUE_PROP = "add_new_value_desc";
    public static final String ADD_EXISTING_VALUE_PROP = "add_existing_value_desc";
    public static final String REPLACE_WITH_NEW_VALUE_PROP = "replace_with_new_value_desc";
    public static final String REPLACE_WITH_EXISTING_VALUE_PROP = "replace_with_existing_value_desc";

    public static final String ADD_ICON_PROP = "add_icon";
    public static final String REPLACE_ICON_PROP = "replace_icon";

    public static final String SHOW_ADD_NEW_PROP = "show_add_new";
    public static final String SHOW_ADD_EXISTING_NEW_PROP = "show_add_existing";
    public static final String SHOW_REPLACE_NEW_PROP = "show_replace_new";
    public static final String SHOW_REPLACE_EXISTING_PROP = "show_replace_existing";

    /*
     * Util methods
     */

    /*
     * Action descriptions
     */
    
    public static String getAddNewValueActionDesc(Map<String, Object> widgetConfig, ProjectConfiguration projectConfiguration, String defaultDesc) {
        return UIUtil.getStringConfigurationProperty(widgetConfig, projectConfiguration, ADD_NEW_VALUE_PROP, defaultDesc );
    }
    
    public static String getAddExistingValueActionDesc(Map<String, Object> widgetConfig, ProjectConfiguration projectConfiguration, String defaultDesc) {
        return UIUtil.getStringConfigurationProperty(widgetConfig, projectConfiguration, ADD_EXISTING_VALUE_PROP, defaultDesc );
    }
    
    public static String getReplaceNewValueActionDesc(Map<String, Object> widgetConfig, ProjectConfiguration projectConfiguration, String defaultDesc) {
        return UIUtil.getStringConfigurationProperty(widgetConfig, projectConfiguration, REPLACE_WITH_NEW_VALUE_PROP, defaultDesc);
    }
    
    public static String getReplaceExistingValueActionDesc(Map<String, Object> widgetConfig, ProjectConfiguration projectConfiguration, String defaultDesc) {
        return UIUtil.getStringConfigurationProperty(widgetConfig, projectConfiguration, REPLACE_WITH_EXISTING_VALUE_PROP, defaultDesc);
    }

    
    
    public static String getAddNewValueActionDesc(Map<String, Object> widgetConfig, ProjectConfiguration projectConfiguration) {
        return getAddNewValueActionDesc(widgetConfig, projectConfiguration, ADD_NEW_VALUE );
    }

    public static String getAddExistingValueActionDesc(Map<String, Object> widgetConfig, ProjectConfiguration projectConfiguration) {
        return getAddExistingValueActionDesc(widgetConfig, projectConfiguration, ADD_EXISTING_VALUE );
    }

    public static String getReplaceNewValueActionDesc(Map<String, Object> widgetConfig, ProjectConfiguration projectConfiguration) {
        return getReplaceNewValueActionDesc(widgetConfig, projectConfiguration, REPLACE_WITH_NEW_VALUE);
    }

    public static String getReplaceExistingValueActionDesc(Map<String, Object> widgetConfig, ProjectConfiguration projectConfiguration) {
        return getReplaceExistingValueActionDesc(widgetConfig, projectConfiguration, REPLACE_WITH_EXISTING_VALUE);
    }

    /*
     * Icons
     */

    public static String getAddIcon(Map<String, Object> widgetConfig, ProjectConfiguration projectConfiguration) {
        return UIUtil.getStringConfigurationProperty(widgetConfig, projectConfiguration, ADD_ICON_PROP, ADD_ICON);
    }

    public static String getReplaceIcon(Map<String, Object> widgetConfig, ProjectConfiguration projectConfiguration) {
        return UIUtil.getStringConfigurationProperty(widgetConfig, projectConfiguration, REPLACE_ICON_PROP, REPLACE_ICON);
    }

    /*
     * Action links
     */

    public static String getAddNewLink(Map<String, Object> widgetConfig, ProjectConfiguration projectConfiguration) {
        return getIconLink(getAddNewValueActionDesc(widgetConfig, projectConfiguration), getAddIcon(widgetConfig, projectConfiguration));
    }

    public static String getAddExistingLink(Map<String, Object> widgetConfig, ProjectConfiguration projectConfiguration) {
        return getIconLink(getAddExistingValueActionDesc(widgetConfig, projectConfiguration), getAddIcon(widgetConfig, projectConfiguration));
    }

    public static String getReplaceNewLink(Map<String, Object> widgetConfig, ProjectConfiguration projectConfiguration) {
        return getIconLink(getReplaceNewValueActionDesc(widgetConfig, projectConfiguration), getReplaceIcon(widgetConfig, projectConfiguration));
    }

    public static String getReplaceExistingLink(Map<String, Object> widgetConfig, ProjectConfiguration projectConfiguration) {
        return getIconLink(getReplaceExistingValueActionDesc(widgetConfig, projectConfiguration), getReplaceIcon(widgetConfig, projectConfiguration));
    }

    public static String getIconLink(String text, String icon) {
        return "<table style=\"padding:3px 0px;\"><tr><td style=\"vertical-align:text-center; padding: 0px 3px 0px 0px;\"><img src=\"" + icon + "\" ></img></td><td>" + text + "</td></tr></table>";
    }

    /*
     * Show action links
     */

    public static boolean getShowAction(Map<String, Object> widgetConfig, ProjectConfiguration projectConfiguration, 
            String action, boolean defaultValue) {
        return UIUtil.getBooleanConfigurationProperty(widgetConfig, projectConfiguration, action, defaultValue);
    }

    public static boolean showAddNewActionLink(Map<String, Object> widgetConfig, ProjectConfiguration projectConfiguration) {
        return getShowAction(widgetConfig, projectConfiguration, SHOW_ADD_NEW_PROP, true);
    }

    public static boolean showAddExistingActionLink(Map<String, Object> widgetConfig, ProjectConfiguration projectConfiguration) {
        return getShowAction(widgetConfig, projectConfiguration, SHOW_ADD_EXISTING_NEW_PROP, true);
    }

    public static boolean showReplaceNewActionLink(Map<String, Object> widgetConfig, ProjectConfiguration projectConfiguration) {
        return getShowAction(widgetConfig, projectConfiguration, SHOW_REPLACE_NEW_PROP, 
                showAddNewActionLink(widgetConfig, projectConfiguration));
    }

    public static boolean showReplaceExistingActionLink(Map<String, Object> widgetConfig, ProjectConfiguration projectConfiguration) {
        return getShowAction(widgetConfig, projectConfiguration, SHOW_REPLACE_EXISTING_PROP, 
                showAddExistingActionLink(widgetConfig, projectConfiguration));
    }


}
