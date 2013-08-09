package edu.stanford.bmir.protege.web.shared.irigen;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class IRIGeneratorSettings implements Serializable {

    private String iriPrefix;

    private SuffixSettings schemeSpecificSettings;

    private IRIGeneratorSettings() {
    }

    public IRIGeneratorSettings(String iriPrefix, SuffixSettings schemeSpecificSettings) {
        this.iriPrefix = iriPrefix;
        this.schemeSpecificSettings = schemeSpecificSettings;
    }

    public String getIRIPrefix() {
        return iriPrefix;
    }

    public SuffixSettings getSchemeSpecificSettings() {
        return schemeSpecificSettings;
    }
}
