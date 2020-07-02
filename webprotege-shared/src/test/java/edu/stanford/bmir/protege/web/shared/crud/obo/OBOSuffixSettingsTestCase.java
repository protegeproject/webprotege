package edu.stanford.bmir.protege.web.shared.crud.obo;

import edu.stanford.bmir.protege.web.shared.crud.oboid.OBOIdSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.oboid.OboIdSuffixSettings;
import org.junit.Test;

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

    @Test
    public void getKitIdReturnsTheCorrectId() {
        OboIdSuffixSettings settings = OboIdSuffixSettings.get();
        assertEquals(OBOIdSuffixKit.getId(), settings.getKitId());
    }

    @Test
    public void shouldSupplyDefaultTotalDigits() {
        OboIdSuffixSettings settings = OboIdSuffixSettings.get();
        assertEquals(7, settings.getTotalDigits());
    }
}
