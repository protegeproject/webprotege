package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.settings.SettingsPresenter;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
public class LanguageSettingsPresenter {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final SettingsPresenter settingsPresenter;

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private final DisplayLanguagesView displayLanguagesView;

    @Nonnull
    private final EntityDefaultLanguageView entityDefaultLanguageView;

    @Nonnull
    private Optional<Place> nextPlace = Optional.empty();

    @Inject
    public LanguageSettingsPresenter(@Nonnull ProjectId projectId,
                                     @Nonnull SettingsPresenter settingsPresenter,
                                     @Nonnull DispatchServiceManager dispatch,
                                     @Nonnull EntityDefaultLanguageView entityDefaultLanguageView,
                                     @Nonnull DisplayLanguagesView displayLanguagesView) {
        this.projectId = checkNotNull(projectId);
        this.settingsPresenter = checkNotNull(settingsPresenter);
        this.dispatch = checkNotNull(dispatch);
        this.entityDefaultLanguageView = checkNotNull(entityDefaultLanguageView);
        this.displayLanguagesView = checkNotNull(displayLanguagesView);
    }

    public void start(@Nonnull AcceptsOneWidget container) {
        settingsPresenter.setSettingsTitle("Language Settings");
        settingsPresenter.start(container);
        settingsPresenter.addSection("Display Languages").setWidget(displayLanguagesView);
        settingsPresenter.addSection("New Entities Language").setWidget(entityDefaultLanguageView);
    }

    public void setNextPlace(@Nonnull Optional<Place> place) {
        this.nextPlace = checkNotNull(place);
    }

}
