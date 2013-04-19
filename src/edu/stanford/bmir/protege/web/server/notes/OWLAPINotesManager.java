package edu.stanford.bmir.protege.web.server.notes;

import edu.stanford.bmir.protege.web.client.rpc.data.UserId;
import edu.stanford.bmir.protege.web.shared.notes.*;
import org.semanticweb.owlapi.model.OWLEntity;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/04/2012
 */
public interface OWLAPINotesManager {

    int getDirectNotesCount(OWLEntity entity);

    int getIndirectNotesCount(OWLEntity entity);

    DiscussionThread getDiscusssionThread(OWLEntity targetEntity);

    Note addNoteToEntity(OWLEntity targetEntity, NoteContent noteContent, UserId author);


    void deleteNoteAndReplies(NoteId noteId);

    Note addReplyToNote(NoteId inReplyToId, NoteContent replyContent, UserId author);

    void setNoteStatus(NoteId noteId, NoteStatus noteStatus);


}
