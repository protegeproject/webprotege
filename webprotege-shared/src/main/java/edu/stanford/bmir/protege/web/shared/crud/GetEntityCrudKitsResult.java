package edu.stanford.bmir.protege.web.shared.crud;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class GetEntityCrudKitsResult implements Result {

    private List<EntityCrudKit<?>> kits = new ArrayList<EntityCrudKit<?>>();

    private GetEntityCrudKitsResult() {
    }

    public GetEntityCrudKitsResult(List<EntityCrudKit<?>> kits) {
        this.kits = kits;
    }

    public List<EntityCrudKit<?>> getKits() {
        return new ArrayList<EntityCrudKit<?>>(kits);
    }
}
