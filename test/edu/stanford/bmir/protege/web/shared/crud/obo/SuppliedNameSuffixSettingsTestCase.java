package edu.stanford.bmir.protege.web.shared.crud.obo;

import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.supplied.WhiteSpaceTreatment;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/08/2013
 */
public class SuppliedNameSuffixSettingsTestCase {

    @Test
    public void differentObjectsWithEqualConstructorArgumentsAreEqualAndHaveSameHashCode() {
        SuppliedNameSuffixSettings settingsA = new SuppliedNameSuffixSettings();
        SuppliedNameSuffixSettings settingsB = new SuppliedNameSuffixSettings();
        assertEquals(settingsA, settingsB);
        assertEquals(settingsA.hashCode(), settingsB.hashCode());

        SuppliedNameSuffixSettings settingsAA = new SuppliedNameSuffixSettings(WhiteSpaceTreatment.ESCAPE);
        SuppliedNameSuffixSettings settingsBB = new SuppliedNameSuffixSettings(WhiteSpaceTreatment.ESCAPE);
        assertEquals(settingsAA, settingsBB);
        assertEquals(settingsAA.hashCode(), settingsBB.hashCode());
    }

    @Test
    public void getKitIdReturnsExpectedValue() {
        SuppliedNameSuffixSettings settings = new SuppliedNameSuffixSettings();
        assertEquals(SuppliedNameSuffixKit.get().getKitId(), settings.getKitId());
    }
}
