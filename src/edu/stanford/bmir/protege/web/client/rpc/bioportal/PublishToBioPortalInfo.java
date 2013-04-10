package edu.stanford.bmir.protege.web.client.rpc.bioportal;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class PublishToBioPortalInfo implements Serializable {

//    StringBuffer params = new StringBuffer();
//    addIfNotNull(params, "downloadLocation", downloadLocation);
//    addIfNotNull(params, "displayLabel", displayLabel);
//    addIfNotNull(params, "format", format);
//    addIfNotNull(params, "dateReleased", dateReleased);
//    addIfNotNull(params, "contactName", contactName);
//    addIfNotNull(params, "contactEmail", contactEmail);
//    addIfNotNull(params, "abbreviation", abbreviation);
//    addIfNotNull(params, "versionNumber", versionNumber);
//    addIfNotNull(params, "homepage", homepage);
//    addIfNotNull(params, "documentation", documentation);
//    addIfNotNull(params, "publication", publication);
//    addIfNotNull(params, "viewingRestriction", viewingRestriction);
//    addIfNotNull(params, "useracl", useracl);
//    addIfNotNull(params, "description", description);
//    addIfNotNull(params, "categories", categories);
//    addIfNotNull(params, "synonymSlot", synonymSlot);
//    addIfNotNull(params, "preferredNameSlot", preferredNameSlot);
//    addIfNotNull(params, "documentationSlot", documentationSlot);
//    addIfNotNull(params, "authorSlot", authorSlot);

//    Upload ontology to BioPortal from a URL.
//    * Required arguments: downloadLocation, displayLabel, userId, format, dateReleased, contactName, contactEmail
//    *
//            * Documentation for BioPortal Upload REST service: https://gist.github.com/a5b9f715a6a6416b52ff
//            *


    private BioPortalOntologyId ontologyId;

    private BioPortalUserId userId;
    
    private String ontologyDisplayName;

    private String ontologyAbbreviation;

    private String ontologyDescription;
    
    
    
    private String contactName;
    
    private String contactEmailAddress;

    
    
    private String versionNumber;
    
    private String homepageLink;

    private String documentationLink;
    

    private String publicationLink;


    public PublishToBioPortalInfo(BioPortalUserId userId, BioPortalOntologyId bioPortalOntologyId, String ontologyDisplayName, String ontologyAbbreviation, String ontologyDescription, String contactName, String contactEmailAddress, String versionNumber) {
        this.userId = userId;
        this.ontologyId = bioPortalOntologyId;
        this.ontologyDisplayName = ontologyDisplayName;
        this.ontologyAbbreviation = ontologyAbbreviation;
        this.ontologyDescription = ontologyDescription;
        this.contactName = contactName;
        this.contactEmailAddress = contactEmailAddress;
        this.versionNumber = versionNumber;
    }

    public PublishToBioPortalInfo(BioPortalUserId userId, BioPortalOntologyId bioPortalOntologyId, String ontologyDisplayName, String ontologyAbbreviation, String ontologyDescription, String contactName, String contactEmailAddress, String versionNumber, String homepageLink, String documentationLink, String publicationLink) {
        this.userId = userId;
        this.ontologyId = bioPortalOntologyId;
        this.ontologyDisplayName = ontologyDisplayName;
        this.ontologyAbbreviation = ontologyAbbreviation;
        this.ontologyDescription = ontologyDescription;
        this.contactName = contactName;
        this.contactEmailAddress = contactEmailAddress;
        this.versionNumber = versionNumber;
        this.homepageLink = homepageLink;
        this.documentationLink = documentationLink;
        this.publicationLink = publicationLink;
    }

    /**
     * Private constructor for serialization purposes.
     */
    private PublishToBioPortalInfo() {

    }

    public String getContactName() {
        return contactName;
    }

    public String getContactEmailAddress() {
        return contactEmailAddress;
    }

    public BioPortalUserId getUserId() {
        return userId;
    }

    public String getDisplayLabel() {
        return ontologyDisplayName;
    }
    
    public String getOntologyAbbreviation() {
        return ontologyAbbreviation;
    }


    public String getVersionNumber() {
        return versionNumber;
    }
    
    public String getOntologyDescription() {
        return ontologyDescription;
    }

    public String getHomepageLink() {
        return homepageLink;
    }

    public String getDocumentationLink() {
        return documentationLink;
    }

    public String getPublicationLink() {
        return publicationLink;
    }

    public BioPortalOntologyId getBioPortalOntologyId() {
        return ontologyId;
    }
}
