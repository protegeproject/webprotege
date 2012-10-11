package edu.stanford.bmir.protege.web.client.rpc.data.extref;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/10/2012
 */
public class ExternalReferenceType implements Serializable {

    private static final ExternalReferenceType SUB_CLASS_TYPE = new ExternalReferenceType("SubClass");

    private static final ExternalReferenceType RDFS_SEE_ALSO_TYPE = new ExternalReferenceType("rdfs:seeAlso");

    private String description;

    public ExternalReferenceType(String description) {
        this.description = description;
    }

    /**
     * Gets a partial description of the reference type that can be concatenated onto the string "Add reference as "
     * for use in the UI.
     * @return A short partial description of the reference type (e.g. "SubClass", "rdfs:seeAlso annotation"). Not
     * <code>null</code>.
     */
    public String getDescription() {
        return description;
    }


    public static ExternalReferenceType getSubClassType() {
        return SUB_CLASS_TYPE;
    }

    public static ExternalReferenceType getSeeAlsoType() {
        return RDFS_SEE_ALSO_TYPE;
    }

    @Override
    public int hashCode() {
        return "ExternalReferenceType".hashCode() + description.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ExternalReferenceType)) {
            return false;
        }
        ExternalReferenceType other = (ExternalReferenceType) obj;
        return this.description.equals(other.description);
    }
}
