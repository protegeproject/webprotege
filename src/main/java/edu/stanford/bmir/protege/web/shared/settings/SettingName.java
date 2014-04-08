package edu.stanford.bmir.protege.web.shared.settings;


import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/07/2013
 */
public class SettingName<T extends Serializable> implements Serializable {

    private String name;

    private Class<T> settingValueClass;

    public SettingName(String name, Class<T> settingValueClass) {
        this.name = checkNotNull(name);
        this.settingValueClass = settingValueClass;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return "SettingName".hashCode() + settingValueClass.hashCode() + name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof SettingName)) {
            return false;
        }
        SettingName other = (SettingName) obj;
        return this.name.equals(other.name) && this.settingValueClass.equals(other.settingValueClass);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SettingName");
        sb.append("(");
        sb.append(name);
        sb.append(" Class(");
        sb.append(settingValueClass);
        sb.append("))");
        return sb.toString();
    }

}
