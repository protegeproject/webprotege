package edu.stanford.bmir.protege.web.shared.lang;

import com.google.common.base.MoreObjects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public class GetProjectLangTagsAction implements ProjectAction<GetProjectLangTagsResult> {

    private ProjectId projectId;

    public GetProjectLangTagsAction(@Nonnull ProjectId projectId) {
        this.projectId = checkNotNull(projectId);
    }

    @GwtSerializationConstructor
    private GetProjectLangTagsAction() {
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Override
    public int hashCode() {
        return projectId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof GetProjectLangTagsAction)) {
            return false;
        }
        GetProjectLangTagsAction other = (GetProjectLangTagsAction) obj;
        return this.projectId.equals(other.projectId);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetProjectLangTagsAction")
                .addValue(projectId)
                .toString();
    }
}
