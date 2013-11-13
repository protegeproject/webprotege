package edu.stanford.bmir.protege.web.shared.crud.obo;

import edu.stanford.bmir.protege.web.shared.crud.oboid.OBOIdSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.oboid.OBOIdSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.oboid.UserIdRange;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/08/2013
 */
public class OBOSuffixSettingsTestCase {

    @Test(expected = NullPointerException.class)
    public void constructorThrowsNullPointerExceptionIfUserRangesIsNull() {
        new OBOIdSuffixSettings(null);
    }


    @Test(expected = NullPointerException.class)
    public void constructorThrowsNullPointerExceptionIfUserRangesWithDigitsIsNull() {
        new OBOIdSuffixSettings(7, null);
    }

    @Test
    public void differentObjectsWithEqualConstructorArgumentsAreEqualAndHaveSameHashCode() {
        OBOIdSuffixSettings settingsA = new OBOIdSuffixSettings(Collections.<UserIdRange>emptyList());
        OBOIdSuffixSettings settingsB = new OBOIdSuffixSettings(Collections.<UserIdRange>emptyList());
        assertEquals(settingsA, settingsB);
        assertEquals(settingsA.hashCode(), settingsB.hashCode());

        OBOIdSuffixSettings settingsAA = new OBOIdSuffixSettings(7, Collections.<UserIdRange>emptyList());
        OBOIdSuffixSettings settingsBB = new OBOIdSuffixSettings(7, Collections.<UserIdRange>emptyList());
        assertEquals(settingsAA, settingsBB);
        assertEquals(settingsAA.hashCode(), settingsBB.hashCode());
    }


    @Test
    public void getKitIdReturnsTheCorrectId() {
        OBOIdSuffixSettings settings = new OBOIdSuffixSettings();
        assertEquals(OBOIdSuffixKit.get().getKitId(), settings.getKitId());
    }

    @Test
    public void constructorCopiesSpecifiedUserRanges() {
        List<UserIdRange> rangeList = new ArrayList<UserIdRange>();
        UserIdRange userIdRange = mock(UserIdRange.class);
        rangeList.add(userIdRange);
        OBOIdSuffixSettings settings = new OBOIdSuffixSettings(rangeList);
        rangeList.clear();
        assertEquals(Arrays.asList(userIdRange), settings.getUserIdRanges());
    }

    @Test
    public void getUserIdRangesReturnsCopy() {
        OBOIdSuffixSettings settings = new OBOIdSuffixSettings();
        List<UserIdRange> rangeList = settings.getUserIdRanges();
        rangeList.add(mock(UserIdRange.class));
        assertTrue(settings.getUserIdRanges().isEmpty());
    }

}
