package edu.stanford.bmir.protege.web.client.rpc.data;


import java.io.Serializable;
import java.util.Comparator;

/**
 * @author z.khan
 *
 */
public class AccessPolicyUserData implements Serializable, Comparator<AccessPolicyUserData> {
    private String name;
    private String password;

    private boolean isPartofReaders;
    private boolean isPartofWriters;

    public AccessPolicyUserData() {
    }

    public AccessPolicyUserData(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPartofReaders() {
        return isPartofReaders;
    }

    public void setPartofReaders(boolean isPartofReaders) {
        this.isPartofReaders = isPartofReaders;
    }

    public boolean isPartofWriters() {
        return isPartofWriters;
    }

    public void setPartofWriters(boolean isPartofWriters) {
        this.isPartofWriters = isPartofWriters;
    }

    public int compare(AccessPolicyUserData o1, AccessPolicyUserData o2) {
        try {
            if (o1.getName() == null || o2.getName() == null) {
                return 0;
            }
            return o1.getName().compareToIgnoreCase(o2.getName());
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        AccessPolicyUserData userDataToCompare = (AccessPolicyUserData) obj;
        if (this.getName().equals(userDataToCompare.getName())
                ) {
            return true;
        }
        return false;
    }
}
