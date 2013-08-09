package edu.stanford.bmir.protege.web.shared.crud;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 */
public class EntityCrudKitDescriptor implements HasKitId {

    private EntityCrudKitId kitId;

    private String displayName;

    private EntityCrudKitSettings defaultSettings;

    public EntityCrudKitDescriptor(EntityCrudKitId kitId, String displayName, EntityCrudKitSettings defaultSettings) {
        this.kitId = checkNotNull(kitId);
        this.displayName = checkNotNull(displayName);
        this.defaultSettings = checkNotNull(defaultSettings);
    }

    public EntityCrudKitId getKitId() {
        return kitId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public EntityCrudKitSettings getDefaultSettings() {
        return defaultSettings;
    }
}
