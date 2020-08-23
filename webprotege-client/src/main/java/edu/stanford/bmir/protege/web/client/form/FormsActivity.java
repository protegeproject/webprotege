package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
public class FormsActivity extends AbstractActivity {

    private final FormsManagerPresenter formsManagerPresenter;

    private final FormsPlace formsPlace;

    public FormsActivity(@Nonnull FormsManagerPresenter formsManagerPresenter,
                         @Nonnull FormsPlace formsPlace) {
        this.formsManagerPresenter = checkNotNull(formsManagerPresenter);
        this.formsPlace = checkNotNull(formsPlace);
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        formsManagerPresenter.start(panel, eventBus);
    }

    @Override
    public int hashCode() {
        return formsManagerPresenter.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof FormsActivity)) {
            return false;
        }
        FormsActivity other = (FormsActivity) obj;
        return this.formsManagerPresenter == other.formsManagerPresenter && formsPlace.equals(other.formsPlace);
    }
}
