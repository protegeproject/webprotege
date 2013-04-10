package edu.stanford.bmir.protege.web.shared.event;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.notes.NoteTarget;
import edu.stanford.bmir.protege.web.shared.notes.NoteDetails;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 26/03/2013
 */
public class NotePostedEvent extends ProjectEvent<NotePostedHandler> {

    public transient static final Type<NotePostedHandler> TYPE = new Type<NotePostedHandler>();

    private NoteDetails noteDetails;

    private NoteTarget noteTarget;

    private OWLEntityData targetAsEntityData;

    public NotePostedEvent(ProjectId source, NoteTarget noteTarget, NoteDetails noteDetails) {
        super(source);
        this.noteDetails = noteDetails;
        this.noteTarget = noteTarget;
        // Internal
        this.targetAsEntityData = null;
    }

    public NotePostedEvent(ProjectId source, NoteTarget noteTarget, Optional<OWLEntityData> targetAsEntityData, NoteDetails noteDetails) {
        super(source);
        this.noteDetails = noteDetails;
        this.noteTarget = noteTarget;
        this.targetAsEntityData = targetAsEntityData.isPresent() ? targetAsEntityData.get() : null;
    }

    private NotePostedEvent() {
    }



    public NoteTarget getNoteTarget() {
        return noteTarget;
    }

    public Optional<OWLEntityData> getTargetAsEntityData() {
        return Optional.fromNullable(targetAsEntityData);
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
        return "NotePostedEvent".hashCode() + this.noteTarget.hashCode() + this.noteDetails.hashCode();
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
        return this.noteTarget.equals(other.noteTarget) && this.noteDetails.equals(other.noteDetails);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NotePostedEvent");
        sb.append("(");
        sb.append(noteTarget);
        sb.append(" ");
        sb.append(noteDetails);
        sb.append(")");
        return sb.toString();
    }



}
