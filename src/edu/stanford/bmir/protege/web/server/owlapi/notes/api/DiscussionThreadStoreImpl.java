package edu.stanford.bmir.protege.web.server.owlapi.notes.api;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.NodeID;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 17/08/2012
 * <p>
 * Manages discussion threads for resources.  A discussion thread is a tree shaped structure of notes.  Each
 * node in the tree is essentially a NoteId and each child is a "reply" to the parent.  Each resource may have
 * several discussion threads associated with it.
 * </p>
 * <p>
 * Some terminology:
 * <ul>
 * <li>Resource - an object that is identifiable via an IRI.  Each resource can have several discussion threads
 * associated with it.
 * </li>
 * <li>
 * DiscussionThread - A tree shaped structure of one or more NoteId objects.
 * </li>
 * <li>
 * Thread starter - The root node of a DiscussionThread.  Note that the exact meaning of a thread starter depends
 * upon the context of its use.
 * </li>
 * </ul>
 * </p>
 * <p>
 * Notes in a discussion thread are identified via their NoteIds.  This allows the contents of notes in threads to
 * be edited whilst preserving the thread structure.
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
 */
public class DiscussionThreadStoreImpl implements DiscussionThreadStore {

    private static final DiscussionThreadStoreImpl instance;

    private MutableNoteStore noteStore;

    static {
        MutableNoteStore noteStore = null;//new NoteStoreImpl();
        instance = new DiscussionThreadStoreImpl(noteStore);
    }


    public static DiscussionThreadStoreImpl getManager() {
        return instance;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private DiscussionThreadStoreImpl(MutableNoteStore noteStore) {
        this.noteStore = noteStore;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private boolean isReplyToIRI(NoteId noteId) {
        InReplyToId inReplyToId = noteStore.getInReplyToId(noteId);
        return inReplyToId != null && inReplyToId.isIRI();
    }

    public DiscussionThread getDiscussionThread(InReplyToId inReplyToId) {
        Set<Note> replies = getRepliesToId(ReplyTreatment.DIRECT, inReplyToId);
        List<DiscussionThread> replyThreads = new ArrayList<DiscussionThread>();
        for (Note reply : replies) {
            DiscussionThread replyThread = getDiscussionThread(InReplyToId.createFromNoteId(reply.getNoteId()));
            replyThreads.add(replyThread);
        }
        return new DiscussionThread(inReplyToId, replyThreads);
    }

    public DiscussionThread getDiscussionThread(IRI inReplyToIRI) {
        return getDiscussionThread(InReplyToId.createFromIRI(inReplyToIRI.toString()));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Global operations


    public int getNoteCount() {
        return noteStore.getNoteCount();
    }

    public int getIRIsWithThreadsCount() {
        return noteStore.getIRIsWithReplies().size();
    }

    public Set<IRI> getIRIsWithThreads() {
        return noteStore.getIRIsWithReplies();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Reply retrieval

    public Set<Note> getRepliesToId(ReplyTreatment replyTreatment, InReplyToId inReplyToId) {
        List<Note> directReplies = noteStore.getReplies(inReplyToId);

        if (directReplies == null) {
            return Collections.emptySet();
        }

        if (replyTreatment == ReplyTreatment.DIRECT) {
            return new TreeSet<Note>(directReplies);
        }

        Set<Note> indirectReplies = new TreeSet<Note>();
        for (Note reply : directReplies) {
            InReplyToId replyId = InReplyToId.createFromNoteId(reply.getNoteId());
            indirectReplies.addAll(getRepliesToId(replyTreatment, replyId));
        }
        return indirectReplies;
    }

    public Set<Note> getRepliesToIRI(ReplyTreatment replyTreatment, IRI resourceIRI) {
        InReplyToId inReplyToId = InReplyToId.createFromIRI(resourceIRI.toString());
        return getRepliesToId(replyTreatment, inReplyToId);
    }

    public Set<Note> getRepliesToNoteId(ReplyTreatment replyTreatment, NoteId noteId) {
        InReplyToId inReplyToId = InReplyToId.createFromNoteId(noteId);
        return getRepliesToId(replyTreatment, inReplyToId);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Reply counting

    public int getRepliesToIdCount(ReplyTreatment replyTreatment, InReplyToId inReplyToId) {
        return getRepliesToId(replyTreatment, inReplyToId).size();
    }

    public int getRepliesIRICount(ReplyTreatment replyTreatment, IRI resourceIRI) {
        InReplyToId inReplyToId = InReplyToId.createFromIRI(resourceIRI.toString());
        return getRepliesToIdCount(replyTreatment, inReplyToId);
    }

    public int getRepliesToNoteIdCount(ReplyTreatment replyTreatment, NoteId noteId) {
        InReplyToId inReplyToId = InReplyToId.createFromNoteId(noteId);
        return getRepliesToIdCount(replyTreatment, inReplyToId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Searching

    public Set<Note> getMatchingNotes(Pattern searchPattern, NoteField... fieldsToSearch) {
        return Collections.emptySet();
    }

    public Set<Note> getMatchingNotes(Pattern searchPattern, EnumSet<NoteField> fieldsToSearch) {
        return Collections.emptySet();
    }

    public Set<Note> getNotesOfType(NoteType noteType) {
        return Collections.emptySet();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Mutation

    public void addNote(Note note) {
        checkForCycle(note);
        noteStore.addNote(note);
    }

    public void removeNote(NoteId noteId) {
        if (noteStore.hasReplies(InReplyToId.createFromNoteId(noteId))) {
            throw new CannotRemoveNoteWithRepliesException(noteId);
        }
        noteStore.removeNote(noteId);
    }

    public void removeThread(NoteId threadStarter) {
        InReplyToId inReplyToId  = InReplyToId.createFromNoteId(threadStarter);
        for(Note reply : noteStore.getReplies(inReplyToId)) {
            removeThread(reply.getNoteId());
        }
        removeNote(threadStarter);
    }

    private void checkForCycle(Note note) {
        NoteId currentNoteId = note.getNoteId();
        Set<NoteId> visited = new HashSet<NoteId>();
        while (true) {
            if (isReplyToIRI(currentNoteId)) {
                return;
            }
            if (visited.contains(currentNoteId)) {
                throw new CycleInDiscussionThreadException(note);
            }
            visited.add(currentNoteId);
            InReplyToId inReplyToId = noteStore.getInReplyToId(currentNoteId);
            if (inReplyToId == null) {
                return;
            }
            currentNoteId = inReplyToId.toNoteId();
        }
    }


}
