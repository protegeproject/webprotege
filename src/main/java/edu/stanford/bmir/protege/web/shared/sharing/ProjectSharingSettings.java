package edu.stanford.bmir.protege.web.shared.sharing;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/02/2012
 */
public class ProjectSharingSettings implements Serializable {

    private ProjectId projectId;
    
    private List<SharingSetting> sharingSettings = new ArrayList<>();

    private Optional<SharingPermission> linkSharingPermission = Optional.absent();

    /**
     * Default no-args constructor for GWT serialization purposes.
     */
    private ProjectSharingSettings() {
    }

    public ProjectSharingSettings(ProjectId projectId, Optional<SharingPermission> linkSharingPermission, List<SharingSetting> sharingSettings) {
        this.projectId = checkNotNull(projectId);
        this.sharingSettings = new ArrayList<>(checkNotNull(sharingSettings));
        this.linkSharingPermission = checkNotNull(linkSharingPermission);
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public List<SharingSetting> getSharingSettings() {
        return new ArrayList<>(sharingSettings);
    }

    public Optional<SharingPermission> getLinkSharingPermission() {
        return linkSharingPermission;
    }

    @Override
    public int hashCode() {
        return projectId.hashCode() + sharingSettings.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof ProjectSharingSettings)) {
            return false;
        }
        ProjectSharingSettings other = (ProjectSharingSettings) obj;
        return other.projectId.equals(this.projectId) && other.sharingSettings.equals(this.sharingSettings);
    }


    @Override
    public String toString() {
        return toStringHelper("ProjectSharingSettings")
                .addValue(projectId)
                .addValue(sharingSettings)
                .toString();
    }
}
