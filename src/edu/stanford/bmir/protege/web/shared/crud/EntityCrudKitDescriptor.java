package edu.stanford.bmir.protege.web.shared.crud;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 * <p>
 *     Describes an {@link edu.stanford.bmir.protege.web.server.crud.EntityCrudKitHandler} in terms of its id, display name (for
 *     use in the UI, and default settings).
 * </p>
 */
public abstract class EntityCrudKitDescriptor implements HasKitId {

    private EntityCrudKitId kitId;

    private String displayName;

    /**
     * Creates a descriptor for the specified information.
     * @param kitId The id of the kit. Not {@code null}.
     * @param displayName The display name for the kit.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public EntityCrudKitDescriptor(EntityCrudKitId kitId, String displayName) {
        this.kitId = checkNotNull(kitId);
        this.displayName = checkNotNull(displayName);
    }

    public EntityCrudKitId getKitId() {
        return kitId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public abstract EntityCrudKitSuffixSettingsEditor<?> getSuffixSettingsEditor();

    public abstract EntityCrudKitPrefixSettings getDefaultPrefixSettings();

    public abstract EntityCrudKitSuffixSettings getDefaultSuffixSettings();


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EntityCrudKitDescriptor");
        sb.append("(");
        sb.append(getKitId());
        sb.append(" DisplayName(");
        sb.append(getDisplayName());
        sb.append("))");
        return sb.toString();
    }
}
