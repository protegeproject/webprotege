package edu.stanford.bmir.protege.web.shared.crud;

import edu.stanford.bmir.protege.web.shared.HasProjectId;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/19/13
 */
public class GetEntityCrudKitSettingsResult implements Result {

    private EntityCrudKitSettings<?> settings;

    private GetEntityCrudKitSettingsResult() {
    }

    public GetEntityCrudKitSettingsResult(EntityCrudKitSettings<?> settings) {
        this.settings = checkNotNull(settings);
    }

    public EntityCrudKitSettings<?> getSettings() {
        return settings;
    }
}
