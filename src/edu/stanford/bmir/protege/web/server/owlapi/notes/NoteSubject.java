package edu.stanford.bmir.protege.web.server.owlapi.notes;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 10/04/2012
 */
public class NoteSubject {

    private String subject;

    public NoteSubject() {
        this("");
    }

    public NoteSubject(String subject) {
        if(subject == null) {
            throw new NullPointerException("subject must not be null");
        }
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public int hashCode() {
        return 7 * subject.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof NoteSubject)) {
            return false;
        }
        NoteSubject other = (NoteSubject) obj;
        return other.getSubject().equals(subject);
    }
}
