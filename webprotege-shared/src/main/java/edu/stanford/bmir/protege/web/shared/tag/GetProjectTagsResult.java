package edu.stanford.bmir.protege.web.shared.tag;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;
import java.util.*;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2018
 */
public class GetProjectTagsResult implements Result {

    private List<Tag> tags;

    private Map<TagId, Integer> tagUsage;

    public GetProjectTagsResult(@Nonnull Collection<Tag> tags,
                                @Nonnull Map<TagId, Integer> tagUsage) {
        this.tags = new ArrayList<>(checkNotNull(tags));
        this.tagUsage = new HashMap<>(tagUsage);
    }

    @GwtSerializationConstructor
    private GetProjectTagsResult() {
    }

    @Nonnull
    public List<Tag> getTags() {
        return new ArrayList<>(tags);
    }

    @Nonnull
    public Map<TagId, Integer> getTagUsage() {
        return new HashMap<>(tagUsage);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tags, tagUsage);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GetProjectTagsResult)) {
            return false;
        }
        GetProjectTagsResult other = (GetProjectTagsResult) obj;
        return this.tags.equals(other.tags)
                && this.tagUsage.equals(other.tagUsage);
    }


    @Override
    public String toString() {
        return toStringHelper("GetProjectTagsResult")
                .addValue(tags)
                .add("usage", tagUsage)
                .toString();
    }
}
