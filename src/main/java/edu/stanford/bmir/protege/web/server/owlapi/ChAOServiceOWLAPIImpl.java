package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.ChAOService;
import edu.stanford.bmir.protege.web.client.rpc.data.ChangeData;
import edu.stanford.bmir.protege.web.client.rpc.data.PaginationData;
import edu.stanford.bmir.protege.web.server.PaginationServerUtil;
import edu.stanford.bmir.protege.web.server.WebProtegeRemoteServiceServlet;
import edu.stanford.bmir.protege.web.server.owlapi.change.OWLAPIChangeManager;
import edu.stanford.bmir.protege.web.server.owlapi.change.RevisionType;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/04/2012
 */
public class ChAOServiceOWLAPIImpl extends WebProtegeRemoteServiceServlet implements ChAOService {

    private OWLAPIProject getProject(String projectName) {
        ProjectId projectId = ProjectId.get(projectName);
        return getProject(projectId);
    }

    private OWLAPIProject getProject(ProjectId projectId) {
        OWLAPIProjectManager pm = OWLAPIProjectManager.getProjectManager();
        return pm.getProject(projectId);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////
    //////
    //////  Changes Stuff
    //////
    //////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public PaginationData<ChangeData> getChanges(String projectName, Date startDate, Date endDate, int start, int limit, String sort, String dir) {
        List<ChangeData> changes = getChanges(projectName, startDate, endDate);
        return PaginationServerUtil.pagedRecords(changes, start, limit, sort, dir);
    }

    public PaginationData<ChangeData> getChanges(String projectName, String entityName, int start, int limit, String sort, String dir) {
        List<ChangeData> changes = getChanges(projectName, entityName);
        return PaginationServerUtil.pagedRecords(changes, start, limit, sort, dir);
    }

    public List<ChangeData> getChanges(String projectName, Date start, Date end) {
        OWLAPIProject project = getProject(projectName);
        OWLAPIChangeManager changeManager = project.getChangeManager();
        return changeManager.getChangeDataInTimestampInterval(start.getTime(), end.getTime(), RevisionType.EDIT);
    }

    public Integer getNumChanges(String projectName, Date start, Date end) {
        OWLAPIProject project = getProject(projectName);
        OWLAPIChangeManager changeManager = project.getChangeManager();
        long fromTimestamp = start.getTime();
        long toTimestamp = end.getTime();
        return changeManager.getChangeSetCount(fromTimestamp, toTimestamp);
    }

    public List<ChangeData> getChanges(String projectName, String entityName) {
        OWLAPIProject project = getProject(projectName);
        OWLAPIChangeManager changeManager = project.getChangeManager();
        RenderingManager rm = project.getRenderingManager();
        Set<OWLEntity> entities = rm.getEntities(entityName);
        return changeManager.getChangeDataForEntitiesInTimeStampInterval(entities, 0, Long.MAX_VALUE);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////
    //////
    //////  Watched Entities
    //////
    //////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
     * Watched entities
     */

    public PaginationData<ChangeData> getWatchedEntities(String projectName, String userName, int start, int limit, String sort, String dir) {
        OWLAPIProject project = getProject(projectName);
        OWLAPIChangeManager changeManager = project.getChangeManager();
        final UserId userId = UserId.getUserId(userName);
        Set<Watch<?>> watches = project.getWatchManager().getWatches(userId);
        List<ChangeData> data = changeManager.getChangeDataForWatches(watches);
        return PaginationServerUtil.pagedRecords(data, start, limit, sort, dir);
    }

}
