package edu.stanford.bmir.protege.web.client.tag;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 19 Jun 2018
 */
public class TagCriteriaViewContainerImpl extends Composite implements TagCriteriaViewContainer {


    interface TagCriteriaViewContainerImplUiBinder extends UiBinder<HTMLPanel, TagCriteriaViewContainerImpl> {

    }

    private static TagCriteriaViewContainerImplUiBinder ourUiBinder = GWT.create(TagCriteriaViewContainerImplUiBinder.class);


    private RemoveHandler removeHandler = () -> {};

    @UiField
    SimplePanel viewContainer;

    @UiField
    Button removeButton;

    @Inject
    public TagCriteriaViewContainerImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("removeButton")
    protected void handleRemoveButtonClicked(ClickEvent event) {
        removeHandler.handleRemove();
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getViewContainer() {
        return viewContainer;
    }

    @Override
    public void setRemoveHandler(RemoveHandler removeHandler) {
        this.removeHandler = checkNotNull(removeHandler);
    }
}