package edu.stanford.bmir.protege.web.shared.tag;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.annotations.GwtSerializationConstructor;
import edu.stanford.bmir.protege.web.shared.dispatch.ProjectAction;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Mar 2018
 */
public class SetProjectTagsAction implements ProjectAction<SetProjectTagsResult> {

    private ProjectId projectId;

    private List<TagData> tagData;

    public SetProjectTagsAction(@Nonnull ProjectId projectId,
                                @Nonnull List<TagData> tagData) {
        this.projectId = checkNotNull(projectId);
        this.tagData = new ArrayList<>(checkNotNull(tagData));
    }

    @GwtSerializationConstructor
    private SetProjectTagsAction() {
    }

    public static SetProjectTagsAction setProjectTags(@Nonnull ProjectId projectId,
                                                      @Nonnull List<TagData> tagData) {
        return new SetProjectTagsAction(projectId, tagData);
    }

    @Nonnull
    @Override
    public ProjectId getProjectId() {
        return projectId;
    }

    @Nonnull
    public List<TagData> getTagData() {
        return new ArrayList<>(tagData);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectId, tagData);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SetProjectTagsAction)) {
            return false;
        }
        SetProjectTagsAction other = (SetProjectTagsAction) obj;
        return this.projectId.equals(other.projectId)
                && this.tagData.equals(other.tagData);
    }


    @Override
    public String toString() {
        return toStringHelper("SetProjectTagsAction")
                .addValue(projectId)
                .addValue(tagData)
                .toString();
    }
}
