package edu.stanford.bmir.protege.web.shared.lang;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public class GetProjectLangTagsResult implements Result {

    private ProjectId projectId;

    private ImmutableSet<LangTag> langTags;

    public GetProjectLangTagsResult(@Nonnull ProjectId projectId,
                                    @Nonnull ImmutableSet<LangTag> langTags) {
        this.projectId = checkNotNull(projectId);
        this.langTags = checkNotNull(langTags);
    }

    @GwtSerializationConstructor
    private GetProjectLangTagsResult() {
    }

    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public ImmutableSet<LangTag> getLangTags() {
        return langTags;
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, langTags);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof GetProjectLangTagsResult)) {
            return false;
        }
        GetProjectLangTagsResult other = (GetProjectLangTagsResult) obj;
        return this.projectId.equals(other.projectId) && this.langTags.equals(other.langTags);
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper("GetProjectLangTagsResult")
                .addValue(projectId)
                .add("langTags", langTags)
                .toString();
    }
}
