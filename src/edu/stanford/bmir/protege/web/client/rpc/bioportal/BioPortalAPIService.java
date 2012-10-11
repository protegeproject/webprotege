package edu.stanford.bmir.protege.web.client.rpc.bioportal;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionNumber;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;

import java.io.IOException;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
@RemoteServiceRelativePath("bioportalapi")
public interface BioPortalAPIService extends RemoteService {

    BioPortalUserInfo getBioPortalUserInfo(String bioportalAccountName, String bioportalPassword) throws CannotValidateBioPortalCredentials;

    void uploadProjectToBioPortal(ProjectId projectId, RevisionNumber revisionNumber, BioPortalUploadInfo uploadInfo) throws IOException;

    List<BioPortalOntologyInfo> getOwnedOntologies(BioPortalUserId bioPortalUserId) throws BioPortalAPIServiceException;

    BioPortalOntologyInfo getBioPortalOntologyInfoForDisplayName(String displayName) throws BioPortalAPIServiceException;
}
