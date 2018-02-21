package edu.stanford.bmir.protege.web.shared.crud;

import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixSettings;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class SuppliedNameSuffixSettingsTestCase {

    @Test
    public void equalsReturnsTrueForDifferentObjects() {
        SuppliedNameSuffixSettings settingsA = new SuppliedNameSuffixSettings();
        SuppliedNameSuffixSettings settingsB = new SuppliedNameSuffixSettings();
        assertEquals(settingsA, settingsB);
    }

    @Test
    public void hashCodeReturnsSameValueForDifferentObjects() {
        SuppliedNameSuffixSettings settingsA = new SuppliedNameSuffixSettings();
        SuppliedNameSuffixSettings settingsB = new SuppliedNameSuffixSettings();
        assertEquals(settingsA.hashCode(), settingsB.hashCode());
    }

    @Test
    public void getKitIdIsNotNull() {
        SuppliedNameSuffixSettings settings = new SuppliedNameSuffixSettings();
        EntityCrudKitId kitId = settings.getKitId();
        assertNotNull(kitId);
    }

    @Test
    public void getKitIdMatchesSuppliedNameDescriptorId() {
        SuppliedNameSuffixSettings settings = new SuppliedNameSuffixSettings();
        assertEquals(SuppliedNameSuffixKit.get().getKitId(), settings.getKitId());
    }
}
