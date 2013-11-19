package edu.stanford.bmir.protege.web.server.bioportal;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
import edu.stanford.bmir.protege.web.client.rpc.bioportal.BioPortalUpload;
import edu.stanford.bmir.protege.web.client.ui.ontology.search.BioPortalConstants;
import edu.stanford.bmir.protege.web.server.URLUtil;


public class BioPortalUploadImpl implements BioPortalUpload {


    public String uploadOntologyFromURL(String bpRestBase, String downloadLocation, String displayLabel, String userId, String format,
                                        String dateReleased, String contactName, String contactEmail, String abbreviation, String versionNumber,
                                        String homepage, String documentation, String publication, String viewingRestriction, String useracl,
                                        String description, String categories, String synonymSlot, String preferredNameSlot,
                                        String documentationSlot, String authorSlot) {

        //could be handled in separate ifs
        if (downloadLocation == null ||
                displayLabel == null ||
                format == null ||
                dateReleased == null ||
                contactName == null ||
                contactEmail == null) {
            throw new IllegalArgumentException("One of the required arguments for the BioPortal Upload is null: " +
                    "downloadLocation = " + downloadLocation + ", displayLabel = " + displayLabel + ", format = " + format +
                    ", dateReleased = " + dateReleased + ", contactName = " + contactName + ", contactEmail = " + contactEmail);
        }

        downloadLocation = URLUtil.encode(downloadLocation);
        displayLabel = URLUtil.encode(displayLabel);
        dateReleased = URLUtil.encode(dateReleased);
        contactName = URLUtil.encode(contactName);
        contactEmail = URLUtil.encode(contactEmail);
        abbreviation = URLUtil.encode(abbreviation);
        versionNumber = URLUtil.encode(versionNumber);
        homepage = URLUtil.encode(homepage);
        documentation = URLUtil.encode(documentation);
        publication = URLUtil.encode(publication);
        viewingRestriction = URLUtil.encode(viewingRestriction);
        useracl = URLUtil.encode(useracl);
        description = URLUtil.encode(description);
        categories = URLUtil.encode(categories);
        synonymSlot = URLUtil.encode(synonymSlot);
        preferredNameSlot = URLUtil.encode(preferredNameSlot);
        documentationSlot = URLUtil.encode(documentationSlot);
        authorSlot = URLUtil.encode(authorSlot);

        viewingRestriction = viewingRestriction == null ? "public" : viewingRestriction;

        StringBuffer params = new StringBuffer();
        addIfNotNull(params, "downloadLocation", downloadLocation);
        addIfNotNull(params, "displayLabel", displayLabel);
        addIfNotNull(params, "format", format);
        addIfNotNull(params, "dateReleased", dateReleased);
        addIfNotNull(params, "contactName", contactName);
        addIfNotNull(params, "contactEmail", contactEmail);
        addIfNotNull(params, "abbreviation", abbreviation);
        addIfNotNull(params, "versionNumber", versionNumber);
        addIfNotNull(params, "homepage", homepage);
        addIfNotNull(params, "documentation", documentation);
        addIfNotNull(params, "publication", publication);
        addIfNotNull(params, "viewingRestriction", viewingRestriction);
        addIfNotNull(params, "useracl", useracl);
        addIfNotNull(params, "description", description);
        addIfNotNull(params, "categoryIds", categories);
        addIfNotNull(params, "synonymSlot", synonymSlot);
        addIfNotNull(params, "preferredNameSlot", preferredNameSlot);
        addIfNotNull(params, "documentationSlot", documentationSlot);
        addIfNotNull(params, "authorSlot", authorSlot);

        //needed for URL ontologies
        addIfNotNull(params, "isRemote", "1");

        //TODO: Need to add also the userid. Currently uses the WebProtege userid, should be changed to use the real user id
//        userId = userId == null ? BioPortalConstants.DEFAULT_WEBPROTEGE_USERID : userId;
        addIfNotNull(params, "userId", userId);

        //TODO: should be configurable
        params.append(BioPortalConstants.DEFAULT_BIOPORTAL_REST_CALL_SUFFIX);

        StringBuffer url = new StringBuffer(bpRestBase == null ? BioPortalConstants.DEFAULT_BIOPORTAL_REST_BASE_URL : bpRestBase);
        url.append("ontologies");

        URLUtil.httpPost(url.toString(), params.toString());
        return null;
    }

    private void addIfNotNull(StringBuffer buffer, String param, Object value) {
        if (value == null) {
            return;
        }
        buffer.append(param);
        buffer.append("=");
        buffer.append(value.toString());
        buffer.append("&");
    }


    public static void main(String[] args) {
        BioPortalUploadImpl bpUploadService = new BioPortalUploadImpl();
        String response = bpUploadService.uploadOntologyFromURL("http://stagerest.bioontology.org/bioportal/",
                "http://smi-protege.stanford.edu/collab-protege/ont/TestUpload1.owl",
                "Test Upload 3", null, "OWL",
                "10/08/2012", "Tania Tudorache", "tudorache@stanford.edu",
                "UPLThree", "1.0", "http://ont.org", "http://doc.org",
                "http://publ.org", "public", null, "This is a test upload from WP", "2818,5060",
                null, null, null, null);

        System.out.println("POST response: " + response);
    }
}