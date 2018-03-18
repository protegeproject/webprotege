package edu.stanford.bmir.protege.web.shared.tag;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2018
 */
public class GetEntityTagsResult implements Result {

    private List<Tag> tags;

    public GetEntityTagsResult(@Nonnull List<Tag> tags) {
        this.tags = new ArrayList<>(tags);
    }

    @GwtSerializationConstructor
    private GetEntityTagsResult() {
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
        if (!(obj instanceof GetEntityTagsResult)) {
            return false;
        }
        GetEntityTagsResult other = (GetEntityTagsResult) obj;
        return this.tags.equals(other.tags);
    }


    @Override
    public String toString() {
        return toStringHelper("GetEntityTagsResult")
                .addValue(tags)
                .toString();
    }
}
