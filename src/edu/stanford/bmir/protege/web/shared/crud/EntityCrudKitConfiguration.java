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

    private EntityCrudKitManager currentKit;

    private List<EntityCrudKitManager> availableKits;

    public EntityCrudKitConfiguration(EntityCrudKitManager currentKit, List<EntityCrudKitManager> availableKits) {
        this.currentKit = currentKit;
        this.availableKits = availableKits;
    }

    public EntityCrudKitManager getCurrentKit() {
        return currentKit;
    }

    public List<EntityCrudKitManager> getAvailableKits() {
        return new ArrayList<EntityCrudKitManager>(availableKits);
    }

}
