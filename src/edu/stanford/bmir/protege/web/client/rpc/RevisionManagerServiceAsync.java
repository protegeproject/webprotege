package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;
import edu.stanford.bmir.protege.web.shared.revision.RevisionSummary;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.List;

public interface RevisionManagerServiceAsync {

    void getHeadRevisionNumber(ProjectId projectId, AsyncCallback<RevisionNumber> async);

    void getRevisionSummaries(ProjectId projectId, AsyncCallback<List<RevisionSummary>> async);
}
