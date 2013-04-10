package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionNumber;
import edu.stanford.bmir.protege.web.client.rpc.data.RevisionSummary;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 07/10/2012
 */
@RemoteServiceRelativePath("revisionmanager")
public interface RevisionManagerService extends RemoteService {

    RevisionNumber getHeadRevisionNumber(ProjectId projectId);

    List<RevisionSummary> getRevisionSummaries(ProjectId projectId);
    

}
