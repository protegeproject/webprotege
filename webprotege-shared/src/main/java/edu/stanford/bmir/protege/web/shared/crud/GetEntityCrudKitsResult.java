package edu.stanford.bmir.protege.web.shared.crud;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class GetEntityCrudKitsResult implements Result {

    private List<EntityCrudKit<?>> kits = new ArrayList<>();

    private EntityCrudKitSettings<?> currentSettings;

    private GetEntityCrudKitsResult() {
    }

    public GetEntityCrudKitsResult(List<EntityCrudKit<?>> kits, EntityCrudKitSettings<?> currentSettings) {
        this.kits = checkNotNull(kits);
        this.currentSettings = checkNotNull(currentSettings);
    }

    public List<EntityCrudKit<?>> getKits() {
        return new ArrayList<EntityCrudKit<?>>(kits);
    }

    public EntityCrudKitSettings<?> getCurrentSettings() {
        return currentSettings;
    }
}
