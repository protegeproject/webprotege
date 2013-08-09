package edu.stanford.bmir.protege.web.shared.irigen.obo;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.irigen.SuffixSettingsId;
import edu.stanford.bmir.protege.web.shared.irigen.SuffixSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 30/07/2013
 */
public class OBOSuffixSettings extends SuffixSettings {

    public static final SuffixSettingsId Id = SuffixSettingsId.get("OBO", "Generate OBO style Id");


    public static final int DEFAULT_TOTAL_DIGITS = 7;

    private int totalDigits = DEFAULT_TOTAL_DIGITS;

    private Optional<String> labelLanguage;

    private List<UserIdRange> userIdRanges = new ArrayList<UserIdRange>();

    /**
     * For serialization purposes only.
     */
    private OBOSuffixSettings() {
    }

    public OBOSuffixSettings(List<UserIdRange> userIdRanges, Optional<String> labelLanguage) {
        this(DEFAULT_TOTAL_DIGITS, userIdRanges, labelLanguage);
    }

    public OBOSuffixSettings(int totalDigits, List<UserIdRange> userIdRanges, Optional<String> labelLanguage) {
        super(Id);
        this.totalDigits = totalDigits;
        this.userIdRanges.addAll(userIdRanges);
        this.labelLanguage = labelLanguage;
    }

    public int getTotalDigits() {
        return totalDigits;
    }

    public Optional<String> getLabelLanguage() {
        return labelLanguage;
    }

    public List<UserIdRange> getUserIdRanges() {
        return new ArrayList<UserIdRange>(userIdRanges);
    }

    @Override
    public int hashCode() {
        return "OBOIdSchemeSpecificSettings".hashCode() + getId().hashCode() + totalDigits;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof OBOSuffixSettings)) {
            return false;
        }
        OBOSuffixSettings other = (OBOSuffixSettings) obj;
        return this.getId().equals(other.getId()) && this.totalDigits == other.totalDigits;
    }

//    @Override
//    public String generateExample(String base) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(base);
//        for(int i = totalDigits; i > 0; i--) {
//            if (i > 3) {
//                sb.append("0");
//            }
//            else {
//                sb.append(i);
//            }
//        }
//        return sb.toString();
//    }

}
