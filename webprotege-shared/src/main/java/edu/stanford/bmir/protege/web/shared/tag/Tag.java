package edu.stanford.bmir.protege.web.shared.tag;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.gwt.user.client.rpc.IsSerializable;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.match.criteria.RootCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.WithProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static edu.stanford.bmir.protege.web.shared.DeserializationUtil.nonNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Mar 2018
 *
 * Represents a tag in a project.  Tags are used to tag entities with information that can be used for
 * development and project management.
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class Tag implements IsSerializable, WithProjectId<Tag> {


    public static final String ID = "_id";

    public static final String PROJECT_ID = "projectId";

    public static final String LABEL = "label";

    private static final String DESCRIPTION = "description";

    private static final String COLOR = "color";

    private static final String BACKGROUND_COLOR = "backgroundColor";

    private static final String CRITERIA = "criteria";

    /**
     * Creates a Tag.
     *
     * @param tagId           The tag id.
     * @param projectId       The project id of the project that the tag belongs to.
     * @param label           The label for the tag.  This must not be empty.
     * @param description     An optional description for the tag.  This may be empty.
     * @param color           A color for the tag.  This is the foreground color of the tag in the user interface.
     * @param backgroundColor A background color for the tag.  This is the background color of the tag in the user
     *                        interface.
     */
    @JsonCreator
    public static Tag get(@Nonnull @JsonProperty(ID) TagId tagId,
                          @Nonnull @JsonProperty(PROJECT_ID) ProjectId projectId,
                          @Nonnull @JsonProperty(LABEL) String label,
                          @Nonnull @JsonProperty(DESCRIPTION) String description,
                          @Nonnull @JsonProperty(COLOR) Color color,
                          @Nonnull @JsonProperty(BACKGROUND_COLOR) Color backgroundColor,
                          @Nullable @JsonProperty(CRITERIA) List<RootCriteria> criteria) {
        checkArgument(!label.isEmpty(), "Tag label cannot be empty");
        ImmutableList<RootCriteria> rootCriteria;
        if(criteria != null) {
            rootCriteria = ImmutableList.copyOf(criteria);
        }
        else {
            rootCriteria = ImmutableList.of();
        }
        return new AutoValue_Tag(tagId, projectId, nonNull(label), nonNull(description), color, backgroundColor, rootCriteria);
    }

    /**
     * Gets the {@link TagId}
     */
    @JsonProperty(ID)
    @Nonnull
    public abstract TagId getTagId();

    /**
     * Gets the project that this tag belongs to.
     */
    @JsonProperty(PROJECT_ID)
    @Nonnull
    public abstract ProjectId getProjectId();

    /**
     * Gets the human readable name for the tag.
     */
    @JsonProperty(LABEL)
    @Nonnull
    public abstract String getLabel();


    /**
     * Gets a description for this tag.
     *
     * @return The description, possibly empty.
     */
    @JsonProperty(DESCRIPTION)
    @Nonnull
    public abstract String getDescription();

    /**
     * Gets the (foreground) color of the tag.
     *
     * @return The color as a hexadecimal string
     */
    @JsonProperty(COLOR)
    @Nonnull
    public abstract Color getColor();

    /**
     * Gets the background color of this tag.
     *
     * @return The background color as a hexadecimal string.
     */
    @JsonProperty(BACKGROUND_COLOR)
    @Nonnull
    public abstract Color getBackgroundColor();

    @JsonProperty(CRITERIA)
    @Nonnull
    public abstract ImmutableList<RootCriteria> getCriteria();

    @Override
    public Tag withProjectId(@Nonnull ProjectId projectId) {
        return Tag.get(getTagId(),
                       projectId,
                       getLabel(),
                       getDescription(),
                       getColor(),
                       getBackgroundColor(),
                       getCriteria());
    }
}
