package edu.stanford.bmir.protege.web.shared.sharing;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 27/02/2012
 */
public class ProjectSharingSettings implements Serializable {

    private ProjectId projectId;
    
    private List<UserSharingSetting> sharingSettings = new ArrayList<>();

    private SharingPermission defaultSharingPermission;

    /**
     * Default no-args constructor for GWT serialization purposes.
     */
    private ProjectSharingSettings() {
    }

    public ProjectSharingSettings(ProjectId projectId, SharingPermission defaultSharingPermission, List<UserSharingSetting> sharingSettings) {
        this.projectId = checkNotNull(projectId);
        this.defaultSharingPermission = checkNotNull(defaultSharingPermission);
        this.sharingSettings = new ArrayList<>(checkNotNull(sharingSettings));
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    public SharingPermission getDefaultSharingPermission() {
        return defaultSharingPermission;
    }

    public List<UserSharingSetting> getSharingSettings() {
        return new ArrayList<>(sharingSettings);
    }


    @Override
    public int hashCode() {
        return projectId.hashCode() + sharingSettings.hashCode() + defaultSharingPermission.hashCode();
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
        return other.projectId.equals(this.projectId) && other.defaultSharingPermission.equals(this.defaultSharingPermission) && other.sharingSettings.equals(this.sharingSettings);
    }


    @Override
    public String toString() {
        return Objects.toStringHelper("ProjectSharingSettings")
                .addValue(projectId)
                .addValue(defaultSharingPermission)
                .add("sharingSettings", sharingSettings)
                .toString();
    }
}
