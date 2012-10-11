package edu.stanford.bmir.protege.web.server.owlapi.notes.api;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 25/08/2012
 */
public class RemoveReplyTo extends ReplyToChange {

    public RemoveReplyTo(Note note) {
        super(note.getNoteId(), note.getInReplyToId());
    }

    @Override
    public int hashCode() {
        return RemoveReplyTo.class.getSimpleName().hashCode() + super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof RemoveReplyTo)) {
            return false;
        }
        RemoveReplyTo other = (RemoveReplyTo) obj;
        return super.equals(other);
    }
}
