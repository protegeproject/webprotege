package edu.stanford.bmir.protege.web.shared.settings;

import java.io.Serializable;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/07/2013
 */
public interface HasSettings {

    List<SettingName<?>> getSettingNames();

    <T extends Serializable> T getSetting(SettingName<T> settingName, T defaultValue);

    <T extends Serializable> void setSetting(SettingName<T> settingName, T value);

    <T extends Serializable> void clearSetting(SettingName<T> settingName);
}
