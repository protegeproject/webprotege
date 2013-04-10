package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityLookupRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;

public interface EntityLookupServiceAsync {

    void lookupEntities(ProjectId projectId, EntityLookupRequest entityLookupRequest, AsyncCallback<List<EntityLookupServiceResult>> async);

}
