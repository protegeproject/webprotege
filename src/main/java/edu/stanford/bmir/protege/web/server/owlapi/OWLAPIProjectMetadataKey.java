package edu.stanford.bmir.protege.web.server.owlapi;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/08/2012
 */
public enum OWLAPIProjectMetadataKey {

    ID("id"),

    TYPE("type"),

    NAME("name"),

    DESCRIPTION("description"),

    OWNER("owner"),

    LAST_MODIFIED("lastModified");
    
    private String keyName;

    private OWLAPIProjectMetadataKey(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyName() {
        return keyName;
    }
}
