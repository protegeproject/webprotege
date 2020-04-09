package edu.stanford.bmir.protege.web.client.crud;

import com.google.common.collect.ImmutableList;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.form.ObjectListPresenter;
import edu.stanford.bmir.protege.web.shared.crud.ConditionalIriPrefix;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-06
 */
public class EntityCrudKitPrefixSettingsPresenter {

    @Nonnull
    private final EntityCrudKitPrefixSettingsView view;

    @Nonnull
    private final ObjectListPresenter<ConditionalIriPrefix> conditionalIriPrefixesPresenter;

    @Inject
    public EntityCrudKitPrefixSettingsPresenter(@Nonnull EntityCrudKitPrefixSettingsView view,
                                                @Nonnull ObjectListPresenter<ConditionalIriPrefix> conditionalIriPrefixesPresenter) {
        this.view = checkNotNull(view);
        this.conditionalIriPrefixesPresenter = checkNotNull(conditionalIriPrefixesPresenter);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        container.setWidget(view);
        conditionalIriPrefixesPresenter.setAddObjectText("Add rule");
        conditionalIriPrefixesPresenter.start(view.getCriteriaListContainer(), new SimpleEventBus());
    }

    public void clear() {
        view.setFallbackPrefix(EntityCrudKitPrefixSettings.DEFAULT_IRI_PREFIX);
        conditionalIriPrefixesPresenter.clear();
    }

    public void setPrefixSettings(@Nonnull EntityCrudKitPrefixSettings settings) {
        String fallbackPrefix = settings.getIRIPrefix();
        view.setFallbackPrefix(fallbackPrefix);
        conditionalIriPrefixesPresenter.setValues(settings.getConditionalIriPrefixes());
    }

    @Nonnull
    public EntityCrudKitPrefixSettings getPrefixSettings() {
        String fallbackPrefix = view.getFallbackPrefix().trim();
        return EntityCrudKitPrefixSettings.get(fallbackPrefix, ImmutableList.copyOf(conditionalIriPrefixesPresenter.getValues()));
    }

}
