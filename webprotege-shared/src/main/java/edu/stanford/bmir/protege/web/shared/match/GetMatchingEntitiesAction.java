package edu.stanford.bmir.protege.web.shared.match;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

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

    private PageRequest pageRequest;

    public GetMatchingEntitiesAction(Criteria criteria,
                                     ProjectId projectId,
                                     PageRequest pageRequest) {
        this.criteria = checkNotNull(criteria);
        this.projectId = checkNotNull(projectId);
        this.pageRequest = checkNotNull(pageRequest);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public PageRequest getPageRequest() {
        return pageRequest;
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
                                                                @Nonnull Criteria criteria,
                                                                @Nonnull PageRequest pageRequest) {
        return new GetMatchingEntitiesAction(criteria, projectId, pageRequest);
    }
}
