package edu.stanford.bmir.protege.web.shared.tag;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.color.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Mar 2018
 */
public class TagData implements IsSerializable {

    @Nullable
    private TagId tagId;

    private String label;

    private String description;

    private Color color;

    private Color backgroundColor;

    public TagData(@Nonnull Optional<TagId> tagId,
                   @Nonnull String label,
                   @Nonnull String description,
                   @Nonnull Color color,
                   @Nonnull Color backgroundColor) {
        this.tagId = tagId.orElse(null);
        this.label = checkNotNull(label);
        this.description = checkNotNull(description);
        this.color = checkNotNull(color);
        this.backgroundColor = checkNotNull(backgroundColor);
    }

    @GwtSerializationConstructor
    private TagData() {
    }

    @Nonnull
    public Optional<TagId> getTagId() {
        return Optional.ofNullable(tagId);
    }

    @Nonnull
    public String getLabel() {
        return label;
    }

    @Nonnull
    public String getDescription() {
        return description;
    }

    @Nonnull
    public Color getColor() {
        return color;
    }

    @Nonnull
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tagId, label, description, color, backgroundColor);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TagData)) {
            return false;
        }
        TagData other = (TagData) obj;
        return Objects.equal(this.tagId, other.tagId)
                && this.label.equals(other.label)
                && this.description.equals(other.description)
                && this.color.equals(other.color)
                && this.backgroundColor.equals(other.backgroundColor);
    }


    @Override
    public String toString() {
        return toStringHelper("TagData")
                .add("tagId", tagId)
                .add("label", label)
                .add("description", description)
                .add("color", color)
                .add("backgroundColor", backgroundColor)
                .toString();
    }
}
