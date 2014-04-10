package edu.stanford.bmir.protege.web.client.rpc.data.layout;

import java.io.Serializable;
import java.util.Map;

public class GenericConfiguration implements Serializable {
	private static final long serialVersionUID = 7898127979213L;
	
	private Map<String, Object> properties;

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

    public String getStringProperty(String prop, String defaultValue) {
        if (properties == null) {
            return defaultValue;
        }
        try {
            String value = (String) properties.get(prop);
            if (value == null) {
                value = defaultValue;
            }
            return value;
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    public int getIntegerProperty(String prop, int defaultValue) {
    	if (properties == null) {
    		return defaultValue;
    	}
    	try {
    		Integer value = (Integer) properties.get(prop);
    		if (value == null) {
    			value = defaultValue;
    		}
    		return value.intValue();
    	} catch (Exception e) {
        	try {
        		String value = (String) properties.get(prop);
        		return Integer.parseInt(value);
        	} catch (Exception se) {
        		return defaultValue;
        	}
    	}
    }

    public boolean getBooleanProperty(String prop, boolean defaultValue) {
        if (properties == null) {
            return defaultValue;
        }
        try {
        	Boolean value = (Boolean) properties.get(prop);
            if (value == null) {
                value = defaultValue;
            }
            return value.booleanValue();
        } catch (Exception e) {
        	try {
        		String value = (String) properties.get(prop);
        		return Boolean.parseBoolean(value);
        	} catch (Exception se) {
        		return defaultValue;
        	}
        }
    }
	
	
}
