package edu.stanford.bmir.protege.web.shared.crud;

import edu.stanford.bmir.protege.web.shared.HasDisplayName;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 * <p>
 *     An {@code EntityCrudKit} is used by the system when creating and updating entities.  Each kit provides a human
 *     readable name, an editor for viewing and altering settings for the kit and a back end implementation that actually
 *     generates the ontology changes required to enact high level changes such as "creating" a fresh entity, or updating
 *     the display name.
 * </p>
 */
public abstract class EntityCrudKit<S extends EntityCrudKitSuffixSettings> implements HasKitId, HasDisplayName, Serializable {

    private EntityCrudKitId kitId;

    private String displayName;

    /**
     * For serialization purposes only!
     */
    protected EntityCrudKit() {
    }

    /**
     * Creates a descriptor for the specified information.
     * @param kitId The id of the kit. Not {@code null}.
     * @param displayName The display name for the kit.  Not {@code null}.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public EntityCrudKit(EntityCrudKitId kitId, String displayName) {
        this.kitId = checkNotNull(kitId);
        this.displayName = checkNotNull(displayName);
    }

    /**
     * Gets the id for this kit.
     * @return The id.  Not {@code null}.
     */
    @Override
    public EntityCrudKitId getKitId() {
        return kitId;
    }

    /**
     * Gets the human readable display name for this kit.
     * @return The name.  Not {@code null}.
     */
    @Override
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets an editor for viewing and altering the suffix settings.
     * This method may only be called on the client side.
     * @return An editor.  Not {@code null}.
     */
    public abstract EntityCrudKitSuffixSettingsEditor<S> getSuffixSettingsEditor();

    public abstract EntityCrudKitPrefixSettings getDefaultPrefixSettings();

    public abstract S getDefaultSuffixSettings();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EntityCrudKit");
        sb.append("(");
        sb.append(getKitId());
        sb.append(" DisplayName(");
        sb.append(getDisplayName());
        sb.append("))");
        return sb.toString();
    }
}
