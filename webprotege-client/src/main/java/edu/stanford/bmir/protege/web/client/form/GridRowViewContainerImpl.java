package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class GridRowViewContainerImpl extends Composite implements GridRowViewContainer {

    private boolean enabled = true;

    interface GridRowViewContainerImplUiBinder extends UiBinder<HTMLPanel, GridRowViewContainerImpl> {
    }

    private static GridRowViewContainerImplUiBinder ourUiBinder = GWT.create(GridRowViewContainerImplUiBinder.class);

    private DeleteHandler deleteHandler = () -> {};

    @UiField
    GridRowViewContainerCss style;

    @UiField
    SimplePanel container;

    @UiField
    Button deleteButton;

    @Inject
    public GridRowViewContainerImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        deleteButton.addClickHandler(this::handleDeleteButtonClicked);
        deleteButton.addMouseOverHandler(this::handleDeleteButtonMouseOver);
        deleteButton.addMouseOutHandler(this::handleDeleteButtonMouseOut);
    }

    private void handleDeleteButtonMouseOut(MouseOutEvent event) {
        removeStyleName(WebProtegeClientBundle.BUNDLE.style().formStackItemDeleteHover());
    }

    private void handleDeleteButtonMouseOver(MouseOverEvent event) {
        addStyleName(WebProtegeClientBundle.BUNDLE.style().formStackItemDeleteHover());
    }

    private void handleDeleteButtonClicked(ClickEvent event) {
        // TODO:  Confirm?
        deleteHandler.handleDelete();
    }


    @Override
    public void setDeleteHandler(@Nonnull DeleteHandler handler) {
        this.deleteHandler = checkNotNull(handler);
    }

    @Override
    public void clear() {

    }

    @Override
    public void setWidget(IsWidget w) {
        container.setWidget(w);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if(enabled) {
            removeStyleName(style.disabled());
        }
        else {
            addStyleName(style.disabled());
        }
    }

    @Override
    public void requestFocus() {
        Widget w = container.getWidget();
        if(w instanceof HasRequestFocus) {
            ((HasRequestFocus) w).requestFocus();
        }
    }
}