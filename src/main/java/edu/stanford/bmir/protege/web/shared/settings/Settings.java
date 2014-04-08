package edu.stanford.bmir.protege.web.shared.settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/07/2013
 */
public class Settings implements HasSettings {

    private Map<SettingName<?>, Serializable> map = new HashMap<SettingName<?>, Serializable>();

    @Override
    public List<SettingName<?>> getSettingNames() {
        return new ArrayList<SettingName<?>>(map.keySet());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Serializable> T getSetting(SettingName<T> settingName, T defaultValue) {
        Serializable value = map.get(checkNotNull(settingName));
        if(value == null) {
            return defaultValue;
        }
        return (T) value;
    }

    @Override
    public <T extends Serializable> void setSetting(SettingName<T> settingName, T value) {
        map.put(checkNotNull(settingName), checkNotNull(value));
    }

    @Override
    public <T extends Serializable> void clearSetting(SettingName<T> settingName) {
        map.remove(checkNotNull(settingName));
    }
}
