package edu.stanford.bmir.protege.web.server.crud;

import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedAnnotationsSettings;
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
        EntityCrudKitSettings.get(null, mock(EntityCrudKitSuffixSettings.class), mock(GeneratedAnnotationsSettings.class));
    }

    @Test(expected = NullPointerException.class)
    public void constructorThrowsNullPointerExceptionIfSuffixSettingsIsNull() {
        EntityCrudKitSettings.get(EntityCrudKitPrefixSettings.get(), null, mock(GeneratedAnnotationsSettings.class));
    }

    @Test
    public void objectsWithSamePrefixAndSuffixSettingsHaveEqualHashCodes() {
        EntityCrudKitPrefixSettings prefixSettings = EntityCrudKitPrefixSettings.get();
        EntityCrudKitSuffixSettings suffixSettings = mock(EntityCrudKitSuffixSettings.class);
        GeneratedAnnotationsSettings generatedAnnotationsSettings = mock(GeneratedAnnotationsSettings.class);
        EntityCrudKitSettings<?> settingsA = EntityCrudKitSettings.get(prefixSettings, suffixSettings, generatedAnnotationsSettings);
        EntityCrudKitSettings<?> settingsB = EntityCrudKitSettings.get(prefixSettings, suffixSettings, generatedAnnotationsSettings);
        assertEquals(settingsA.hashCode(), settingsB.hashCode());
    }

    @Test
    public void objectsWithSamePrefixAndSuffixSettingsAreEqual() {
        EntityCrudKitPrefixSettings prefixSettings = EntityCrudKitPrefixSettings.get();
        EntityCrudKitSuffixSettings suffixSettings = mock(EntityCrudKitSuffixSettings.class);
        GeneratedAnnotationsSettings generatedAnnotationsSettings = mock(GeneratedAnnotationsSettings.class);
        EntityCrudKitSettings<?> settingsA = EntityCrudKitSettings.get(prefixSettings, suffixSettings, generatedAnnotationsSettings);
        EntityCrudKitSettings<?> settingsB = EntityCrudKitSettings.get(prefixSettings, suffixSettings, generatedAnnotationsSettings);
        assertEquals(settingsA, settingsB);
    }

}
