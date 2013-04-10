package edu.stanford.bmir.protege.web.server.notes;

import edu.stanford.bmir.protege.web.client.dispatch.actions.GetDiscussionThreadResult;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.NotesData;
import edu.stanford.bmir.protege.web.shared.notes.DiscussionThread;
import org.protege.notesapi.notes.NoteType;
import org.semanticweb.owlapi.model.OWLClass;
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

    NotesData addReplyToObjectId(String subject, String author, String body, NoteType noteType, String annotatedThingId);

    NotesData changeNoteContent(String noteId, String subject, String author, String body, NoteType noteType);

    void setArchivedStatus(String noteId, ArchivesStatus archivesStatus);

    int getDirectNotesCount(OWLEntity entity);

    int getIndirectNotesCount(OWLEntity entity);

    DiscussionThread getDiscusssionThread(OWLEntity targetEntity);
}
