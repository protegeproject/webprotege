package edu.stanford.bmir.protege.web.client.rpc.bioportal;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

import javax.servlet.Servlet;
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

    void uploadProjectToBioPortal(String projectDisplayName, ProjectId projectId, RevisionNumber revisionNumber, PublishToBioPortalInfo publishInfo) throws IOException;

    List<BioPortalOntologyInfo> getOwnedOntologies(BioPortalUserId bioPortalUserId) throws BioPortalAPIServiceException;

    BioPortalOntologyInfo getBioPortalOntologyInfoForDisplayName(String displayName) throws BioPortalAPIServiceException;
}
