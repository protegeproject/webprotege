package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
public class ObjectListViewImpl extends Composite implements ObjectListView {

    private final MessageBox messageBox;

    private final FormsMessages formsMessages;

    private Runnable addObjectHandler = () -> {};

    interface FormElementDescriptorListViewImplUiBinder extends UiBinder<HTMLPanel, ObjectListViewImpl> {

    }

    private static FormElementDescriptorListViewImplUiBinder ourUiBinder = GWT.create(
            FormElementDescriptorListViewImplUiBinder.class);

    @UiField
    HTMLPanel elementDescriptorViewContainer;

    @UiField
    Button addObjectButton;

    private final List<ObjectListViewHolder> views = new ArrayList<>();

    @Inject
    public ObjectListViewImpl(MessageBox messageBox,
                              FormsMessages formsMessages) {
        this.messageBox = messageBox;
        this.formsMessages = formsMessages;
        initWidget(ourUiBinder.createAndBindUi(this));
        addObjectButton.addClickHandler(event -> addObjectHandler.run());
    }

    @Override
    public void clear() {
        views.clear();
        elementDescriptorViewContainer.clear();
    }

    @Override
    public void addView(@Nonnull ObjectListViewHolder view) {
        views.add(view);
        elementDescriptorViewContainer.add(view);
        updateButtons();
    }

    @Override
    public void removeView(@Nonnull ObjectListViewHolder viewHolder) {
        views.remove(viewHolder);
        elementDescriptorViewContainer.remove(viewHolder);
        updateButtons();
    }

    @Override
    public void moveUp(@Nonnull ObjectListViewHolder viewHolder) {
        int fromIndex = views.indexOf(viewHolder);
        int toIndex = fromIndex - 1;
        if(toIndex > -1) {
            Collections.swap(views, fromIndex, toIndex);
            refill(viewHolder);
        }
    }

    @Override
    public void moveDown(@Nonnull ObjectListViewHolder viewHolder) {
        int fromIndex = views.indexOf(viewHolder);
        int toIndex = fromIndex + 1;
        if(toIndex < views.size()) {
            Collections.swap(views, fromIndex, toIndex);
            refill(viewHolder);
        }
    }

    @Override
    public void setAddObjectText(@Nonnull String addObjectText) {
        addObjectButton.setText(checkNotNull(addObjectText));
    }


    @Override
    public void setAddObjectHandler(Runnable handler) {
        this.addObjectHandler = checkNotNull(handler);
    }

    private void updateButtons() {
        for(int i = 0; i < views.size(); i++) {
            ObjectListViewHolder holder = views.get(i);
            holder.setMiddle();
            if(i == 0) {
                holder.setFirst();
            }
            if(i == views.size() - 1) {
                holder.setLast();
            }
        }
    }

    private void refill(ObjectListViewHolder viewHolder) {
        elementDescriptorViewContainer.clear();
        views.forEach(view -> elementDescriptorViewContainer.add(view));
        viewHolder.scrollIntoView();
        updateButtons();
    }

    @Override
    public void performDeleteElementConfirmation(String objectId,
                                                 @Nonnull Runnable deleteRunnable) {
        messageBox.showConfirmBox(formsMessages.deleteFormElementConfirmation_Title(objectId),
                                  formsMessages.deleteFormElementConfirmation_Message(objectId),
                                  DialogButton.NO,
                                  DialogButton.DELETE,
                                  deleteRunnable,
                                  DialogButton.NO);
    }
}
