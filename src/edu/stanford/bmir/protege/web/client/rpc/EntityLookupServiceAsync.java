package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityLookupRequest;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.primitive.VisualEntity;

import java.util.List;

public interface EntityLookupServiceAsync {

    void lookupEntities(ProjectId projectId, EntityLookupRequest entityLookupRequest, AsyncCallback<List<EntityLookupServiceResult>> async);
}
