package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.form.FormControlStackRepeatingView.FormControlContainer;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class FormControlContainerImpl extends Composite implements FormControlContainer {

    private boolean enabled = true;

    interface FormControlContainerImplUiBinder extends UiBinder<HTMLPanel, FormControlContainerImpl> {
    }

    private static FormControlContainerImplUiBinder ourUiBinder = GWT.create(FormControlContainerImplUiBinder.class);

    @UiField
    SimplePanel container;

    @UiField
    Button deleteButton;

    @UiField
    FormControlContainerStyle style;

    private RemoveHandler removeHandler = () -> {};

    @Inject
    public FormControlContainerImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        deleteButton.addClickHandler(this::handleRemoveClicked);
        deleteButton.addMouseOverHandler(this::handleMouseOverDeleteButton);
        deleteButton.addMouseOutHandler(this::handleMouseOutDeleteButton);
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

    @Override
    public void setRemoveHandler(RemoveHandler handler) {
        removeHandler = checkNotNull(handler);
    }

    private void handleRemoveClicked(@Nonnull ClickEvent event) {
        // TODO: Confirm
        removeHandler.handleRemove();
    }

    private void handleMouseOverDeleteButton(MouseOverEvent event) {
        container.addStyleName(WebProtegeClientBundle.BUNDLE.style().formStackItemDeleteHover());
    }

    private void handleMouseOutDeleteButton(MouseOutEvent event) {
        container.removeStyleName(WebProtegeClientBundle.BUNDLE.style().formStackItemDeleteHover());
    }

}