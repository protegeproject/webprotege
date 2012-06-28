package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class NotesData implements Serializable {

    private static final long serialVersionUID = 1499115919382949498L;

    private String author = "";

    private String body = "";

    private String creationDate = "";

    private Date latestUpdate = null;

    private String type = "";

    private String subject = "";

    /**
     * This is the object that identifies this note.
     */
    private EntityData noteId = null;

    /**
     * This is the object that this note is a "reply" to.  It's not really an entity (in ontology terms) in all cases -
     * it's only an actual entity for "top level notes".
     */
    private EntityData repliedToObjectId = null;

    /**
     * A list of replies to this note.
     */
    private List<NotesData> replies = null;


    private boolean isArchived = false;

    private int numOfReplies = 0;

    public NotesData() {
    }

    // TODO: add annotation

    /**
     * Constructs a NotesData object which represents a note which is a "reply" to either an entity in the ontology or
     * another note.  Each note may have zero or more replies.  Each note is identified in the system by means of an
     * id, which is actually represented here as an EntityData object.  Each note has various fields such as author,
     * subject, body etc.
     * @param author The author of the note.  A string representing the name of the author.  Not null.
     * @param body The body of the note.  A string representing the body of the note.
     * @param creationDate The creation date of the note.  This should a formatted string which represents the timestamp
     * of when the note was created.
     * @param type The type of note.  A string (which really should be an IRI, but isn't) that specifies the type of note.
     * @param subject The subject of the note.  Not null.
     * @param nodeId The EntityData object which represents the identity of the note.
     * @param repliedToObjectId The object (either an entity or a note, but in both cases represented by entity data) which
     * this note "replies to".  Not null.
     * @param replies A list of replies.  Not null.
     */
    public NotesData(String author, String body, String creationDate, String type, String subject, EntityData nodeId, EntityData repliedToObjectId, List<NotesData> replies) {
        this.author = author;
        this.body = body;
        this.creationDate = creationDate;
        this.type = type;
        this.noteId = nodeId;
        this.replies = replies;
        this.subject = subject;
        this.repliedToObjectId = repliedToObjectId;
        this.latestUpdate = null;
    }

    public int getNumOfReplies() {
        return numOfReplies;
    }

    public void setNumOfReplies(int numOfReplies) {
        this.numOfReplies = numOfReplies;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }

    public void setLatestUpdate(Date d) {
        if (d != null) {
            this.latestUpdate = d;
        }
    }

    public Date getLatestUpdate() {
        return latestUpdate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(String date) {
        this.creationDate = date;
    }

    // TODO: add annotation
    public List<NotesData> getReplies() {
        return replies;
    }

    // TODO: add annotation
    public void setReplies(ArrayList<NotesData> replies) {
        this.replies = replies;
        if (replies != null) {
            this.numOfReplies = replies.size();
        }
    }

    public void addReply(NotesData reply) {
        if (this.replies == null) {
            this.replies = new ArrayList<NotesData>();
        }
        this.replies.add(reply);
        this.numOfReplies++;
    }

    public void addReply(int index, NotesData reply) {
        if (this.replies == null) {
            this.replies = new ArrayList<NotesData>();
        }
        if (index >= 0 && index < this.replies.size()) {
            this.replies.add(index, reply);
        }
        else {
            this.replies.add(0, reply);
        }
        this.numOfReplies++;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EntityData getAnnotatedEntity() {
        return repliedToObjectId;
    }

    public void setAnnotatedEntity(EntityData annotatedEntity) {
        this.repliedToObjectId = annotatedEntity;
    }

    /**
     * @deprecated Use {@link #getNoteId()}
     * @return See {@link #getNoteId()}.
     */
    @Deprecated
    public EntityData getEntity() {
        return noteId;
    }

    /**
     * @deprecated  Use the constructor!
     * @param entity The EntityData which represents the note Id.
     */
    @Deprecated
    public void setEntity(EntityData entity) {
        this.noteId = entity;
    }

    /**
     * Gets the object which identifies this note in the system.  MH:  It's very confusing to "fresh eyes" that this
     * is represented with an EntityData object.
     * @return The EntityData which represents the object that identifies this note.
     */
    public EntityData getNoteId() {
        return noteId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(author);
        buffer.append(" (");
        buffer.append(getCreationDate());
        buffer.append("), type: ");
        buffer.append(type);
        buffer.append(", body: ");
        buffer.append(body);
        return buffer.toString();
    }
}
