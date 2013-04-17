package edu.stanford.bmir.protege.web.server.notes;

import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.NotesData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.shared.notes.*;
import org.protege.notesapi.notes.NoteType;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/04/2012
 */
public interface OWLAPINotesManager {
    
    List<EntityData> getAvailableNoteTypes();

    List<NotesData> getRepliesForObjectId(String objectId);
    
    List<NotesData> getDirectRepliesForObjectId(String objectId);
    
    List<NotesData> deleteNoteAndRepliesForObjectId(String objectId);

    void deleteNoteAndReplies(NoteId noteId);

    Note addReplyToNote(NoteId inReplyToId, NoteContent replyContent, UserId author);

    Note addNoteToEntity(OWLEntity targetEntity, NoteContent noteContent, UserId author);

    NotesData addReplyToObjectId(String subject, String author, String body, NoteType noteType, String annotatedThingId);

    NotesData changeNoteContent(String noteId, String subject, String author, String body, NoteType noteType);

    void setArchivedStatus(String noteId, ArchivesStatus archivesStatus);


    void setNoteStatus(NoteId noteId, NoteStatus noteStatus);

    int getDirectNotesCount(OWLEntity entity);

    int getIndirectNotesCount(OWLEntity entity);

    DiscussionThread getDiscusssionThread(OWLEntity targetEntity);
}
