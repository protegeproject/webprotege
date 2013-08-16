package edu.stanford.bmir.protege.web.shared.crud;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/08/2013
 */
public class EntityCrudKitConfiguration implements Serializable {

    private EntityCrudKitEditorKit currentKit;

    private List<EntityCrudKitEditorKit> availableKits;

    public EntityCrudKitConfiguration(EntityCrudKitEditorKit currentKit, List<EntityCrudKitEditorKit> availableKits) {
        this.currentKit = currentKit;
        this.availableKits = availableKits;
    }

    public EntityCrudKitEditorKit getCurrentKit() {
        return currentKit;
    }

    public List<EntityCrudKitEditorKit> getAvailableKits() {
        return new ArrayList<EntityCrudKitEditorKit>(availableKits);
    }

}
