package edu.stanford.bmir.protege.web.shared.match;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Jun 2018
 */
//@AutoValue
//@GwtCompatible(serializable = true)
public class GetMatchingEntitiesAction implements ProjectAction<GetMatchingEntitiesResult> {

    private Criteria criteria;

    private ProjectId projectId;

    public GetMatchingEntitiesAction(Criteria criteria, ProjectId projectId) {
        this.criteria = criteria;
        this.projectId = projectId;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @GwtSerializationConstructor
    private GetMatchingEntitiesAction() {
    }

    @Nonnull
    public Criteria getCriteria() {
        return criteria;
    }

    @Nonnull
    public static GetMatchingEntitiesAction getMatchingEntities(@Nonnull ProjectId projectId,
                                                                @Nonnull Criteria criteria) {
        return new GetMatchingEntitiesAction(criteria, projectId);
    }
}
