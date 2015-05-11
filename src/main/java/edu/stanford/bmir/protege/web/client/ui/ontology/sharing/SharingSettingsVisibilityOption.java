package edu.stanford.bmir.protege.web.client.ui.ontology.sharing;

import edu.stanford.bmir.protege.web.shared.sharing.SharingPermission;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/02/2012
 */
public enum SharingSettingsVisibilityOption {

    PUBLIC("Public", "Appears in list of ontologies.  No sign-in required.", SharingPermission.VIEW, SharingPermission.COMMENT, SharingPermission.EDIT),

    PRIVATE("Private",  "Does not appear in the list of ontologies.  Sign-in required.", SharingPermission.NONE);

    
    private String settingTitle;
    
    private String settingDescription;
    
    private Set<SharingPermission> sharingPermissions = new HashSet<SharingPermission>();

    private SharingSettingsVisibilityOption(String settingTitle, String settingDescription, SharingPermission... sharingPermissions) {
        this.settingTitle = settingTitle;
        this.settingDescription = settingDescription;
        this.sharingPermissions.addAll(Arrays.asList(sharingPermissions));
    }

    public String getSettingTitle() {
        return settingTitle;
    }

    public String getSettingDescription() {
        return settingDescription;
    }

    public boolean isForSharingSetting(SharingPermission sharingPermission) {
        return sharingPermissions.contains(sharingPermission);
    }

    public Set<SharingPermission> getSharingPermissions() {
        return sharingPermissions;
    }
}
