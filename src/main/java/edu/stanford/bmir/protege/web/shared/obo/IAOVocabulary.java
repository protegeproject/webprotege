package edu.stanford.bmir.protege.web.shared.obo;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/11/2012
 */
public enum IAOVocabulary {

    DEFINITION("IAO_0000115");


    private String suffix;

    IAOVocabulary(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }
}
