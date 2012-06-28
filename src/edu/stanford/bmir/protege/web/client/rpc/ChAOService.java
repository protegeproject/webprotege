package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * A service for ontology changes and annotations.
 * <p />
 *
 * @author Csongor Nyulas <csongor.nyulas@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 */
@RemoteServiceRelativePath("chao")
public interface ChAOService extends RemoteService {

    /*
     * Notes methods
     */

    public List<NotesData> getNotes(String projectName, String entityName, boolean ontologyLevelNotes);

    public List<NotesData> getNotes(String projectName, String entityName, boolean ontologyLevelNotes, boolean topLevelNotesOnly);

    public Collection<EntityData> getAvailableNoteTypes(String projectName);

    public NotesData createNote(String projectName, NotesData newNote, boolean topLevel);

    public void deleteNote(String projectName, String noteId);

    public Boolean archiveNote(String projectName, String nodeId, boolean archive);

    public NotesData editNote(String projectName, NotesData note , String noteId);

    public List<NotesData> getReplies(String projectName, String noteId, boolean topLevelOnly);

    /*
     * Changes
     */
    public ChangeData createChange(String projectName, String userName, String entityName, final String action, final String context);

    public PaginationData<ChangeData> getChanges(String projectName, Date startDate, Date endDate, int start, int limit,
            String sort, String dir);

    public PaginationData<ChangeData> getChanges(String projectName, String entityName, int start, int limit, String sort,
            String dir);

    public Collection<ChangeData> getChanges(String projectName, Date start, Date end);

    public Integer getNumChanges(String projectName, Date start, Date end);

    public Collection<ChangeData> getChanges(String projectName, String entityName);

    /*
     * Watched entities
     */

    public PaginationData<ChangeData> getWatchedEntities(String projectName, String userName, int start, int limit,
            String sort, String dir);

    public Collection<ChangeData> getWatchedEntities(String projectName, String userName);

    public EntityData addWatchedEntity(String projectName, String userName, String watchedEntityName);

    public void removeAllWatches(String projectName, String userName, String watchedEntityName);

    public EntityData addWatchedBranch(String projectName, String userName, String watchedEntityName);

    /*
     * Reviews
     */

    public List<String> getReviewers(String projectName);

    public Collection<ReviewData> getReviews(String projectName, String entityName);

    public void requestReview(String projectName, String entityName, List<String> reviewerNames);

    public void addReview(String projectName, String userName, NotesData data);


}
