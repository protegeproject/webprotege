package edu.stanford.bmir.protege.web.client.dispatch.actions;

import edu.stanford.bmir.protege.web.client.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.client.ui.frame.LabelledFrame;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.frame.Frame;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public abstract class UpdateFrameAction<F extends Frame<S>, S extends OWLEntity> extends AbstractHasProjectAction<Result> implements UpdateObjectAction<LabelledFrame<F>> {

    private LabelledFrame<F> from;

    private LabelledFrame<F> to;

    /**
     * For serialization purposes only
     */
    protected UpdateFrameAction() {
    }

    protected UpdateFrameAction(ProjectId projectId, LabelledFrame<F> from, LabelledFrame<F> to) {
        super(projectId);
        this.from = from;
        this.to = to;
    }

    @Override
    public LabelledFrame<F> getFrom() {
        return from;
    }

    @Override
    public LabelledFrame<F> getTo() {
        return to;
    }
}
