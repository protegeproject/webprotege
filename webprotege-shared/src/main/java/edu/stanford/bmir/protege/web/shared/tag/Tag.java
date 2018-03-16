package edu.stanford.bmir.protege.web.shared.tag;

import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.renderer.Color;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Mar 2018
 */
public class Tag implements IsSerializable {

    private TagId tagId;

    private String tagLabel;

    private Color color;

    private Color backgroundColor;

    public Tag(@Nonnull TagId tagId,
               @Nonnull String tagLabel,
               @Nonnull Color color,
               @Nonnull Color backgroundColor) {
        this.tagId = checkNotNull(tagId);
        this.tagLabel = checkNotNull(tagLabel);
        this.color = checkNotNull(color);
        this.backgroundColor = checkNotNull(backgroundColor);
    }

    /**
     * Gets the {@link TagId}
     */
    @Nonnull
    public TagId getTagId() {
        return tagId;
    }

    /**
     * Gets the human readable name for the tag.
     */
    @Nonnull
    public String getTagLabel() {
        return tagLabel;
    }

    /**
     * Gets the (foreground) color of the tag.
     * @return The color as a hexadecimal string
     */
    @Nonnull
    public Color getColor() {
        return color;
    }

    /**
     * Gets the background color of this tag.
     * @return The background color as a hexadecimal string.
     */
    @Nonnull
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tagId, tagLabel, color, backgroundColor);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Tag)) {
            return false;
        }
        Tag other = (Tag) obj;
        return this.tagId.equals(other.tagId)
                && this.tagLabel.equals(other.tagLabel)
                && this.color.equals(other.color)
                && this.backgroundColor.equals(other.backgroundColor);
    }


    @Override
    public String toString() {
        return toStringHelper("Tag")
                .addValue(tagId)
                .add("label", tagLabel)
                .add("color", color)
                .add("background-color", backgroundColor)
                .toString();
    }
}
