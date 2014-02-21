package edu.stanford.bmir.protege.web.client.rpc.bioportal;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("bpUpload")
public interface BioPortalUpload  extends RemoteService {

    /**
     * Upload ontology to BioPortal from a URL.
     * Required arguments: downloadLocation, displayLabel, userId, format, dateReleased, contactName, contactEmail
     *
     * Documentation for BioPortal Upload REST service: https://gist.github.com/a5b9f715a6a6416b52ff
     *
     *@param bpRestBase
     * @param downloadLocation
     * @param displayLabel
     * @param userId
     * @param format
     * @param dateReleased - in format: mm/dd/yyyy
     * @param contactName
     * @param contactEmail
     * @param abreviation
     * @param versionNumber
     * @param homepage
     * @param documentation
     * @param publication
     * @param viewingRestriction
     * @param useracl
     * @param description
     * @param categories
     * @param synonymSlot
     * @param preferredNameSlot
     * @param documentationSlot
     * @param authorSlot
     * @return String - the BioPortal response as XML
     */
    String uploadOntologyFromURL(String bpRestBase,
                                 String downloadLocation,
                                 String displayLabel, String userId, String format,
                                 String dateReleased, String contactName, String contactEmail,
                                 String abreviation, String versionNumber,
                                 String homepage, String documentation, String publication,
                                 String viewingRestriction, String useracl,
                                 String description, String categories,
                                 String synonymSlot, String preferredNameSlot, String documentationSlot, String authorSlot);

}
