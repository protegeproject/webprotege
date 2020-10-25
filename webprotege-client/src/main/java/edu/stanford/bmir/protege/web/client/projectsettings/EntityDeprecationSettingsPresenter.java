package edu.stanford.bmir.protege.web.client.projectsettings;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.shared.projectsettings.EntityDeprecationSettings;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-23
 */
public class EntityDeprecationSettingsPresenter {

    @Nonnull
    private final EntityDeprecationSettingsView view;

    @Inject
    public EntityDeprecationSettingsPresenter(@Nonnull EntityDeprecationSettingsView view) {
        this.view = checkNotNull(view);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
    }

    public void setValue(@Nonnull EntityDeprecationSettings settings) {

    }

    public EntityDeprecationSettings getValue() {
        return EntityDeprecationSettings.empty();
    }
}
