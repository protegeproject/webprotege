package edu.stanford.bmir.protege.web.client.rpc.bioportal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class BioPortalOntologyInfo implements Serializable {

    private BioPortalOntologyId ontologyId;
    
    private String displayLabel;
    
    private String abbreviation;
    
    private String versionNumber;
    
    private String description;

    private List<BioPortalUserId> owners = new ArrayList<BioPortalUserId>();
    
    /**
     * Empty constructor for serialization purposes only.
     */
    private BioPortalOntologyInfo() {
    }


    public BioPortalOntologyInfo(BioPortalOntologyId ontologyId, String displayLabel, String abbreviation, String versionNumber, String description, List<BioPortalUserId> owners) {
        this.ontologyId = ontologyId;
        this.displayLabel = displayLabel;
        this.abbreviation = abbreviation;
        this.versionNumber = versionNumber;
        this.description = description;
        this.owners.addAll(owners);
    }

    public BioPortalOntologyId getOntologyId() {
        return ontologyId;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public String getDescription() {
        return description;
    }

    public List<BioPortalUserId> getOwners() {
        return new ArrayList<BioPortalUserId>(owners);
    }

    @Override
    public String toString() {
        return "BioPortalOntologyInfo(" + ontologyId + " displayLabel(" + displayLabel + ") abbreviation(" + abbreviation + ") versionNumber(" + versionNumber + "))";
    }
}
