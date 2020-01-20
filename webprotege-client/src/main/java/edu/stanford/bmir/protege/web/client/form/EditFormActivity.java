package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-20
 */
public class EditFormActivity extends AbstractActivity {

    private final FormEditorPresenter presenter;

    private final EditFormPlace place;

    @Inject
    public EditFormActivity(FormEditorPresenter presenter, EditFormPlace place) {
        this.presenter = checkNotNull(presenter);
        this.place = checkNotNull(place);
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        presenter.start(panel, eventBus);
        presenter.setFormId(place.getFormId());
        presenter.setNextPlace(place.getNextPlace());
    }

    @Override
    public int hashCode() {
        return presenter.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof EditFormActivity)) {
            return false;
        }
        EditFormActivity other = (EditFormActivity) obj;
        return this.presenter == other.presenter;
    }

}
