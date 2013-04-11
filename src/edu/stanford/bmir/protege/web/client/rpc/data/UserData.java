package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class UserData implements Serializable {

    private static final long serialVersionUID = -2102841071892674245L;

    private UserId userId;

    private String email;

//	private Collection<String> groups;

    private Map<String, String> properties = new HashMap<String, String>();

	private UserData() {	}

	public UserData(UserId userId) {
	    this.userId = userId;
	}

    public UserId getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    // USED BY OPTRA STUFF

    /**
     * @deprecated Do not use.  This will be removed.
     */
    @Deprecated
    public String getProperty(String prop) {
        return properties.get(prop);
    }

    /**
     * @deprecated Do not used.  This will be removed.
     */
    @Deprecated
    public void setProperty(String prop, String value) {
        properties.put(prop, value);
    }
}
