package edu.stanford.bmir.protege.web.client.rpc.bioportal;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class BioPortalOntologyId implements Serializable {

    public static final BioPortalOntologyId NULL_VALUE = new BioPortalOntologyId(-1);

    private int intValue;

    /**
     * Default constructor for serialization purposes only
     */
    private BioPortalOntologyId() {
    }

    private BioPortalOntologyId(int intValue) {
        this.intValue = intValue;
    }

    public boolean isNull() {
        return this.equals(NULL_VALUE);
    }

    public static BioPortalOntologyId getNull() {
        return NULL_VALUE;
    }

    public static BioPortalOntologyId getId(int intValue) {
        return new BioPortalOntologyId(intValue);
    }

    public int getIntValue() {
        return intValue;
    }

    @Override
    public int hashCode() {
        return "BioPortalOntologyId".hashCode() + intValue;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof BioPortalOntologyId)) {
            return false;
        }
        BioPortalOntologyId other = (BioPortalOntologyId) obj;
        return this.intValue == other.intValue;
    }

    @Override
    public String toString() {
        return "BioPortalOntologyId(" + intValue + ")";
    }
}
