package edu.stanford.bmir.protege.web.shared.place;

import com.google.common.base.Objects;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import edu.stanford.bmir.protege.web.client.admin.AdminPresenter;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Mar 2017
 */
public class AdminActivity extends AbstractActivity {

    private final AdminPresenter presenter;

    @Inject
    public AdminActivity(AdminPresenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        presenter.start(panel, eventBus);
    }

    @Override
    public int hashCode() {
        return 44;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj instanceof AdminActivity;
    }
}
