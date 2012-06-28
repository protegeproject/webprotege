package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.client.rpc.data.*;

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

    public void getNotes(String projectName, String entityName, boolean topLevel, AsyncCallback<List<NotesData>> cb) {
        proxy.getNotes(projectName, entityName, topLevel, cb);
    }

    public void getNotes(String projectName, String entityName, boolean ontologyLevelNotes, boolean topLevelNotesOnly, AsyncCallback<List<NotesData>> cb){
        proxy.getNotes(projectName, entityName, ontologyLevelNotes, topLevelNotesOnly, cb);
    }

    public void getAvailableNoteTypes(String projectName, AsyncCallback<Collection<EntityData>> cb) {
        proxy.getAvailableNoteTypes(projectName, cb);
    }

    public void createNote(String projectName, NotesData newNote, boolean topLevel, AsyncCallback<NotesData> cb) {
        proxy.createNote(projectName, newNote, topLevel, cb);
    }

    public void deleteNote(String projectName, String noteId, AsyncCallback<Void> callback) {
        proxy.deleteNote(projectName, noteId, callback);
    }

    public void archiveNote(String projectName, String nodeId, Boolean archive, AsyncCallback<Boolean> cb) {
        proxy.archiveNote(projectName,nodeId, archive, cb);
    }

    public void editNote(String projectName, NotesData note, String noteId, AsyncCallback<NotesData> cb) {
        proxy.editNote(projectName, note, noteId, cb);

    }

    public void getReplies(String projectName, String noteId, boolean topLevelOnly, AsyncCallback<List<NotesData>> cb){
        proxy.getReplies(projectName, noteId, topLevelOnly, cb);
    }

    /*
     * Change methods
     */

    public void createChange(String projectName, String userName, String entityName, String action, String context, AsyncCallback<ChangeData> cb) {
        proxy.createChange(projectName, userName, entityName, action, context, cb);
    }

    public void getChanges(String projectName, Date startDate, Date endDate, int start, int limit, String sort,
            String dir, AsyncCallback<PaginationData<ChangeData>> cb) {
        proxy.getChanges(projectName, startDate, endDate, start, limit, sort, dir, cb);
    }

    public void getChanges(String projectName, String entityName, int start, int limit, String sort, String dir,
            AsyncCallback<PaginationData<ChangeData>> cb) {
        proxy.getChanges(projectName, entityName, start, limit, sort, dir, cb);
    }

    public void getNumChanges(String projectName, Date start, Date end, AsyncCallback<Integer> cb) {
        proxy.getNumChanges(projectName, start, end, cb);
    }

    /*
     * Watched entities
     */

    public void getWatchedEntities(String projectName, String userName, int start, int limit, String sort, String dir,
            AsyncCallback<PaginationData<ChangeData>> cb) {

        proxy.getWatchedEntities(projectName, userName, start, limit, sort, dir, cb);
    }

    public void getWatchedEntities(String projectName, String userName, AsyncCallback<Collection<ChangeData>> cb) {
        proxy.getWatchedEntities(projectName, userName, cb);
    }

    public void addWatchedEntity(String projectName, String userName, String watchedEntityName,
            AsyncCallback<EntityData> cb) {
        proxy.addWatchedEntity(projectName, userName, watchedEntityName, cb);
    }

    public void addWatchedBranchEntity(String projectName, String userName, String watchedEntityName,
            AsyncCallback<EntityData> cb) {
        proxy.addWatchedBranch(projectName, userName, watchedEntityName, cb);
    }

    public void removeWatchedEntity(String projectName, String userName, String watchedEntityName,
            AsyncCallback<Void> cb) {
        proxy.removeAllWatches(projectName, userName, watchedEntityName, cb);
    }

    /*
     * Reviews
     */
    public void getReviewers(String projectName, AsyncCallback<List<String>> cb) {
        proxy.getReviewers(projectName, cb);
    }

    public void getReviews(String projectName, String entityName, AsyncCallback<Collection<ReviewData>> cb) {
        proxy.getReviews(projectName, entityName, cb);
    }

    public void requestReview(String projectName, String entityName, List<String> reviewerNames, AsyncCallback<Void> cb) {
        proxy.requestReview(projectName, entityName, reviewerNames, cb);
    }

    public void addReview(String projectName, String userName, NotesData data, AsyncCallback<Void> cb) {
        proxy.addReview(projectName, userName, data, cb);
    }


}
