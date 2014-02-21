package edu.stanford.bmir.protege.web.client.rpc.bioportal;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;

public interface BioPortalAPIServiceAsync {

    void getBioPortalUserInfo(String bioportalAccountName, String bioportalPassword, AsyncCallback<BioPortalUserInfo> async) throws CannotValidateBioPortalCredentials;

    void uploadProjectToBioPortal(ProjectId projectId, RevisionNumber revisionNumber, PublishToBioPortalInfo publishInfo, AsyncCallback<Void> async);

    void getBioPortalOntologyInfoForDisplayName(String displayName, AsyncCallback<BioPortalOntologyInfo> async);

    void getOwnedOntologies(BioPortalUserId bioPortalUserId, AsyncCallback<List<BioPortalOntologyInfo>> async);
}
