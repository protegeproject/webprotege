package edu.stanford.bmir.protege.web.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.stanford.bmir.protege.web.client.rpc.bioportal.*;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionNumber;
import edu.stanford.bmir.protege.web.server.bioportal.BioPortalConfigurationManager;
import edu.stanford.bmir.protege.web.server.bioportal.BioPortalMetadataCache;
import edu.stanford.bmir.protege.web.server.bioportal.BioPortalRestAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class BioPortalAPIServiceImpl extends RemoteServiceServlet implements BioPortalAPIService {



    private BioPortalRestAPI getAPI() {
        String restBase = BioPortalConfigurationManager.getManager().getRestBase();
        return new BioPortalRestAPI(restBase);
    }

    public BioPortalUserInfo getBioPortalUserInfo(String bioportalAccountName, String bioportalPassword) throws CannotValidateBioPortalCredentials {
        BioPortalRestAPI api = getAPI();
        return api.getUserInfo(bioportalAccountName, bioportalPassword);
    }

    public void uploadProjectToBioPortal(ProjectId projectId, RevisionNumber revisionNumber, PublishToBioPortalInfo publishInfo) throws IOException {
        BioPortalRestAPI api = getAPI();
        api.uploadOntologyToBioPortal(projectId, revisionNumber, publishInfo);
    }



    public BioPortalOntologyInfo getBioPortalOntologyInfoForDisplayName(String displayName) {
        return BioPortalMetadataCache.getCache().getOntologyWithDisplayName(displayName);
    }

    public List<BioPortalOntologyInfo> getOwnedOntologies(BioPortalUserId bioPortalUserId) {
        List<BioPortalOntologyInfo> result = new ArrayList<BioPortalOntologyInfo>();
        List<BioPortalOntologyInfo> onts = BioPortalMetadataCache.getCache().getOntologies();
        for(BioPortalOntologyInfo info : onts) {
            if(info.getOwners().contains(bioPortalUserId)) {
                result.add(info);
            }
        }
        return result;
    }

}
