package edu.stanford.bmir.protege.web.shared.frame;

import edu.stanford.bmir.protege.web.shared.dispatch.AbstractHasProjectAction;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.dispatch.UpdateObjectAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/02/2013
 */
public abstract class UpdateFrameAction extends AbstractHasProjectAction<Result> {

    private PlainEntityFrame from;

    private PlainEntityFrame to;

    /**
     * For serialization purposes only
     */
    protected UpdateFrameAction() {
    }

    protected UpdateFrameAction(ProjectId projectId, PlainEntityFrame from, PlainEntityFrame to) {
        super(projectId);
        this.from = checkNotNull(from);
        this.to = checkNotNull(to);
    }

    public PlainEntityFrame getFrom() {
        return from;
    }

    public PlainEntityFrame getTo() {
        return to;
    }
}
