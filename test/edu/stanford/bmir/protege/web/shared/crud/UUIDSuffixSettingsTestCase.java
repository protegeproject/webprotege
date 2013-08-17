package edu.stanford.bmir.protege.web.shared.crud;


import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 16/08/2013
 */
public class UUIDSuffixSettingsTestCase {

    @Test
    public void equalsReturnsTrueForDifferentObjects() {
        UUIDSuffixSettings settingsA = new UUIDSuffixSettings();
        UUIDSuffixSettings settingsB = new UUIDSuffixSettings();
        assertEquals(settingsA, settingsB);
    }

    @Test
    public void hashCodeReturnsSameValueForDifferentObjects() {
        UUIDSuffixSettings settingsA = new UUIDSuffixSettings();
        UUIDSuffixSettings settingsB = new UUIDSuffixSettings();
        assertEquals(settingsA.hashCode(), settingsB.hashCode());
    }

    @Test
    public void getKitIdIsNotNull() {
        UUIDSuffixSettings settings = new UUIDSuffixSettings();
        EntityCrudKitId kitId = settings.getKitId();
        assertNotNull(kitId);
    }

    @Test
    public void getKitIdMatchesUUIDDescriptorId() {
        UUIDSuffixSettings settings = new UUIDSuffixSettings();
        assertEquals(UUIDSuffixDescriptor.get().getKitId(), settings.getKitId());
    }

}
