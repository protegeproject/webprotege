package edu.stanford.bmir.protege.web.client.search;

import com.google.auto.value.AutoValue;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-16
 */
@AutoValue
public abstract class EntitySearchSettingsActivity extends AbstractActivity {

    @Nonnull
    public static EntitySearchSettingsActivity get(@Nonnull EntitySearchSettingsPresenter presenter,
                                                   @Nonnull Optional<Place> nextPlace) {
        return new AutoValue_EntitySearchSettingsActivity(nextPlace, presenter);
    }

    protected abstract Optional<Place> getNextPlace();

    protected abstract EntitySearchSettingsPresenter getPresenter();

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        EntitySearchSettingsPresenter presenter = getPresenter();
        presenter.start(panel, eventBus);
    }
}
