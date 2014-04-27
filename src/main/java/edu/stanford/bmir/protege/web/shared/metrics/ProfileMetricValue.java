package edu.stanford.bmir.protege.web.shared.metrics;

import edu.stanford.bmir.protege.web.shared.metrics.MetricValue;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 26/04/2014
 */
public class ProfileMetricValue extends MetricValue {

    private String profileName;

    private boolean inProfile;

    private ProfileMetricValue() {
    }

    public ProfileMetricValue(String profileName, boolean inProfile) {
        super(profileName, getBrowserText(inProfile), false);
        this.profileName = profileName;
        this.inProfile = inProfile;
    }

    private static String getBrowserText(boolean inProfile) {
        return inProfile ? "<span style=\"color: green;\">Yes</span>" : "<span style=\"color: orange;\">No</span>";
    }

    public String getProfileName() {
        return profileName;
    }

    public boolean isInProfile() {
        return inProfile;
    }
}
