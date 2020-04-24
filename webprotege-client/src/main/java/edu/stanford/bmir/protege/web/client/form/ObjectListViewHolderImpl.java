package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;

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
        moveUpButton.addClickHandler(event -> moveUpHandler.run());
        moveDownButton.addClickHandler(event -> moveDownHandler.run());
        removeButton.addClickHandler(event -> removeHandler.run());
        elementHeader.sinkEvents(Event.ONCLICK);
        elementHeader.addHandler(event -> toggleExpansion(), ClickEvent.getType());
    }

    @Override
    public void setWidget(IsWidget w) {
        container.clear();
        container.add(w);
    }

    private void toggleExpansion() {
        container.setVisible(!container.isVisible());
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
}
