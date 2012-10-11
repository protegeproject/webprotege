package edu.stanford.bmir.protege.web.server.owlapi.notes.api;

import org.semanticweb.owlapi.model.IRI;

import java.util.EnumSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/08/2012
 */
public interface DiscussionThreadStore {

    Set<IRI> getIRIsWithThreads();

    int getIRIsWithThreadsCount();

    int getNoteCount();


    DiscussionThread getDiscussionThread(IRI inReplyToIRI);

    DiscussionThread getDiscussionThread(InReplyToId inReplyToId);
    




    Set<Note> getRepliesToId(ReplyTreatment replyTreatment, InReplyToId inReplyToId);

    Set<Note> getRepliesToIRI(ReplyTreatment replyTreatment, IRI resourceIRI);

    Set<Note> getRepliesToNoteId(ReplyTreatment replyTreatment, NoteId noteId);



    int getRepliesToIdCount(ReplyTreatment replyTreatment, InReplyToId inReplyToId);

    int getRepliesIRICount(ReplyTreatment replyTreatment, IRI resourceIRI);

    int getRepliesToNoteIdCount(ReplyTreatment replyTreatment, NoteId noteId);



    Set<Note> getMatchingNotes(Pattern searchPattern, NoteField... fieldsToSearch);

    Set<Note> getMatchingNotes(Pattern searchPattern, EnumSet<NoteField> fieldsToSearch);

    Set<Note> getNotesOfType(NoteType noteType);



    void addNote(Note note) throws CycleInDiscussionThreadException, UnknownNoteIdException;

    void removeNote(NoteId noteId) throws CannotRemoveNoteWithRepliesException;

    void removeThread(NoteId noteId);
}
