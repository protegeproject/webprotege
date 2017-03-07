package edu.stanford.bmir.protege.web.shared.notes;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMultimap;

import java.io.Serializable;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/03/2013
 */
@Deprecated
public final class DiscussionThread implements Serializable {

    /**
     * A map which maps {@link NoteId}s to {@link Note}s corresponding to the replies to the {@link NoteId}s.
     * The distinguished {@link com.google.common.base.Optional#absent()} value represents root notes which are not
     * replies to other notes.
     */
    private ImmutableMultimap<Optional<NoteId>, Note> replyToMap;

    /**
     * For serialization purposes only
     */
    private DiscussionThread() {
    }

    /**
     * Gets the size of this discussion thread.
     * @return The size of this thread.  This corresponds to the number of notes in the thread
     */
    public int size() {
        return replyToMap.values().size();
    }

    /**
     * Constructs a discussion thread from the specified set of {@link Note}s. The discussion thread is constructed
     * based on the reply to ids of each {@link Note}.  Root notes are those notes for which the reply to id is
     * an absent optional value (i.e. {@link com.google.common.base.Optional#absent()}).
     * @param notes The set of {@link Note} objects from which to construct a discussion thread.
     * @throws NullPointerException if {@code notes} is {@code null}.
     */
    public DiscussionThread(Set<Note> notes) {
        checkNotNull(notes, "notes must not be null");
        ImmutableMultimap.Builder<Optional<NoteId>, Note> builder = ImmutableMultimap.builder();
        for(Note note : notes) {
            final Optional<NoteId> inReplyTo = note.getInReplyTo();
            builder.put(inReplyTo, note);
        }
        replyToMap = builder.build();
    }

    /**
     * Gets all of the {@link Note}s in this {@link DiscussionThread}.
     * @return A {@link Set} of {@link Note} objects which are contained in this discussion thread.  Not {@code null}.
     */
    public Set<Note> getNotes() {
        return new HashSet<Note>(replyToMap.values());
    }

    /**
     * Gets all of the {@link NoteId} of the notes that appear in this {@link DiscussionThread}.
     * @return A {@link Set} of {@link NoteId} objects that identify all of the notes that appear in this thread.
     * Not {@code null}.
     */
    public Set<NoteId> getNoteIds() {
        Set<NoteId> result = new HashSet<NoteId>();
        for(Optional<NoteId> noteId : replyToMap.keys()) {
            if(noteId.isPresent()) {
                result.add(noteId.get());
            }
        }
        return result;
    }

    /**
     * Gets the root {@link Note}s in this thread.  Root notes are those notes which have a reply to id corresponding
     * to {@link com.google.common.base.Optional#absent()}.
     * @return A (possibly empty) set of {@link Note} objects representing the root notes.  Not {@code null}.
     */
    public Set<Note> getRootNotes() {
        return new HashSet<Note>(replyToMap.get(Optional.<NoteId>absent()));
    }


    /**
     * Gets the replies (if any) to the {@link Note} identified by the specified {@link NoteId}.
     * @param noteId The {@link NoteId}.  Not {@code null}.
     * @return A (possibly empty) set of {@link Note} objects whose reply to id corresponds to the specified {@link NoteId}.
     * Not {@code null}.
     */
    public Set<Note> getReplies(NoteId noteId) {
        List<Note> sortedNotes = new ArrayList<Note>(replyToMap.get(Optional.of(noteId)));
        Collections.sort(sortedNotes, new Comparator<Note>() {
            @Override
            public int compare(Note o1, Note o2) {
                return o1.getHeader().compareTo(o2.getHeader());
            }
        });
        return new LinkedHashSet<Note>(sortedNotes);
    }

    /**
     * Determines whether or not the specified note, identified by the specified {@link NoteId} has some replies.
     * @param noteId The {@link NoteId} to check for.  Not {@code null}.
     * @return {@code true} if the note identified by {@code NoteId} has one or more replies, otherwise {@code false}
     */
    public boolean hasReplies(NoteId noteId) {
        return !replyToMap.get(Optional.of(noteId)).isEmpty();
    }



    @Override
    public int hashCode() {
        return "DiscussionThread".hashCode() + replyToMap.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof DiscussionThread)) {
            return false;
        }
        DiscussionThread other = (DiscussionThread) obj;
        return this.replyToMap.equals(other.replyToMap);
    }
}
