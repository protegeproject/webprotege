package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.shared.form.FormId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
public class FormsManagerViewImpl extends Composite implements FormsManagerView {

    interface FormsManagerViewImplUiBinder extends UiBinder<HTMLPanel, FormsManagerViewImpl> {

    }

    private static FormsManagerViewImplUiBinder ourUiBinder = GWT.create(FormsManagerViewImplUiBinder.class);

    @UiField
    SimplePanel formDescriptorContainer;

    @UiField
    ListBox formSelector;

    @Inject
    public FormsManagerViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFormDescriptorContainer() {
        return formDescriptorContainer;
    }

    @Override
    public void clear() {
    }

    @Override
    public void setFormIds(@Nonnull List<FormId> formIds) {
        formSelector.clear();
        formIds.stream()
               .map(FormId::getId)
               .forEach(formSelector::addItem);
        if(formSelector.getItemCount() > 0) {
            formSelector.setSelectedIndex(0);
        }
    }

    @Override
    public void setCurrentFormId(@Nonnull FormId formId) {
        for(int i = 0; i < formSelector.getItemCount(); i++) {
            if(formSelector.getValue(i).equals(formId.getId())) {
                formSelector.setSelectedIndex(i);
                return;
            }
        }
    }
}
