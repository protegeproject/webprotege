package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionNumber;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionSummary;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;

import java.util.List;

public interface RevisionManagerServiceAsync {

    void getHeadRevisionNumber(ProjectId projectId, AsyncCallback<RevisionNumber> async);

    void getRevisionSummaries(ProjectId projectId, AsyncCallback<List<RevisionSummary>> async);
}
