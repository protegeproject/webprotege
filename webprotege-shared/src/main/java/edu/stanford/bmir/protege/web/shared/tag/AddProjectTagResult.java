package edu.stanford.bmir.protege.web.shared.tag;

import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2018
 */
public class AddProjectTagResult implements Result {

    @Nullable
    private Tag addedTag;

    public AddProjectTagResult(@Nonnull Tag addedTag) {
        this.addedTag = checkNotNull(addedTag);
    }

    @GwtSerializationConstructor
    public AddProjectTagResult() {
        this.addedTag = null;
    }

    /**
     * Gets the tag if it was added.
     * @return The tag.  If the tag was not added (likely due to a duplicate label, for example)
     * then the return value will be empty.
     */
    @Nonnull
    public Optional<Tag> getAddedTag() {
        return Optional.ofNullable(addedTag);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(addedTag);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AddProjectTagResult)) {
            return false;
        }
        AddProjectTagResult other = (AddProjectTagResult) obj;
        return Objects.equals(this.addedTag, other.addedTag);
    }


    @Override
    public String toString() {
        return toStringHelper("AddProjectTagResult")
                .addValue(addedTag)
                .toString();
    }
}
