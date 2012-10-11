package edu.stanford.bmir.protege.web.client.rpc.bioportal;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 * <p>
 *     Represents a BioPortal user id (which is an integer).
 * </p>
 */
public class BioPortalUserId implements Serializable {

    private int intValue;

    /**
     * Empty constructor for serialization purposes only.
     */
    private BioPortalUserId() {

    }

    /**
     * Creates a BioPortalUserId based on the specified int value.
     * @param intValue The int value on which to base the BioPortalUserId
     */
    private BioPortalUserId(int intValue) {
        this.intValue = intValue;
    }

    /**
     * Creates a BioPortalUserId from an int id.
     * @param id The int id which to create the BioPortalUserId from.
     * @return The BioPortalUserId that corresponds to the int id.
     */
    public static BioPortalUserId createFromId(int id) {
        return new BioPortalUserId(id);
    }

    /**
     * Gets the int id representation of this BioPortalUserId.
     * @return The int id value of this BioPortalUserId
     */
    public int getIntValue() {
        return intValue;
    }


    @Override
    public int hashCode() {
        return "BioPortalUserId".hashCode() + intValue;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof BioPortalUserId)) {
            return false;
        }
        BioPortalUserId other = (BioPortalUserId) obj;
        return this.intValue == other.intValue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BioPortalUserId(");
        sb.append(intValue);
        sb.append(")");
        return sb.toString();
    }
}
