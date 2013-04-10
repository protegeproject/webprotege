package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Csongor Nyulas <csongor.nyulas@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
public class ChAOServiceManager {

    private static ChAOServiceAsync proxy;
    static ChAOServiceManager instance;

    private ChAOServiceManager() {
        proxy = (ChAOServiceAsync) GWT.create(ChAOService.class);
    }

    public static ChAOServiceManager getInstance() {
        if (instance == null) {
            instance = new ChAOServiceManager();
        }
        return instance;
    }

    /*
     * Notes methods
     */

    public void getNotes(ProjectId projectId, String entityName, boolean topLevel, AsyncCallback<List<NotesData>> cb) {
        proxy.getNotes(projectId.getProjectName(), entityName, topLevel, cb);
    }

    public void getNotes(ProjectId projectId, String entityName, boolean ontologyLevelNotes, boolean topLevelNotesOnly, AsyncCallback<List<NotesData>> cb){
        proxy.getNotes(projectId.getProjectName(), entityName, ontologyLevelNotes, topLevelNotesOnly, cb);
    }

    public void getAvailableNoteTypes(ProjectId projectId, AsyncCallback<Collection<EntityData>> cb) {
        proxy.getAvailableNoteTypes(projectId.getProjectName(), cb);
    }

    public void createNote(ProjectId projectId, NotesData newNote, boolean topLevel, AsyncCallback<NotesData> cb) {
        proxy.createNote(projectId.getProjectName(), newNote, topLevel, cb);
    }

    public void deleteNote(ProjectId projectId, String noteId, AsyncCallback<Void> callback) {
        proxy.deleteNote(projectId.getProjectName(), noteId, callback);
    }

    public void archiveNote(ProjectId projectId, String nodeId, Boolean archive, AsyncCallback<Boolean> cb) {
        proxy.archiveNote(projectId.getProjectName(),nodeId, archive, cb);
    }

    public void editNote(ProjectId projectId, NotesData note, String noteId, AsyncCallback<NotesData> cb) {
        proxy.editNote(projectId.getProjectName(), note, noteId, cb);

    }

    public void getReplies(ProjectId projectId, String noteId, boolean topLevelOnly, AsyncCallback<List<NotesData>> cb){
        proxy.getReplies(projectId.getProjectName(), noteId, topLevelOnly, cb);
    }

    /*
     * Change methods
     */

    public void createChange(ProjectId projectId, String userName, String entityName, String action, String context, AsyncCallback<ChangeData> cb) {
        proxy.createChange(projectId.getProjectName(), userName, entityName, action, context, cb);
    }

    public void getChanges(ProjectId projectId, Date startDate, Date endDate, int start, int limit, String sort,
            String dir, AsyncCallback<PaginationData<ChangeData>> cb) {
        proxy.getChanges(projectId.getProjectName(), startDate, endDate, start, limit, sort, dir, cb);
    }

    public void getChanges(ProjectId projectId, String entityName, int start, int limit, String sort, String dir,
            AsyncCallback<PaginationData<ChangeData>> cb) {
        proxy.getChanges(projectId.getProjectName(), entityName, start, limit, sort, dir, cb);
    }

    public void getNumChanges(ProjectId projectId, Date start, Date end, AsyncCallback<Integer> cb) {
        proxy.getNumChanges(projectId.getProjectName(), start, end, cb);
    }

    /*
     * Watched entities
     */

    public void getWatchedEntities(ProjectId projectId, UserId userId, int start, int limit, String sort, String dir,
            AsyncCallback<PaginationData<ChangeData>> cb) {

        proxy.getWatchedEntities(projectId.getProjectName(), userId.getUserName(), start, limit, sort, dir, cb);
    }

    public void getWatchedEntities(ProjectId projectId, String userName, AsyncCallback<Collection<ChangeData>> cb) {
        proxy.getWatchedEntities(projectId.getProjectName(), userName, cb);
    }

//    public void addWatchedEntity(ProjectId projectId, String userName, String watchedEntityName,
//            AsyncCallback<EntityData> cb) {
//        proxy.addWatchedEntity(projectId.getProjectName(), userName, watchedEntityName, cb);
//    }

//    public void addWatchedBranchEntity(ProjectId projectId, String userName, String watchedEntityName,
//            AsyncCallback<EntityData> cb) {
//        proxy.addWatchedBranch(projectId.getProjectName(), userName, watchedEntityName, cb);
//    }

//    public void removeWatchedEntity(ProjectId projectId, String userName, String watchedEntityName,
//            AsyncCallback<Void> cb) {
//        proxy.removeAllWatches(projectId.getProjectName(), userName, watchedEntityName, cb);
//    }

    /*
     * Reviews
     */
    public void getReviewers(ProjectId projectId, AsyncCallback<List<String>> cb) {
        proxy.getReviewers(projectId.getProjectName(), cb);
    }

    public void getReviews(ProjectId projectId, String entityName, AsyncCallback<Collection<ReviewData>> cb) {
        proxy.getReviews(projectId.getProjectName(), entityName, cb);
    }

    public void requestReview(ProjectId projectId, String entityName, List<String> reviewerNames, AsyncCallback<Void> cb) {
        proxy.requestReview(projectId.getProjectName(), entityName, reviewerNames, cb);
    }

    public void addReview(ProjectId projectId, UserId userId, NotesData data, AsyncCallback<Void> cb) {
        proxy.addReview(projectId.getProjectName(), userId.getUserName(), data, cb);
    }


}
