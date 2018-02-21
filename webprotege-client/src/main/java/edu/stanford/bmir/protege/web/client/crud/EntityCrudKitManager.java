package edu.stanford.bmir.protege.web.client.crud;

import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKit;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class EntityCrudKitManager {

    private static EntityCrudKitManager instance = new EntityCrudKitManager();

    private List<EntityCrudKit<?>> kits = new ArrayList<EntityCrudKit<?>>();

    private EntityCrudKitManager() {
    }

    public static EntityCrudKitManager get() {
        return instance;
    }


    public List<EntityCrudKit<?>> getKits() {
        return new ArrayList<EntityCrudKit<?>>(kits);
    }

    protected void init(List<EntityCrudKit<?>> kits) {
        this.kits.addAll(kits);
    }
}
