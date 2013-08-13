package edu.stanford.bmir.protege.web.shared.crud;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/08/2013
 */
public class EntityCrudKitDescriptorTestCase {

    @Test(expected = NullPointerException.class)
    public void nullEntityCrudKitIdThrowsNullPointerException() {
        new EntityCrudKitDescriptor(null, "displayName", new EntityCrudKitSettings() {
            @Override
            public EntityCrudKitId getKitId() {
                return null;
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void nullDisplayNameThrowsNullPointerException() {
        new EntityCrudKitDescriptor(EntityCrudKitId.get("A"), null, new EntityCrudKitSettings() {
            @Override
            public EntityCrudKitId getKitId() {
                return null;
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void nullSettingsThrowsNullPointerException() {
        new EntityCrudKitDescriptor(EntityCrudKitId.get("A"), "A", null);
    }

    @Test
    public void getIdReturnsEqualToIdSpecifiedInConstructor() {
        EntityCrudKitDescriptor descriptor = new EntityCrudKitDescriptor(EntityCrudKitId.get("A"), "A", new EntityCrudKitSettings() {
            @Override
            public EntityCrudKitId getKitId() {
                return null;
            }
        });
        assertEquals(EntityCrudKitId.get("A"), descriptor.getKitId());
    }

    @Test
    public void getDisplayNameReturnsEqualToSpecifiedInConstructor() {
        EntityCrudKitDescriptor descriptor = new EntityCrudKitDescriptor(EntityCrudKitId.get("A"), "A", new EntityCrudKitSettings() {
            @Override
            public EntityCrudKitId getKitId() {
                return null;
            }
        });
        assertEquals("A", descriptor.getDisplayName());
    }


    @Test
    public void getSettingsEqualsToSpecifiedInConstructor() {
        final EntityCrudKitSettings defaultSettings = new EntityCrudKitSettings() {
            @Override
            public EntityCrudKitId getKitId() {
                return null;
            }
        };
        EntityCrudKitDescriptor descriptor = new EntityCrudKitDescriptor(EntityCrudKitId.get("A"), "A", defaultSettings);
        assertEquals(defaultSettings, descriptor.getDefaultSettings());
    }



}
