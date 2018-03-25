package edu.stanford.bmir.protege.web.shared.tag;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Objects;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.color.Color;
import org.mongodb.morphia.annotations.*;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.tag.Tag.PROJECT_ID;
import static edu.stanford.bmir.protege.web.shared.tag.Tag.TAG_LABEL;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Mar 2018
 *
 * Represents a tag in a project.  Tags are used to tag entities with information that can be used for
 * development and project management.
 */
@Entity(value = "Tags", noClassnameStored = true)
@Indexes(
        {
                // Note, in addition to the unique indexes here, tag ids are globally unique
                // Labels are unique within a project
                @Index(fields = {@Field(PROJECT_ID), @Field(TAG_LABEL)}, options = @IndexOptions(unique = true))
        }
)
public class Tag implements IsSerializable {

    /**
     * The JSON field name for the project id.
     */
    public static final String PROJECT_ID = "projectId";

    /**
     * The JSON field name for the tag id.
     */
    public static final String TAG_ID = "tagId";

    /**
     * The JSON field name for the tag label.
     */
    public static final String TAG_LABEL = "label";

    @Id
    private TagId tagId;

    private ProjectId projectId;

    private String label;

    private String description;

    private Color color;

    private Color backgroundColor;

    /**
     * Creates a Tag.
     * @param tagId The tag id.
     * @param projectId The project id of the project that the tag belongs to.
     * @param label The label for the tag.  This must not be empty.
     * @param description An optional description for the tag.  This may be empty.
     * @param color A color for the tag.  This is the foreground color of the tag in the user interface.
     * @param backgroundColor A background color for the tag.  This is the background color of the tag in the user
     *                        interface.
     */
    @JsonCreator
    public Tag(@Nonnull TagId tagId,
               @Nonnull ProjectId projectId,
               @Nonnull String label,
               @Nonnull String description,
               @Nonnull Color color,
               @Nonnull Color backgroundColor) {
        this.tagId = checkNotNull(tagId);
        this.projectId = checkNotNull(projectId);
        this.label = checkNotNull(label);
        checkArgument(!label.isEmpty(), "Tag label cannot be empty");
        this.description = checkNotNull(description);
        this.color = checkNotNull(color);
        this.backgroundColor = checkNotNull(backgroundColor);
    }

    @GwtSerializationConstructor
    private Tag() {
    }

    /**
     * Gets the {@link TagId}
     */
    @Nonnull
    public TagId getTagId() {
        return tagId;
    }

    /**
     * Gets the project that this tag belongs to.
     */
    @Nonnull
    public ProjectId getProjectId() {
        return projectId;
    }

    /**
     * Gets a description for this tag.
     * @return The description, possibly empty.
     */
    @Nonnull
    public String getDescription() {
        return description;
    }

    /**
     * Gets the human readable name for the tag.
     */
    @Nonnull
    public String getLabel() {
        return label;
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
        return Objects.hashCode(tagId, projectId, label, description, color, backgroundColor);
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
                && this.projectId.equals(other.projectId)
                && this.label.equals(other.label)
                && this.description.equals(other.description)
                && this.color.equals(other.color)
                && this.backgroundColor.equals(other.backgroundColor);
    }


    @Override
    public String toString() {
        return toStringHelper("Tag")
                .addValue(tagId)
                .add("label", label)
                .add("color", color)
                .add("background-color", backgroundColor)
                .toString();
    }
}
