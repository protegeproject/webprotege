package edu.stanford.bmir.protege.web.server.owlapi.notes.api;

import edu.stanford.bmir.protege.web.server.owlapi.notes.*;
import org.semanticweb.owlapi.model.IRI;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2012
 *
 * <p>
 *     Represents a basic annotation/note which consists of a body of text along with some metadata, such as the
 *     author, the subject, type, creation time etc.
 * </p>
 * <p>
 *     Each note is identified by an IRI.
 * </p>
 */
public class Note {

    private IRI noteIRI;
    
    private IRI noteTypeIRI;

    private NoteTimeStamp timeStamp;

    private NoteAuthor author;

    private NoteSubject subject;

    private NoteBody body;

    public Note(IRI noteIRI, IRI noteTypeIRI, NoteTimeStamp timeStamp, NoteAuthor author, NoteSubject subject, NoteBody body) {
        this.noteIRI = noteIRI;
        this.noteTypeIRI = noteTypeIRI;
        this.timeStamp = timeStamp;
        this.author = author;
        this.subject = subject;
        this.body = body;
    }

    /**
     * Gets the IRI which identifies this note.
     * @return The Note IRI.
     */
    public IRI getNoteIRI() {
        return noteIRI;
    }

    /**
     * Gets the type of this note.  The IRI specifies the type.
     * @return An IRI that specifies the type of the note.
     */
    public IRI getNoteTypeIRI() {
        return noteTypeIRI;
    }

    public NoteTimeStamp getTimeStamp() {
        return timeStamp;
    }

    public NoteAuthor getAuthor() {
        return author;
    }

    public NoteSubject getSubject() {
        return subject;
    }

    public NoteBody getBody() {
        return body;
    }

    @Override
    public int hashCode() {
        return noteIRI.hashCode() + noteTypeIRI.hashCode() + timeStamp.hashCode() + author.hashCode() + subject.hashCode() + body.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Note)) {
            return false;
        }
        Note other = (Note) obj;
        return other.noteIRI.equals(this.noteIRI) && other.noteTypeIRI.equals(this.noteTypeIRI) && other.timeStamp.equals(this.timeStamp) && other.author.equals(this.author) && other.subject.equals(subject) && other.body.equals(this.body);
    }
}
