package edu.stanford.bmir.protege.web.server.owlapi.notes.api;

import edu.stanford.bmir.protege.web.client.rpc.data.UserId;

import java.io.Serializable;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/08/2012
 * <p>
 *     A Note is a reply to either a resource, identified with an {@link org.semanticweb.owlapi.model.IRI} or another
 *     {@link Note}.
 * </p>
 * <p>
 *     Each {@link Note} has a {@link NoteId} which uniquely identifies it, along with a timestamp which
 *     describes when the {@link Note} was created, a {@link UserId} which specified who created (or authored it),
 *     a subject, which summarises the intent behind the {@link Note} and a body, which is the main content of the
 *     {@link Note}.  Finally, each {@link Note} specified what it is a reply to with a {@link InReplyToId} object.
 * </p>
 */
public final class Note implements Serializable {

    private NoteId noteId;

    private InReplyToId inReplyToId;

    private UserId createdByUser;

    private long createdAt;

    private NoteType noteType;
    
    private String subject;
    
    private String body;

    private Note(NoteId noteId, InReplyToId inReplyToId, long createdAt, NoteType noteType, UserId createdByUser, String subject, String body) {
        this.noteId = noteId;
        this.inReplyToId = inReplyToId;
        this.createdByUser = createdByUser;
        this.createdAt = createdAt;
        this.noteType = noteType;
        this.subject = subject;
        this.body = body;
    }

    /**
     * Default constructor for serialization purposes.
     */
    private Note() {
    }
    
    public static Note createNote(NoteId noteId, InReplyToId inReplyTo, long createdAt, UserId createdByUser, NoteType noteType, String subject, String body) {
        return new Note(noteId, inReplyTo, createdAt, noteType, createdByUser, subject, body);
    }
    
    public static Note createComment(NoteId noteId, InReplyToId inReplyTo, long createdAt, UserId createdBy, String subject, String body) {
        return createNote(noteId, inReplyTo, createdAt, createdBy, NoteType.getComment(), subject, body);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////
    //////
    //////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Gets the identifier for this Note.
     * @return The NoteId for this Note.  Not <code>null</code>.
     */
    public NoteId getNoteId() {
        return noteId;
    }

    /**
     * Gets the identifier that this Note is a reply to.  This either identifies a resource, i.e. IRI, or it identifies
     * another Note.
     * @return The id that this Note is a reply to.
     */
    public InReplyToId getInReplyToId() {
        return inReplyToId;
    }

    public boolean isReplyToIRI() {
        return inReplyToId.isIRI();
    }

    public boolean isReplyToNote() {
        return inReplyToId.isNoteId();
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public UserId getCreatedByUser() {
        return createdByUser;
    }
    
    public String getCreatedByUserName() {
        return createdByUser.getUserName();
    }

    public NoteType getNoteType() {
        return noteType;
    }
    
    
    public String getSubject() {
        return subject;
    }
    
    public String getBody() {
        return body;
    }
    
    public String getFieldValueAsString(NoteFieldId fieldId, String defaultValue) {
        return "";
    }

    public Integer getFieldValueAsInteger(NoteFieldId fieldId, Integer defaultValue) {
        return 0;
    }

    public Long getFieldValueAsLong(NoteFieldId fieldId, Long defaultValue) {
        return 0l;
    }



}
