package edu.stanford.bmir.protege.web.server.owlapi.notes.api;


import org.semanticweb.owlapi.model.IRI;

import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/08/2012
 * <p>
 *     An object which can store {@link Note} objects and can retrieve the replies to a {@link Note}.
 * </p>
 */
public interface NoteStore extends Iterable<Note> {

    int getNoteCount();

    boolean containsNote(NoteId noteId);

    /**
     * Gets the {@link Note} with the specified {@link NoteId}.
     * @param noteId The {@link NoteId} that specifies the {@link Note} to be retrieved.  Not <code>null</code>.
     * @return The {@link Note} with the specified {@link NoteId}, or <code>null</code> if the store does not contain
     * a {@link Note} that has the specified id.
     * @throws NullPointerException if {@link NoteId} is <code>null</code>.
     */
    Note getNote(NoteId noteId);

    InReplyToId getInReplyToId(NoteId noteId);


    boolean hasReplies(InReplyToId inReplyToId);

    List<Note> getReplies(InReplyToId inReplyToId);

    Set<IRI> getIRIsWithReplies();


    
    
    
}