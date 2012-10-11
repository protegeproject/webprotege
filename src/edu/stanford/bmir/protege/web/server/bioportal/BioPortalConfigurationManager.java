package edu.stanford.bmir.protege.web.server.bioportal;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class BioPortalConfigurationManager {

    private static final BioPortalConfigurationManager instance = new BioPortalConfigurationManager();
    
    private String restBase = "http://stagerest.bioontology.org/bioportal/";

    private BioPortalConfigurationManager() {
    }

    public static BioPortalConfigurationManager getManager() {
        return instance;
    }

    public String getRestBase() {
        return restBase;
    }
}
