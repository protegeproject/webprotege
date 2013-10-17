package edu.stanford.bmir.protege.web.server.notes.api;

import edu.stanford.bmir.protege.web.shared.notes.*;
import edu.stanford.bmir.protege.web.shared.user.UserId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/08/2012
 *
 * <p>
 * Manages discussion threads for resources.  A discussion thread is a tree shaped structure of notes.  Each
 * node in the tree is essentially a {@link Note} and each child is a "reply" to the parent.  Each resource may have
 * several discussion threads associated with it.
 * </p>
 * <p>
 * Some terminology:
 * <ul>
 * <li>Resource - an object that is identifiable via an IRI.  Each resource can have several discussion threads
 * associated with it.
 * </li>
 * <li>
 * DiscussionThread - A set of tree shaped structures of one or more NoteId objects.
 * </li>
 * <li>
 * Thread starter - The root node of a DiscussionThread.  Note that the exact meaning of a thread starter depends
 * upon the context of its use.
 * </li>
 * </ul>
 * </p>
 * <p>
 * A NoteId essentially identifies the UNIQUE occurrence of a node in ALL discussion threads.  This means that
 * a NoteId may only appear ONCE in the whole set of discussion threads.  This enforces a tree structure.  Various
 * methods will throw exceptions if this rule would be violated upon mutation of the set of discussion threads.
 * </p>
 * <p>
 * The complete set of discussion threads is managed internally by the manager.  Some methods return DiscussionThread
 * objects.  These REPRESENT whole discussion threads or subtrees of discussion threads at a particular point in
 * time - they are immutable.
 * </p>
 *
 */
public interface NoteStore {

    /**
     * Gets the number of {@link Note}s in this store.
     * @return The number of notes.
     */
    int getNoteCount();

//    /**
//     * Determines whether or not this store contains a {@link Note} with the specified {@link NoteId}.
//     * @param noteId The {@link NoteId}.  Not {@code null}.
//     * @return {@code true} if this store contains a {@link Note} that has the specified {@link NoteId} otherwise
//     * {@code false}.
//     * @throws NullPointerException if {@code noteId} is {@code null}.
//     */
//    boolean containsNote(NoteId noteId);
//
//    /**
//     * Gets the {@link edu.stanford.bmir.protege.web.shared.notes.Note} with the specified {@link NoteId}.
//     * @param noteId The {@link NoteId} that specifies the {@link edu.stanford.bmir.protege.web.shared.notes.Note} to be retrieved.  Not {@code null}.
//     * @return An {@link Optional} for a {@link edu.stanford.bmir.protege.web.shared.notes.Note}.  If the note store contains a note with the specified {@link NoteId}
//     * then {@link com.google.common.base.Optional#get()} will return the {@link edu.stanford.bmir.protege.web.shared.notes.Note} with that {@link NoteId}.  If the
//     * note store does not contain a note with the specified {@link NoteId} then the {@link com.google.common.base.Optional#isPresent()}
//     * will return {@code false}.  Not {@code null}.
//     * a {@link edu.stanford.bmir.protege.web.shared.notes.Note} that has the specified id.
//     * @throws NullPointerException if {@link NoteId} is {@code null}.
//     */
//    Optional<Note> getNote(NoteId noteId);


//
//
//    int getReplyCount(NoteId noteId);
//

    int getNoteCount(NoteTarget target);

//    List<Note> getNotes(NoteTarget target);

    DiscussionThread getDiscussionThread(NoteTarget target);

//
//
//    List<Note> getNotes(NoteHeaderQuery query);
//
//    List<Note> getNotes(NoteContentQuery query);
//
//    List<Note> getNotes(NoteStoreQuery query);
//

//
//    void applyChange(NoteChange noteChange) throws NoteChangeException;

    Note addNote(UserId author, NoteContent noteContent);
//    Note addNote(UserId author, NoteContent noteContent);

    void addNote(Note note) throws NoteChangeException;

    void removeNote(NoteId note) throws NoteChangeException;



}