package edu.stanford.bmir.protege.web.client.rpc;

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
public interface ChAOServiceAsync {

    /*
     * Notes methods
     */

    void getNotes(String projectName, String entityName, boolean topLevel, AsyncCallback<List<NotesData>> cb);

    void getNotes(String projectName, String entityName, boolean ontologyLevelNotes, boolean topLevelNotesOnly, AsyncCallback<List<NotesData>> cb);

    void getAvailableNoteTypes(String projectName, AsyncCallback<Collection<EntityData>> cb);

    void createNote(String projectName, NotesData newNote, boolean topLevel, AsyncCallback<NotesData> cb);

    void deleteNote(String projectName, String noteId, AsyncCallback<Void> callback);

    void getReplies(String projectName, String noteId, boolean topLevelOnly, AsyncCallback<List<NotesData>> cb);

    void editNote(String projectName, NotesData note, String noteId, AsyncCallback<NotesData> cb);

    void archiveNote(String projectName, String nodeId, boolean archive, AsyncCallback<Boolean> cb);

    /*
     * Change methods
     */

    void createChange(String projectName, String userName, String entityName, String action, String context, AsyncCallback<ChangeData> async);

    void getChanges(String projectName, Date startDate, Date endDate, int start, int limit, String sort, String dir, AsyncCallback <PaginationData<ChangeData>> cb);

    void getChanges(String projectName, String entityName, int start, int limit, String sort, String dir, AsyncCallback<PaginationData<ChangeData>> cb);

    void getChanges(String projectName, Date start, Date end, AsyncCallback<Collection<ChangeData>> cb);

    void getNumChanges(String projectName, Date start, Date end, AsyncCallback<Integer> cb);

    void getChanges(String projectName, String entityName, AsyncCallback<Collection<ChangeData>> cb);

    /*
     * Watched entities
     */

	void getWatchedEntities(String projectName, String userName, int start, int limit, String sort, String dir, AsyncCallback<PaginationData<ChangeData>> cb);

    void getWatchedEntities(String projectName, String userName, AsyncCallback<Collection<ChangeData>> cb);

    void addWatchedEntity(String projectName, String userName, String watchedEntityName, AsyncCallback<EntityData> cb);

    void removeAllWatches(String projectName, String userName, String watchedEntityName, AsyncCallback<Void> cb);

    void addWatchedBranch(String projectName, String userName, String watchedEntityName, AsyncCallback<EntityData> cb);

    /*
     * Reviews
     */

    void getReviewers(String projectName, AsyncCallback<List<String>> cb);

    void getReviews(String projectName, String entityName, AsyncCallback<Collection<ReviewData>> cb);

    void requestReview(String projectName, String entityName, List<String> reviewerNames, AsyncCallback<Void> cb);

    void addReview(String projectName, String userName, NotesData data, AsyncCallback<Void> cb);

}
