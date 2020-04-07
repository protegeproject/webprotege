package edu.stanford.bmir.protege.web.shared.crud;

import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixKit;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.supplied.WhiteSpaceTreatment;
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
    public void shouldReturnDefaultValueForWhiteSpaceTreatment() {
        SuppliedNameSuffixSettings settings = SuppliedNameSuffixSettings.get();
        assertEquals(settings.getWhiteSpaceTreatment(), WhiteSpaceTreatment.TRANSFORM_TO_CAMEL_CASE);
    }

    @Test
    public void shouldReturnSuppliedWhiteSpaceTreatment() {
        SuppliedNameSuffixSettings settings = SuppliedNameSuffixSettings.get(WhiteSpaceTreatment.ESCAPE);
        assertEquals(settings.getWhiteSpaceTreatment(), WhiteSpaceTreatment.ESCAPE);
    }
}
