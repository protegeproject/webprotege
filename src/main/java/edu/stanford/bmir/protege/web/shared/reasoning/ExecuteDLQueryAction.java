package edu.stanford.bmir.protege.web.shared.reasoning;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 06/09/2014
 */
public class ExecuteDLQueryAction implements Action<ExecuteDLQueryResult>, HasProjectId {

    private ProjectId projectId;

    private String enteredClassExpression;

    private Optional<String> filter;

    /**
     * For serialization purposes
     */
    private ExecuteDLQueryAction() {
    }

    public ExecuteDLQueryAction(ProjectId projectId, String enteredClassExpression, Optional<String> filter) {
        this.projectId = projectId;
        this.enteredClassExpression = enteredClassExpression;
        this.filter = filter;
    }

    public String getEnteredClassExpression() {
        return enteredClassExpression;
    }

    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    public Optional<String> getFilter() {
        return filter;
    }
}
