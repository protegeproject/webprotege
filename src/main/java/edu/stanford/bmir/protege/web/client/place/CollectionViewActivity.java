package edu.stanford.bmir.protege.web.client.place;

import com.google.common.base.Objects;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.collection.CollectionPresenter;
import edu.stanford.bmir.protege.web.shared.place.CollectionViewPlace;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Jul 2017
 */
public class CollectionViewActivity extends AbstractActivity {

    @Nonnull
    private final CollectionPresenter presenter;

    @Nonnull
    private final CollectionViewPlace place;

    @Inject
    public CollectionViewActivity(@Nonnull CollectionPresenter presenter,
                                  @Nonnull CollectionViewPlace place) {
        this.presenter = checkNotNull(presenter);
        this.place = checkNotNull(place);
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        GWT.log("[CollectionViewActivity] Starting activity");
        presenter.start(panel, eventBus);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(place);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CollectionViewActivity)) {
            return false;
        }
        CollectionViewActivity other = (CollectionViewActivity) obj;
        return this.place.equals(other.place);
    }


    @Override
    public String toString() {
        return toStringHelper("CollectionViewActivity")
                .addValue(place)
                .toString();
    }


}
