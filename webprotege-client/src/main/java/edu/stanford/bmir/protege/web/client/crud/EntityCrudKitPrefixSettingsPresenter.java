package edu.stanford.bmir.protege.web.client.crud;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKit;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public class EntityCrudKitPrefixSettingsPresenter {

    @Nonnull
    private final EntityCrudKitPrefixSettingsView view;

    @Inject
    public EntityCrudKitPrefixSettingsPresenter(@Nonnull EntityCrudKitPrefixSettingsView view) {
        this.view = checkNotNull(view);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void clear() {
        view.setFallbackPrefix(EntityCrudKitPrefixSettings.DEFAULT_IRI_PREFIX);
    }

    public void setPrefixSettings(@Nonnull EntityCrudKitPrefixSettings settings) {
        String fallbackPrefix = settings.getIRIPrefix();
        view.setFallbackPrefix(fallbackPrefix);
    }

    @Nonnull
    public EntityCrudKitPrefixSettings getPrefixSettings() {
        String fallbackPrefix = view.getFallbackPrefix().trim();
        return EntityCrudKitPrefixSettings.get(fallbackPrefix);
    }

}
