package edu.stanford.bmir.protege.web.shared.reasoning;

import edu.stanford.bmir.protege.web.shared.event.ProjectEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.revision.RevisionNumber;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 23/09/2014
 */
public class ReasonerReadyEvent extends ProjectEvent<ReasonerReadyHandler> {

    public static final transient Type<ReasonerReadyHandler> TYPE = new Type<ReasonerReadyHandler>();

    private RevisionNumber revisionNumber;


    /**
     * For serialization purposes only
     */
    private ReasonerReadyEvent() {
    }

    public ReasonerReadyEvent(ProjectId source) {
        super(source);
    }

    @Override
    public Type<ReasonerReadyHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ReasonerReadyHandler handler) {
        handler.handleReasonerReady(this);
    }
}


