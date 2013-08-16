package edu.stanford.bmir.protege.web.shared.crud;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.irigen.obo.UserIdRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 * <p>
 *     Settings for generating OBO style Ids.
 * </p>
 */
public class OBOIdSuffixSettings extends EntityCrudKitSuffixSettings {

    private static final int DEFAULT_TOTAL_DIGITS = 7;

    private int totalDigits = DEFAULT_TOTAL_DIGITS;

//    private static final String DEFAULT_IRI_PREFIX = "http://purl.obolibrary.org/obo/";

    private List<UserIdRange> userIdRanges = new ArrayList<UserIdRange>();


    /**
     * For serialization purposes only.
     */
    public OBOIdSuffixSettings() {
        this(Collections.<UserIdRange>emptyList());
    }

    public OBOIdSuffixSettings(List<UserIdRange> userIdRanges) {
        this(DEFAULT_TOTAL_DIGITS, userIdRanges);
    }

    public OBOIdSuffixSettings(int totalDigits, List<UserIdRange> userIdRanges) {
        this.totalDigits = totalDigits;
        this.userIdRanges.addAll(userIdRanges);
    }



    @Override
    public EntityCrudKitId getKitId() {
        return OBOIdSuffixDescriptor.get().getKitId();
    }

    public int getTotalDigits() {
        return totalDigits;
    }

    public List<UserIdRange> getUserIdRanges() {
        return new ArrayList<UserIdRange>(userIdRanges);
    }



    @Override
    public int hashCode() {
        return "OBOIdSchemeSpecificSettings".hashCode() + totalDigits + userIdRanges.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof OBOIdSuffixSettings)) {
            return false;
        }
        OBOIdSuffixSettings other = (OBOIdSuffixSettings) obj;
        return this.totalDigits == other.totalDigits && this.userIdRanges.equals(other.userIdRanges);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper("OBOIdSuffixSettings")
                .add("totalDigits", totalDigits)
                .add("userRanges", userIdRanges)
                .toString();
    }
}
