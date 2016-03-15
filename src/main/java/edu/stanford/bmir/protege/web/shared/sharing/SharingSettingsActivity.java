package edu.stanford.bmir.protege.web.shared.sharing;

import com.google.common.base.Objects;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.sharing.SharingSettingsPresenter;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13/03/16
 */
public class SharingSettingsActivity extends AbstractActivity {

    private final SharingSettingsPresenter presenter;

    private final SharingSettingsPlace place;

    public SharingSettingsActivity(SharingSettingsPresenter presenter, SharingSettingsPlace place) {
        this.presenter = checkNotNull(presenter);
        this.place = checkNotNull(place);
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        presenter.setNextPlace(place.getContinueTo());
        presenter.start(panel);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(presenter, place);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SharingSettingsActivity)) {
            return false;
        }
        SharingSettingsActivity other = (SharingSettingsActivity) obj;
        return this.presenter.equals(other.presenter)
                && this.place.equals(other.place);
    }


    @Override
    public String toString() {
        return toStringHelper("SharingSettingsActivity")
                .addValue(place)
                .toString();
    }
}
