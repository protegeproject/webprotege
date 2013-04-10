package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionNumber;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionSummary;

import java.util.List;

public interface RevisionManagerServiceAsync {

    void getHeadRevisionNumber(ProjectId projectId, AsyncCallback<RevisionNumber> async);

    void getRevisionSummaries(ProjectId projectId, AsyncCallback<List<RevisionSummary>> async);
}
