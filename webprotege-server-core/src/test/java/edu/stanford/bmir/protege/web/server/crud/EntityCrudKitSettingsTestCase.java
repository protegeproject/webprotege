package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/08/2013
 */
public class EntityCrudKitSettingsTestCase {

    @Test(expected = NullPointerException.class)
    public void constructorThrowsNullPointerExceptionIfPrefixSettingsIsNull() {
        EntityCrudKitSettings.get(null, mock(EntityCrudKitSuffixSettings.class));
    }

    @Test(expected = NullPointerException.class)
    public void constructorThrowsNullPointerExceptionIfSuffixSettingsIsNull() {
        EntityCrudKitSettings.get(EntityCrudKitPrefixSettings.get(), null);
    }

    @Test
    public void objectsWithSamePrefixAndSuffixSettingsHaveEqualHashCodes() {
        EntityCrudKitPrefixSettings prefixSettings = EntityCrudKitPrefixSettings.get();
        EntityCrudKitSuffixSettings suffixSettings = mock(EntityCrudKitSuffixSettings.class);
        EntityCrudKitSettings<?> settingsA = EntityCrudKitSettings.get(prefixSettings, suffixSettings);
        EntityCrudKitSettings<?> settingsB = EntityCrudKitSettings.get(prefixSettings, suffixSettings);
        assertEquals(settingsA.hashCode(), settingsB.hashCode());
    }

    @Test
    public void objectsWithSamePrefixAndSuffixSettingsAreEqual() {
        EntityCrudKitPrefixSettings prefixSettings = EntityCrudKitPrefixSettings.get();
        EntityCrudKitSuffixSettings suffixSettings = mock(EntityCrudKitSuffixSettings.class);
        EntityCrudKitSettings<?> settingsA = EntityCrudKitSettings.get(prefixSettings, suffixSettings);
        EntityCrudKitSettings<?> settingsB = EntityCrudKitSettings.get(prefixSettings, suffixSettings);
        assertEquals(settingsA, settingsB);
    }

}
