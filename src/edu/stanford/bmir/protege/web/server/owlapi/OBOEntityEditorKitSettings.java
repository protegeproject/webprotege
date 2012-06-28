package edu.stanford.bmir.protege.web.server.owlapi;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 24/05/2012
 */
public class OBOEntityEditorKitSettings implements Serializable {

    public static final int DEFAULT_ID_LENGTH = 7;
    
    private int idLength;

    public OBOEntityEditorKitSettings() {
        idLength = DEFAULT_ID_LENGTH;
    }

    public OBOEntityEditorKitSettings(int idLength) {
        this.idLength = idLength;
    }

    public int getIdLength() {
        return idLength;
    }
}
