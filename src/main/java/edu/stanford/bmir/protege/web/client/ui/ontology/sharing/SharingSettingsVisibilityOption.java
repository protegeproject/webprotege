package edu.stanford.bmir.protege.web.client.ui.ontology.sharing;

import edu.stanford.bmir.protege.web.shared.sharing.SharingSetting;

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

    PUBLIC("Public", "Appears in list of ontologies.  No sign-in required.", SharingSetting.VIEW, SharingSetting.COMMENT, SharingSetting.EDIT),

    PRIVATE("Private",  "Does not appear in the list of ontologies.  Sign-in required.", SharingSetting.NONE);

    
    private String settingTitle;
    
    private String settingDescription;
    
    private Set<SharingSetting> sharingSettings = new HashSet<SharingSetting>();

    private SharingSettingsVisibilityOption(String settingTitle, String settingDescription, SharingSetting... sharingSettings) {
        this.settingTitle = settingTitle;
        this.settingDescription = settingDescription;
        this.sharingSettings.addAll(Arrays.asList(sharingSettings));
    }

    public String getSettingTitle() {
        return settingTitle;
    }

    public String getSettingDescription() {
        return settingDescription;
    }

    public boolean isForSharingSetting(SharingSetting sharingSetting) {
        return sharingSettings.contains(sharingSetting);
    }

    public Set<SharingSetting> getSharingSettings() {
        return sharingSettings;
    }
}
