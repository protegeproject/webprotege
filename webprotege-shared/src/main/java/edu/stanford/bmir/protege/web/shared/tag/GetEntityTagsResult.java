package edu.stanford.bmir.protege.web.shared.tag;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2018
 */
public class GetEntityTagsResult implements Result {

    private List<Tag> entityTags;

    private List<Tag> projectTags;

    public GetEntityTagsResult(@Nonnull Collection<Tag> entityTags,
                               @Nonnull Collection<Tag> projectTags) {
        this.entityTags = new ArrayList<>(checkNotNull(entityTags));
        this.projectTags = new ArrayList<>(checkNotNull(projectTags));
    }

    @GwtSerializationConstructor
    private GetEntityTagsResult() {
    }

    /**
     * Gets the tags for the requested entity and project.
     */
    @Nonnull
    public List<Tag> getEntityTags() {
        return new ArrayList<>(entityTags);
    }

    /**
     * Gets the tags for the requested project.  These represent the available
     * entity tags.
     */
    @Nonnull
    public List<Tag> getProjectTags() {
        return new ArrayList<>(projectTags);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entityTags, projectTags);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetEntityTagsResult)) {
            return false;
        }
        GetEntityTagsResult other = (GetEntityTagsResult) obj;
        return this.entityTags.equals(other.entityTags)
                && this.projectTags.equals(other.projectTags);
    }


    @Override
    public String toString() {
        return toStringHelper("GetEntityTagsResult")
                .addValue(entityTags)
                .toString();
    }
}
