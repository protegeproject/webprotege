package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-18
 */
public class ObjectListViewHolderImpl extends Composite implements ObjectListViewHolder {

    interface ObjectListViewHolderUiBinder extends UiBinder<HTMLPanel, ObjectListViewHolderImpl> {

    }

    private static ObjectListViewHolderUiBinder ourUiBinder = GWT.create(
            ObjectListViewHolderUiBinder.class);

    @UiField
    HTMLPanel container;

    @UiField
    Button moveUpButton;

    @UiField
    Button moveDownButton;

    @UiField
    Button removeButton;

    @UiField
    Label numberField;

    @UiField
    HTMLPanel buttonBar;

    @UiField
    Label elementHeaderLabel;

    @UiField
    HTMLPanel elementHeader;

    private Runnable removeHandler = () -> {};

    private Runnable moveUpHandler = () -> {};

    private Runnable moveDownHandler = () -> {};

    @Inject
    public ObjectListViewHolderImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        moveUpButton.addClickHandler(this::handleMoveUpButtonPressed);
        moveDownButton.addClickHandler(this::handleMoveDownButtonPressed);
        removeButton.addClickHandler(event -> removeHandler.run());
        elementHeader.sinkEvents(Event.ONCLICK);
        elementHeader.addHandler(event -> toggleExpansion(), ClickEvent.getType());
    }

    private void handleMoveDownButtonPressed(ClickEvent event) {
        event.preventDefault();
        moveDownHandler.run();
    }

    private void handleMoveUpButtonPressed(ClickEvent event) {
        event.preventDefault();
        moveUpHandler.run();
    }

    @Override
    public void setWidget(IsWidget w) {
        container.clear();
        container.add(w);
    }

    @Override
    public void toggleExpansion() {
        container.setVisible(!container.isVisible());
    }

    @Override
    public void setCollapsed() {
        container.setVisible(false);
    }

    @Override
    public void setExpanded() {
        container.setVisible(true);
    }

    @Override
    public boolean isExpanded() {
        return container.isVisible();
    }

    @Override
    public void setNumber(int number) {
        numberField.setText(Integer.toString(number));
    }

    @Override
    public void setHeaderLabel(@Nonnull String headerLabel) {
        elementHeaderLabel.setText(headerLabel);
    }

    @Override
    public void scrollIntoView() {
        Element element = numberField.getElement();
        scrollIntoView(element);
    }

    @Override
    public void setFirst() {
        moveUpButton.setEnabled(false);
    }

    @Override
    public void setMiddle() {
        moveUpButton.setEnabled(true);
        moveDownButton.setEnabled(true);
    }

    @Override
    public void setLast() {
        moveDownButton.setEnabled(false);
    }

    @Override
    public void setPositionOrdinal(int i) {
        getElement().getStyle().setProperty("order", Integer.toString(i));
    }

    public native void scrollIntoView(JavaScriptObject element)/*-{
        element.scrollIntoView(true)
    }-*/;

    @Override
    public void setMoveDownHandler(@Nonnull Runnable handler) {
        this.moveDownHandler = checkNotNull(handler);
    }

    @Override
    public void setMoveUpHandler(@Nonnull Runnable handler) {
        this.moveUpHandler = checkNotNull(handler);
    }

    @Override
    public void setRemoveHandler(@Nonnull Runnable handler) {
        this.removeHandler = checkNotNull(handler);
    }

    @Override
    public void requestFocus() {
        if(container.getWidgetCount() > 0) {
            Widget widget = container.getWidget(0);
            if(widget instanceof HasRequestFocus) {
                ((HasRequestFocus) widget).requestFocus();
            }
        }
    }
}
