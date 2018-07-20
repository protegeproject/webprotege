package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public abstract class UpdateFrameAction<F extends Frame<S>, S extends OWLEntityData> extends AbstractHasProjectAction<Result> implements UpdateObjectAction<F> {

    private F from;

    private F to;

    /**
     * For serialization purposes only
     */
    protected UpdateFrameAction() {
    }

    protected UpdateFrameAction(ProjectId projectId, F from, F to) {
        super(projectId);
        this.from = from;
        this.to = to;
    }

    @Override
    public F getFrom() {
        return from;
    }

    @Override
    public F getTo() {
        return to;
    }
}
