package edu.stanford.bmir.protege.web.shared.issues;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 7 Mar 2017
 */
public class GetCommentedEntitiesAction implements ProjectAction<GetCommentedEntitiesResult> {

    private ProjectId projectId;

    private PageRequest pageRequest;

    private String userIdFilter;

    private SortingKey sortingKey;

    private Set<Status> statusFilter;

    @GwtSerializationConstructor
    private GetCommentedEntitiesAction() {
    }

    public GetCommentedEntitiesAction(@Nonnull ProjectId projectId,
                                      @Nonnull String userIdFilter,
                                      @Nonnull Set<Status> statusFilter,
                                      @Nonnull SortingKey sortingKey,
                                      @Nonnull PageRequest pageRequest) {
        this.projectId = projectId;
        this.userIdFilter = checkNotNull(userIdFilter);
        this.statusFilter = new HashSet<>(checkNotNull(statusFilter));
        this.sortingKey = checkNotNull(sortingKey);
        this.pageRequest = pageRequest;
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public SortingKey getSortingKey() {
        return sortingKey;
    }

    @Nonnull
    public PageRequest getPageRequest() {
        return pageRequest;
    }

    @Nonnull
    public String getUserIdFilter() {
        return userIdFilter;
    }

    @Nonnull
    public Set<Status> getStatusFilter() {
        return statusFilter;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, pageRequest);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetCommentedEntitiesAction)) {
            return false;
        }
        GetCommentedEntitiesAction other = (GetCommentedEntitiesAction) obj;
        return this.projectId.equals(other.projectId)
                && this.pageRequest.equals(other.pageRequest);
    }


    @Override
    public String toString() {
        return toStringHelper("GetCommentedEntitiesAction")
                .addValue(projectId)
                .addValue(sortingKey)
                .addValue(pageRequest)
                .toString();
    }
}
