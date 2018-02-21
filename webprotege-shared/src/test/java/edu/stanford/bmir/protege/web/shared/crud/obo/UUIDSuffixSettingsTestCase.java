package edu.stanford.bmir.protege.web.shared.crud.obo;

import edu.stanford.bmir.protege.web.shared.crud.uuid.UUIDSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UUIDSuffixSettings;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/08/2013
 */
public class UUIDSuffixSettingsTestCase {

    @Test
    public void getKitIdReturnsExpectedId() {
        UUIDSuffixSettings suffixSettings = new UUIDSuffixSettings();
        assertEquals(UUIDSuffixKit.get().getKitId(), suffixSettings.getKitId());
    }

    @Test
    public void differentObjectsAreEqualAndHaveEqualHashCodes() {
        UUIDSuffixSettings settingsA = new UUIDSuffixSettings();
        UUIDSuffixSettings settingsB = new UUIDSuffixSettings();
        assertEquals(settingsA, settingsB);
        assertEquals(settingsA.hashCode(), settingsB.hashCode());
    }
}
