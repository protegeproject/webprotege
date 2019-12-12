package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-12-11
 */
public class EntityGraphFilterListViewImpl extends Composite implements EntityGraphFilterListView {

    private Runnable addItemHandler = () -> {};

    interface EntityGraphFilterListViewImplUiBinder extends UiBinder<HTMLPanel, EntityGraphFilterListViewImpl> {

    }

    private static EntityGraphFilterListViewImplUiBinder ourUiBinder = GWT.create(EntityGraphFilterListViewImplUiBinder.class);

    @UiField
    HTMLPanel container;

    @UiField
    Button addButton;

    @Inject
    public EntityGraphFilterListViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        addButton.addClickHandler(event -> addItemHandler.run());
    }

    @Override
    public void addItem(@Nonnull EntityGraphFilterListItemView itemView) {
        container.add(itemView);
    }

    @Override
    public void clear() {
        container.clear();
    }

    @Override
    public void removeItem(@Nonnull EntityGraphFilterListItemView itemView) {
        container.remove(itemView);
    }

    @Override
    public void setAddItemHandler(Runnable handler) {
        this.addItemHandler = checkNotNull(handler);
    }
}
