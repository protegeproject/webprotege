package edu.stanford.bmir.protege.web.server.owlapi;

import edu.stanford.bmir.protege.web.client.rpc.ChAOService;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.server.PaginationServerUtil;
import edu.stanford.bmir.protege.web.server.WebProtegeRemoteServiceServlet;
import edu.stanford.bmir.protege.web.server.notes.ArchivesStatus;
import edu.stanford.bmir.protege.web.server.notes.OWLAPINotesManager;
import edu.stanford.bmir.protege.web.server.owlapi.change.OWLAPIChangeManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.watches.Watch;
import org.protege.notesapi.notes.NoteType;
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
    //////  Notes Stuff
    //////
    //////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Retrieves the nots on the specified entity.
     * @param projectName The name of the relevant project
     * @param entityName The name of the entity.  This should be a full IRI, but this implmentation will tolerate
     * browser text.  Note I'm not entirely sure, but this could be simply an Id (i.e. a note id).
     * @param ontologyLevelNotes Not sure what this means.  Doesn't affect things for now.
     * @return A list of notes on the specified entity.  If the specified entity name does not correspond to any entity
     *         then the empty list will be returned.
     */
    public List<NotesData> getNotes(String projectName, String entityName, boolean ontologyLevelNotes) {
        OWLAPIProject project = getProject(projectName);
        OWLAPINotesManager nrm = project.getNotesManager();
        return nrm.getRepliesForObjectId(entityName);
    }

    public List<NotesData> getNotes(String projectName, String objectId, boolean ontologyLevelNotes, boolean topLevelNotesOnly) {
        // Not sure what the ontology level notes is
        // I think the top level Notes only means that the replies aren't returned (even though there may be replies).
        OWLAPIProject project = getProject(projectName);
        OWLAPINotesManager nm = project.getNotesManager();
        if (topLevelNotesOnly) {
            return nm.getDirectRepliesForObjectId(objectId);
        }
        else {
            return nm.getRepliesForObjectId(objectId);
        }
    }

    public List<NotesData> getReplies(String projectName, String noteId, boolean topLevelOnly) {
        OWLAPIProject project = getProject(projectName);
        OWLAPINotesManager nm = project.getNotesManager();
        if (topLevelOnly) {
            return nm.getDirectRepliesForObjectId(noteId);
        }
        else {
            return nm.getRepliesForObjectId(noteId);
        }
    }


    public Collection<EntityData> getAvailableNoteTypes(String projectName) {
        OWLAPIProject project = getProject(projectName);
        OWLAPINotesManager nrm = project.getNotesManager();
        return nrm.getAvailableNoteTypes();
    }

    /**
     * It's not totally clear what fields in the NotesData object are expected or required to be set.  The UI
     * doesn't bother to fill in some values (some don't necessarily make sense for the purposes of Note creation -
     * perhaps some different kind of object is needed here).
     * @param projectName The name of the relevant project.
     * @param newNote As far as I can tell, here's what gets filled in: {@link NotesData#author} a string corresponding
     * to the name of the logged in user.  Since the UI doesn't allow non-logged in changes to notes (well plugins could
     * circumvent this) we don't worry about this for the time being.
     * {@link NotesData#repliedToObjectId} the entity "name" that that the note annotates.  In the case of top level notes
     * on entities (i.e. notes that aren't replied to other notes) this is the IRI of the entity (this implementation
     * will also tolerate browser text though).  For replies to notes, this is the note id object.
     * {@link NotesData#body} the body of the note.  It appears that this is never null.  The empty string seems to represent
     * an empty body.
     * {@link NotesData#creationDate} this is set to the string value "time".  NB the notes API uses longs here.
     * {@link NotesData#noteId} = null.  This always seems to be null and I'm not sure what this value is actually for.
     * {@link NotesData#latestUpdate} = null this is set to null for new notes.
     * {@link NotesData#numOfReplies} = 0,
     * {@link NotesData#replies} = null,  The field is null rather than an empty list.  The empty list would be nicer!
     * {@link NotesData#subject} = "(no subject)" The subject of the note.  Never null.  If the user does not supply
     * a subject then the UI sets this to "(no subject)",
     * {@link NotesData#type} The note type, where type corresponds to one of the names of the {@link
     * EntityData} returned by the {@link #getAvailableNoteTypes(String)} note types method.  Some nicer scheme like
     * IRIs for note types would be better here I think.
     *
     * @param topLevel Not sure what this is for.  It always seems to be set to <code>false</code>.
     * @return The newly created NotesData (the real? notes data.  I think we could do with separate objects for
     * notes creation and representation of existing notes.).
     */
    public NotesData createNote(String projectName, NotesData newNote, boolean topLevel) {

        OWLAPIProject project = getProject(projectName);
        
        String suppliedAnnotatedEntityName = newNote.getAnnotatedEntity().getName();

        OWLAPINotesManager nrm = project.getNotesManager();

        String author = newNote.getAuthor();
        String subject = newNote.getSubject();
        String body = newNote.getBody();

        NoteType noteType = NoteType.valueOf(newNote.getType());
        return nrm.addReplyToObjectId(subject, author, body, noteType, suppliedAnnotatedEntityName);
    }



    public void deleteNote(String projectName, String noteId) {
        // Do we allow deletes if the note has replies?
        OWLAPIProject project = getProject(projectName);
        OWLAPINotesManager nm = project.getNotesManager();
        nm.deleteNoteAndRepliesForObjectId(noteId);
    }

    public Boolean archiveNote(String projectName, String nodeId, boolean archive) {
        OWLAPIProject project = getProject(projectName);
        OWLAPINotesManager nm = project.getNotesManager();
        if (archive) {
            nm.setArchivedStatus(nodeId, ArchivesStatus.ARCHIVED);
        }
        else {
            nm.setArchivedStatus(nodeId, ArchivesStatus.NOT_ARCHIVED);
        }
        return true;
    }

    public NotesData editNote(String projectName, NotesData note, String noteId) {
        OWLAPIProject project = getProject(projectName);
        OWLAPINotesManager nm = project.getNotesManager();
        String subject = note.getSubject();
        String author = note.getAuthor();
        String body = note.getBody();
        NoteType noteType = NoteType.valueOf(note.getType());
        return nm.changeNoteContent(noteId, subject, author, body, noteType);
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


    /*
     * Changes
     */
    public ChangeData createChange(String projectName, String userName, String entityName, final String action, final String context) {
        return null;
    }

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
        return changeManager.getChangeDataInTimestampInterval(start.getTime(), end.getTime());
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

    public Collection<ChangeData> getWatchedEntities(String projectName, String userName) {
        return Collections.emptyList();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////
    //////
    //////  Reviews
    //////
    //////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
     * Reviews
     */

    public List<String> getReviewers(String projectName) {
        return Collections.emptyList();
    }

    public Collection<ReviewData> getReviews(String projectName, String entityName) {
        return Collections.emptyList();
    }

    public void requestReview(String projectName, String entityName, List<String> reviewerNames) {
    }

    public void addReview(String projectName, String userName, NotesData data) {
    }


}
