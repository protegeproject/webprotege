package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
public class FormElementDescriptorListViewImpl extends Composite implements FormElementDescriptorListView {

    private final MessageBox messageBox;

    private final FormsMessages formsMessages;

    interface FormElementDescriptorListViewImplUiBinder extends UiBinder<HTMLPanel, FormElementDescriptorListViewImpl> {

    }

    private static FormElementDescriptorListViewImplUiBinder ourUiBinder = GWT.create(
            FormElementDescriptorListViewImplUiBinder.class);

    @UiField
    HTMLPanel elementDescriptorViewContainer;

    private final List<FormElementDescriptorViewHolder> views = new ArrayList<>();

    @Inject
    public FormElementDescriptorListViewImpl(MessageBox messageBox,
                                             FormsMessages formsMessages) {
        this.messageBox = messageBox;
        this.formsMessages = formsMessages;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void clear() {
        views.clear();
        elementDescriptorViewContainer.clear();
    }

    @Override
    public void addView(@Nonnull FormElementDescriptorViewHolder view) {
        views.add(view);
        elementDescriptorViewContainer.add(view);
        updateButtons();
    }

    @Override
    public void removeView(@Nonnull FormElementDescriptorViewHolder viewHolder) {
        views.remove(viewHolder);
        elementDescriptorViewContainer.remove(viewHolder);
        updateButtons();
    }

    @Override
    public void moveUp(@Nonnull FormElementDescriptorViewHolder viewHolder) {
        int fromIndex = views.indexOf(viewHolder);
        int toIndex = fromIndex - 1;
        if(toIndex > -1) {
            Collections.swap(views, fromIndex, toIndex);
            refill(viewHolder);
        }
    }

    @Override
    public void moveDown(@Nonnull FormElementDescriptorViewHolder viewHolder) {
        int fromIndex = views.indexOf(viewHolder);
        int toIndex = fromIndex + 1;
        if(toIndex < views.size()) {
            Collections.swap(views, fromIndex, toIndex);
            refill(viewHolder);
        }
    }

    private void updateButtons() {
        for(int i = 0; i < views.size(); i++) {
            FormElementDescriptorViewHolder holder = views.get(i);
            holder.setMiddle();
            if(i == 0) {
                holder.setFirst();
            }
            if(i == views.size() - 1) {
                holder.setLast();
            }
        }
    }

    private void refill(FormElementDescriptorViewHolder viewHolder) {
        elementDescriptorViewContainer.clear();
        views.forEach(view -> elementDescriptorViewContainer.add(view));
        viewHolder.scrollIntoView();
        updateButtons();
    }

    @Override
    public void performDeleteElementConfirmation(FormElementId formElementId,
                                                 @Nonnull Runnable deleteRunnable) {
        messageBox.showConfirmBox(formsMessages.deleteFormElementConfirmation_Title(formElementId.getId()),
                                  formsMessages.deleteFormElementConfirmation_Message(formElementId.getId()),
                                  DialogButton.NO,
                                  DialogButton.DELETE,
                                  deleteRunnable,
                                  DialogButton.NO);
    }
}
