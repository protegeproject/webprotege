package edu.stanford.bmir.protege.web.shared.tag;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.color.Color;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Mar 2018
 */
public class AddProjectTagAction implements ProjectAction<AddProjectTagResult> {

    private ProjectId projectId;

    private String label;

    private String description;

    private Color color;

    private Color backgroundColor;

    public AddProjectTagAction(@Nonnull ProjectId projectId,
                               @Nonnull String label,
                               @Nonnull String description,
                               @Nonnull Color color,
                               @Nonnull Color backgroundColor) {
        this.projectId = checkNotNull(projectId);
        this.label = checkNotNull(label);
        this.description = checkNotNull(description);
        this.color = checkNotNull(color);
        this.backgroundColor = checkNotNull(backgroundColor);
    }

    @GwtSerializationConstructor
    private AddProjectTagAction() {
    }

    /**
     * Creates an {@link AddProjectTagAction}.
     * @param projectId The project id which the tag should be added to.
     * @param label The label for the tag.  Must not be empty.
     * @param description The description for the tag.  May be empty.
     * @param color The color for the tag (foreground).
     * @param backgroundColor The background-color for the tag
     */
    @Nonnull
    public static AddProjectTagAction addProjectTag(@Nonnull ProjectId projectId,
                                                    @Nonnull String label,
                                                    @Nonnull String description,
                                                    @Nonnull Color color,
                                                    @Nonnull Color backgroundColor) {
        return new AddProjectTagAction(projectId,
                                                 label,
                                                 description,
                                       color, backgroundColor);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
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
        return Objects.hashCode(projectId, label, description, color, backgroundColor);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AddProjectTagAction)) {
            return false;
        }
        AddProjectTagAction other = (AddProjectTagAction) obj;
        return this.projectId.equals(other.projectId)
                && this.label.equals(other.label)
                && this.description.equals(other.description)
                && this.color.equals(other.color)
                && this.backgroundColor.equals(other.backgroundColor);
    }


    @Override
    public String toString() {
        return toStringHelper("AddProjectTagAction")
                .addValue(projectId)
                .add("label", label)
                .add("description", description)
                .add("color", color)
                .add("backgroundColor", backgroundColor)
                .toString();
    }
}
