package edu.stanford.bmir.protege.web.server.owlapi.notes.impl;

import edu.stanford.bmir.protege.web.server.owlapi.notes.api.InReplyToId;
import edu.stanford.bmir.protege.web.server.owlapi.notes.api.Note;
import edu.stanford.bmir.protege.web.server.owlapi.notes.api.NoteId;
import edu.stanford.bmir.protege.web.server.owlapi.notes.api.NoteStore;
import org.semanticweb.owlapi.model.IRI;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/08/2012
 */
public class NoteStoreIndex {

    private NoteStore noteStore;

    private Map<InReplyToId, Set<Note>> repliesById = new HashMap<InReplyToId, Set<Note>>();

    private Map<NoteId, Note> notesById = new HashMap<NoteId, Note>();

    private Set<IRI> inReplyToIRIs = new HashSet<IRI>();

    public NoteStoreIndex(NoteStore noteStore) {
        this.noteStore = noteStore;
        rebuildIndex();
    }

    private void rebuildIndex() {
        for(Note note : noteStore) {
            addNoteToIndexes(note);
        }
    }

    public void addNote(Note note) {
        addNoteToIndexes(note);
    }

    public void removeNote(Note note) {
        removeNoteFromIndexes(note);
    }


    public int getNoteCount() {
        return notesById.size();
    }

    public boolean containsNote(NoteId noteId) {
        return notesById.containsKey(noteId);
    }

    private void addNoteToIndexes(Note note) {
        InReplyToId inReplyToId = note.getInReplyToId();
        Set<Note> siblingNotes = repliesById.get(inReplyToId);
        if(siblingNotes == null) {
            siblingNotes = new HashSet<Note>(2);
            repliesById.put(inReplyToId, siblingNotes);
        }
        siblingNotes.add(note);

        if(inReplyToId.isIRI()) {
            inReplyToIRIs.add(IRI.create(inReplyToId.toIRI()));
        }

        notesById.put(note.getNoteId(), note);
    }

    private void removeNoteFromIndexes(Note note) {
        InReplyToId inReplyToId = note.getInReplyToId();
        Set<Note> siblingNotes = repliesById.get(inReplyToId);
        if(siblingNotes == null) {
            return;
        }
        siblingNotes.remove(note);
        if(siblingNotes.isEmpty()) {
            repliesById.remove(inReplyToId);
        }

        if(inReplyToId.isIRI()) {
            inReplyToIRIs.remove(IRI.create(inReplyToId.toIRI()));
        }

        notesById.remove(note.getNoteId());

//        leafNoteIds.remove(note.getNoteId());
    }

//    private void buildReplyCountCache() {
//        for(NoteId leafNoteId : leafNoteIds) {
//            updateInReplyToCount(leafNoteId);
//        }
//    }

//    private void updateInReplyToCount(NoteId leafNoteId) {
//        NoteId currentNoteId = leafNoteId;
//        while(true) {
//            InReplyToId inReplyToId = noteStore.getInReplyToId(currentNoteId);
//            if(inReplyToId == null) {
//                break;
//            }
//            incrementReplyCount(inReplyToId);
//            if(noteStore.isReplyToIRI(currentNoteId)) {
//                break;
//            }
//            currentNoteId = inReplyToId.toNoteId();
//        }
//    }
//
//    private void incrementReplyCount(InReplyToId inReplyToId) {
//        Integer count = indirectRepliesCountById.get(inReplyToId);
//        if(count == null) {
//            count = 0;
//        }
//        count++;
//        indirectRepliesCountById.put(inReplyToId, count);
//    }
//
//    private void decrementReplyCount(InReplyToId inReplyToId) {
//        Integer count = indirectRepliesCountById.get(inReplyToId);
//        if (count == null) {
//            return;
//        }
//        count--;
//        indirectRepliesCountById.put(inReplyToId, count);
//    }

//    private void rebuildLeafNotesCache() {
//        leafNoteIds.clear();
//        for(NoteId noteId : notesById.keySet()) {
//            Set<Note> repliesToNote = repliesById.get(InReplyToId.createFromNoteId(noteId));
//            if(repliesToNote == null) {
//                // Leaf note
//                leafNoteIds.add(noteId);
//            }
//        }
//    }

//    public boolean isLeafNote(NoteId noteId) {
//        return repliesById.get(InReplyToId.createFromNoteId(noteId)) == null;
//    }

    public Note getNote(NoteId noteId) {
        return notesById.get(noteId);
    }

    public List<Note> getReplies(InReplyToId inReplyToId) {
        Set<Note> replies = repliesById.get(inReplyToId);
        if(replies == null) {
            return Collections.emptyList();
        }
        return new ArrayList<Note>(replies);
    }

    public boolean hasReplies(InReplyToId inReplyToId) {
        return repliesById.get(inReplyToId) != null;
    }


    public Set<IRI> getIRIsWithReplies() {
        return new HashSet<IRI>(inReplyToIRIs);
    }

    public int getRepliesToIRIsCount() {
        return inReplyToIRIs.size();
    }
}
