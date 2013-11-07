package edu.stanford.bmir.protege.web.shared.event;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.notes.NoteDetails;
import edu.stanford.bmir.protege.web.shared.notes.NoteId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public class NotePostedEvent extends ProjectEvent<NotePostedHandler> {

    public transient static final Type<NotePostedHandler> TYPE = new Type<NotePostedHandler>();

    private NoteDetails noteDetails;

    private Optional<NoteId> inReplyTo;

    private Optional<OWLEntityData> targetAsEntityData;

    public NotePostedEvent(ProjectId source, Optional<OWLEntityData> targetAsEntityData, NoteDetails noteDetails) {
        super(source);
        this.noteDetails = noteDetails;
        this.targetAsEntityData = targetAsEntityData;
        this.inReplyTo = Optional.absent();
    }

    public NotePostedEvent(ProjectId source, NoteDetails noteDetails, Optional<NoteId> inReplyTo) {
        super(source);
        this.noteDetails = noteDetails;
        this.inReplyTo = inReplyTo;
        this.targetAsEntityData = Optional.absent();
    }

    /**
     * For serialization only
     */
    private NotePostedEvent() {
    }

    public Optional<NoteId> getInReplyTo() {
        return inReplyTo;
    }

    public Optional<OWLEntityData> getTargetAsEntityData() {
        return targetAsEntityData;
    }

    public NoteDetails getNoteDetails() {
        return noteDetails;
    }

    @Override
    public Type<NotePostedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NotePostedHandler handler) {
        handler.handleNotePosted(this);
    }


    @Override
    public int hashCode() {
        return "NotePostedEvent".hashCode() + targetAsEntityData.hashCode() + inReplyTo.hashCode() + this.noteDetails.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof NotePostedEvent)) {
            return false;
        }
        NotePostedEvent other = (NotePostedEvent) obj;
        return this.targetAsEntityData.equals(other.targetAsEntityData) && this.inReplyTo.equals(other.inReplyTo)  && this.noteDetails.equals(other.noteDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NotePostedEvent");
        sb.append("( Target(");
        sb.append(targetAsEntityData);
        sb.append(") InReplyTo(");
        sb.append(inReplyTo);
        sb.append(") ");
        sb.append(noteDetails);
        sb.append(")");
        return sb.toString();
    }



}
