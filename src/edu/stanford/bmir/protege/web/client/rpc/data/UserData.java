package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class UserData implements Serializable {
	private static final long serialVersionUID = -2102841071892674245L;

    private String name;
    private String email;
	private Collection<String> groups;
	private Map<String, String> properties = new HashMap<String, String>();

	public UserData() {	}

	public UserData(String name) {
	    this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public Collection<String> getGroups() {
        return groups;
    }

    public void setGroups(Collection<String> groups) {
        this.groups = groups;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProperty(String prop) {
        return properties.get(prop);
    }

    public void setProperty(String prop, String value) {
        properties.put(prop, value);
    }
}
