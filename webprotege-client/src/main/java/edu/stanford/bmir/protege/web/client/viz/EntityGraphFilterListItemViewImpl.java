package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-11
 */
public class EntityGraphFilterListItemViewImpl extends Composite implements EntityGraphFilterListItemView {

    private Runnable deleteHandler = () -> {};

    interface EntityGraphFilterListItemViewImplUiBinder extends UiBinder<HTMLPanel, EntityGraphFilterListItemViewImpl> {

    }

    private static EntityGraphFilterListItemViewImplUiBinder ourUiBinder = GWT.create(
            EntityGraphFilterListItemViewImplUiBinder.class);

    @UiField
    SimplePanel container;

    @UiField
    Button deleteButton;

    @Inject
    public EntityGraphFilterListItemViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        deleteButton.addClickHandler(event -> deleteHandler.run());
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getContainer() {
        return container;
    }

    @Override
    public void setDeleteHandler(Runnable handler) {
        this.deleteHandler = checkNotNull(handler);
    }
}
