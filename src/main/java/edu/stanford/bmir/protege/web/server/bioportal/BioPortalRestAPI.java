package edu.stanford.bmir.protege.web.server.bioportal;

import edu.stanford.bmir.protege.web.client.rpc.bioportal.*;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.client.ui.portlet.bioportal.imports.BioPortalConstants;
import edu.stanford.bmir.protege.web.server.rest.BioPortalUserInfoRestCall;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
public class BioPortalRestAPI {

    private String bioportalRestAPIBase = BioPortalConstants.DEFAULT_BIOPORTAL_REST_BASE_URL;

    public BioPortalRestAPI() {
    }

    public BioPortalRestAPI(String bioportalRestAPIBase) {
        this.bioportalRestAPIBase = bioportalRestAPIBase;
    }
    
    
    public BioPortalUserInfo getUserInfo(String username, String userpassword) throws CannotValidateBioPortalCredentials {
        try {
            BioPortalUserInfoRestCall call = new BioPortalUserInfoRestCall(bioportalRestAPIBase, username, userpassword);
            BioPortalUserInfoBean bean = call.doCallForObject();
            return new BioPortalUserInfo(bean.getUserName(), bean.getFirstName(), bean.getLastName(), BioPortalUserId.createFromId(bean.getId()), bean.getEmail());

        }
        catch (IOException e) {
            throw new CannotValidateBioPortalCredentials();
        }
    }

    public void uploadOntologyToBioPortal(ProjectId projectId, RevisionNumber revisionNumber, PublishToBioPortalInfo publishInfo) throws IOException {
        try {
            BioPortalUploader uploader = new BioPortalUploader(projectId, revisionNumber, publishInfo);
            uploader.uploadToBioPortal(bioportalRestAPIBase);
        }
        catch (IOException e) {
            throw new IOException(e);
        }
        catch (OWLOntologyStorageException e) {
            throw new IOException(e);
        }
    }


    public List<BioPortalOntologyInfo> getOntologies() {

        List<BioPortalOntologyInfo> result = new ArrayList<BioPortalOntologyInfo>();
        try {
            BioPortalGetOntologiesRestCall call = new BioPortalGetOntologiesRestCall(bioportalRestAPIBase);
            BioPortalOntologiesList list = call.doCallForObject();
            for(BioPortalOntologyInfoBean bean : list.getOntologies()) {
                List<BioPortalUserId> owners = new ArrayList<BioPortalUserId>();
                for(Integer ownerIntId : bean.getOwners()) {
                    owners.add(BioPortalUserId.createFromId(ownerIntId));
                }
                BioPortalOntologyInfo info = new BioPortalOntologyInfo(BioPortalOntologyId.getId(bean.getOntologyId()), bean.getDisplayLabel(), bean.getAbrreviation(), bean.getVersionNumber(), bean.getDescription(), owners);
                result.add(info);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public List<BioPortalOntologyInfo> getOntologiesForUser(BioPortalUserId userId) {
        List<BioPortalOntologyInfo> result = new ArrayList<BioPortalOntologyInfo>();
        List<BioPortalOntologyInfo> allOntologies = getOntologies();
        for(BioPortalOntologyInfo info : allOntologies) {
            if(info.getOwners().contains(userId)) {
                result.add(info);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        BioPortalRestAPI api = new BioPortalRestAPI("http://stagerest.bioontology.org/bioportal/");
        List<BioPortalOntologyInfo> infos = api.getOntologiesForUser(BioPortalUserId.createFromId(39918));
        BioPortalUserInfo userInfo = api.getUserInfo("matthewhorridge", "mhorridge");
        System.out.println(userInfo);
        for(BioPortalOntologyInfo info : infos) {
            System.out.println(info);
        }
    }


}
