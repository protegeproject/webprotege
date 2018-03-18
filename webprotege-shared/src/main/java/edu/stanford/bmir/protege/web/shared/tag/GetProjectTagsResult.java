package edu.stanford.bmir.protege.web.shared.tag;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2018
 */
public class GetProjectTagsResult implements Result {

    private List<Tag> tags;

    public GetProjectTagsResult(@Nonnull List<Tag> tags) {
        this.tags = new ArrayList<>(checkNotNull(tags));
    }

    @GwtSerializationConstructor
    private GetProjectTagsResult() {
    }

    @Nonnull
    public List<Tag> getTags() {
        return new ArrayList<>(tags);
    }

    @Override
    public int hashCode() {
        return tags.hashCode();
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
        return this.tags.equals(other.tags);
    }


    @Override
    public String toString() {
        return toStringHelper("GetProjectTagsResult")
                .addValue(tags)
                .toString();
    }
}
