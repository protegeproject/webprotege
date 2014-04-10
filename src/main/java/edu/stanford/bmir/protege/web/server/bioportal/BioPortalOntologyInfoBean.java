package edu.stanford.bmir.protege.web.server.bioportal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class BioPortalOntologyInfoBean {

    @XmlElement(name = "ontologyId")
    private int ontologyId;
    
    @XmlElement(name = "versionNumber")
    private String versionNumber;
    
    @XmlElement(name = "displayLabel")
    private String displayLabel;
    
    @XmlElement(name = "description")
    private String description;
    
    @XmlElement(name = "abbreviation")
    private String abbreviation;

    @XmlElementWrapper(name = "userIds")
    @XmlElement(name = "int")
    private List<Integer> owners;

    private BioPortalOntologyInfoBean() {

    }

    public BioPortalOntologyInfoBean(int ontologyId, String versionNumber, String displayLabel, String description, String abbreviation) {
        this.ontologyId = ontologyId;
        this.versionNumber = versionNumber;
        this.displayLabel = displayLabel;
        this.description = description;
        this.abbreviation = abbreviation;
    }

    public int getOntologyId() {
        return ontologyId;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }

    public String getDescription() {
        return description;
    }

    public String getAbrreviation() {
        return abbreviation;
    }

    public List<Integer> getOwners() {
        return owners;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BioPortalOntologyInfoBean(");
        sb.append("ontologyId(");
        sb.append(ontologyId);
        sb.append(") ");
        sb.append("versionNumber(");
        sb.append(versionNumber);
        sb.append(") ");
        sb.append("displayLabel(");
        sb.append(displayLabel);
        sb.append(") ");
        sb.append("abbreviation(");
        sb.append(abbreviation);
        sb.append(")");
        sb.append(")");
        return sb.toString();
    }
}
