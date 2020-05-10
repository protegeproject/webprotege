package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class FormControlStackRepeatingViewImpl extends Composite implements FormControlStackRepeatingView {

    private AddFormControlHandler addFormControlHandler = () -> {};

    private boolean enabled = true;

    interface FormControlStackRepeatingViewImplUiBinder extends UiBinder<HTMLPanel, FormControlStackRepeatingViewImpl> {
    }

    private static FormControlStackRepeatingViewImplUiBinder ourUiBinder = GWT.create(
            FormControlStackRepeatingViewImplUiBinder.class);
    @UiField
    SimplePanel paginatorContainer;

    @UiField
    HTMLPanel stackContainer;

    @UiField
    Button addButton;

    @Inject
    public FormControlStackRepeatingViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        addButton.addClickHandler(this::handleAddButtonClicked);
    }

    private void handleAddButtonClicked(ClickEvent event) {
        addFormControlHandler.handleAdd();
    }

    @Override
    public void setAddFormControlHandler(@Nonnull AddFormControlHandler handler) {
        this.addFormControlHandler = checkNotNull(handler);
    }

    @Nonnull
    @Override
    public FormControlContainer addFormControlContainer() {
        FormControlContainer container =  new FormControlContainerImpl();
        stackContainer.add(container);
        return container;
    }

    @Override
    public void removeFormControlContainer(FormControlContainer container) {
        stackContainer.remove(container);
    }

    @Override
    public void clear() {
        stackContainer.clear();
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getPaginatorContainer() {
        return paginatorContainer;
    }

    @Override
    public void setPaginatorVisible(boolean visible) {
        paginatorContainer.setVisible(visible);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        for(int i = 0; i < stackContainer.getWidgetCount(); i++) {
            Widget w = stackContainer.getWidget(i);
            if(w instanceof FormControlContainer) {
                ((FormControlContainer) w).setEnabled(enabled);
            }
        }
        addButton.setVisible(enabled);
    }
}