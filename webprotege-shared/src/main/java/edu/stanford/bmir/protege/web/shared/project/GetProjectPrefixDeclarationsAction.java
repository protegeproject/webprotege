package edu.stanford.bmir.protege.web.shared.project;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27 Feb 2018
 */
public class GetProjectPrefixDeclarationsAction implements ProjectAction<GetProjectPrefixDeclarationsResult> {

    private String projectId;

    public GetProjectPrefixDeclarationsAction(@Nonnull ProjectId projectId) {
        this.projectId = checkNotNull(projectId.getId());
    }

    @GwtSerializationConstructor
    private GetProjectPrefixDeclarationsAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return ProjectId.get(projectId);
    }

    @Override
    public int hashCode() {
        return projectId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetProjectPrefixDeclarationsAction)) {
            return false;
        }
        GetProjectPrefixDeclarationsAction other = (GetProjectPrefixDeclarationsAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return toStringHelper("GetProjectPrefixDeclarationsAction")
                .addValue(projectId)
                .toString();
    }
}
