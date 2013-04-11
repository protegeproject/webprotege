package edu.stanford.bmir.protege.web.client.ui.portlet.propertyForm;

import edu.stanford.bmir.protege.web.client.rpc.data.layout.ProjectLayoutConfiguration;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

import java.util.Map;

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
    
    public static String getAddNewValueActionDesc(Map<String, Object> widgetConfig, ProjectLayoutConfiguration projectLayoutConfiguration, String defaultDesc) {
        return UIUtil.getStringConfigurationProperty(widgetConfig, projectLayoutConfiguration, ADD_NEW_VALUE_PROP, defaultDesc );
    }
    
    public static String getAddExistingValueActionDesc(Map<String, Object> widgetConfig, ProjectLayoutConfiguration projectLayoutConfiguration, String defaultDesc) {
        return UIUtil.getStringConfigurationProperty(widgetConfig, projectLayoutConfiguration, ADD_EXISTING_VALUE_PROP, defaultDesc );
    }
    
    public static String getReplaceNewValueActionDesc(Map<String, Object> widgetConfig, ProjectLayoutConfiguration projectLayoutConfiguration, String defaultDesc) {
        return UIUtil.getStringConfigurationProperty(widgetConfig, projectLayoutConfiguration, REPLACE_WITH_NEW_VALUE_PROP, defaultDesc);
    }
    
    public static String getReplaceExistingValueActionDesc(Map<String, Object> widgetConfig, ProjectLayoutConfiguration projectLayoutConfiguration, String defaultDesc) {
        return UIUtil.getStringConfigurationProperty(widgetConfig, projectLayoutConfiguration, REPLACE_WITH_EXISTING_VALUE_PROP, defaultDesc);
    }

    
    
    public static String getAddNewValueActionDesc(Map<String, Object> widgetConfig, ProjectLayoutConfiguration projectLayoutConfiguration) {
        return getAddNewValueActionDesc(widgetConfig, projectLayoutConfiguration, ADD_NEW_VALUE );
    }

    public static String getAddExistingValueActionDesc(Map<String, Object> widgetConfig, ProjectLayoutConfiguration projectLayoutConfiguration) {
        return getAddExistingValueActionDesc(widgetConfig, projectLayoutConfiguration, ADD_EXISTING_VALUE );
    }

    public static String getReplaceNewValueActionDesc(Map<String, Object> widgetConfig, ProjectLayoutConfiguration projectLayoutConfiguration) {
        return getReplaceNewValueActionDesc(widgetConfig, projectLayoutConfiguration, REPLACE_WITH_NEW_VALUE);
    }

    public static String getReplaceExistingValueActionDesc(Map<String, Object> widgetConfig, ProjectLayoutConfiguration projectLayoutConfiguration) {
        return getReplaceExistingValueActionDesc(widgetConfig, projectLayoutConfiguration, REPLACE_WITH_EXISTING_VALUE);
    }

    /*
     * Icons
     */

    public static String getAddIcon(Map<String, Object> widgetConfig, ProjectLayoutConfiguration projectLayoutConfiguration) {
        return UIUtil.getStringConfigurationProperty(widgetConfig, projectLayoutConfiguration, ADD_ICON_PROP, ADD_ICON);
    }

    public static String getReplaceIcon(Map<String, Object> widgetConfig, ProjectLayoutConfiguration projectLayoutConfiguration) {
        return UIUtil.getStringConfigurationProperty(widgetConfig, projectLayoutConfiguration, REPLACE_ICON_PROP, REPLACE_ICON);
    }

    /*
     * Action links
     */

    public static String getAddNewLink(Map<String, Object> widgetConfig, ProjectLayoutConfiguration projectLayoutConfiguration) {
        return getIconLink(getAddNewValueActionDesc(widgetConfig, projectLayoutConfiguration), getAddIcon(widgetConfig, projectLayoutConfiguration));
    }

    public static String getAddExistingLink(Map<String, Object> widgetConfig, ProjectLayoutConfiguration projectLayoutConfiguration) {
        return getIconLink(getAddExistingValueActionDesc(widgetConfig, projectLayoutConfiguration), getAddIcon(widgetConfig, projectLayoutConfiguration));
    }

    public static String getReplaceNewLink(Map<String, Object> widgetConfig, ProjectLayoutConfiguration projectLayoutConfiguration) {
        return getIconLink(getReplaceNewValueActionDesc(widgetConfig, projectLayoutConfiguration), getReplaceIcon(widgetConfig, projectLayoutConfiguration));
    }

    public static String getReplaceExistingLink(Map<String, Object> widgetConfig, ProjectLayoutConfiguration projectLayoutConfiguration) {
        return getIconLink(getReplaceExistingValueActionDesc(widgetConfig, projectLayoutConfiguration), getReplaceIcon(widgetConfig, projectLayoutConfiguration));
    }

    public static String getIconLink(String text, String icon) {
        return "<table style=\"padding:3px 0px;\"><tr><td style=\"vertical-align:text-center; padding: 0px 3px 0px 0px;\"><img src=\"" + icon + "\" ></img></td><td>" + text + "</td></tr></table>";
    }

    /*
     * Show action links
     */

    public static boolean getShowAction(Map<String, Object> widgetConfig, ProjectLayoutConfiguration projectLayoutConfiguration,
            String action, boolean defaultValue) {
        return UIUtil.getBooleanConfigurationProperty(widgetConfig, projectLayoutConfiguration, action, defaultValue);
    }

    public static boolean showAddNewActionLink(Map<String, Object> widgetConfig, ProjectLayoutConfiguration projectLayoutConfiguration) {
        return getShowAction(widgetConfig, projectLayoutConfiguration, SHOW_ADD_NEW_PROP, true);
    }

    public static boolean showAddExistingActionLink(Map<String, Object> widgetConfig, ProjectLayoutConfiguration projectLayoutConfiguration) {
        return getShowAction(widgetConfig, projectLayoutConfiguration, SHOW_ADD_EXISTING_NEW_PROP, true);
    }

    public static boolean showReplaceNewActionLink(Map<String, Object> widgetConfig, ProjectLayoutConfiguration projectLayoutConfiguration) {
        return getShowAction(widgetConfig, projectLayoutConfiguration, SHOW_REPLACE_NEW_PROP,
                showAddNewActionLink(widgetConfig, projectLayoutConfiguration));
    }

    public static boolean showReplaceExistingActionLink(Map<String, Object> widgetConfig, ProjectLayoutConfiguration projectLayoutConfiguration) {
        return getShowAction(widgetConfig, projectLayoutConfiguration, SHOW_REPLACE_EXISTING_PROP,
                showAddExistingActionLink(widgetConfig, projectLayoutConfiguration));
    }


}
