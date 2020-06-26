package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

import javax.inject.Inject;

public class FormControlStackViewImpl extends Composite implements FormControlStackView {

    interface FormControlStackViewImplUiBinder extends UiBinder<HTMLPanel, FormControlStackViewImpl> {
    }

    private static FormControlStackViewImplUiBinder ourUiBinder = GWT.create(FormControlStackViewImplUiBinder.class);

    @UiField
    SimplePanel container;

    @UiField
    SimplePanel paginatorContainer;

    @Inject
    public FormControlStackViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setPaginatorVisible(boolean visible) {

    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {

    }

    @Override
    public void requestFocus() {
        Widget widget = container.getWidget();
        if(widget instanceof HasRequestFocus) {
            ((HasRequestFocus) widget).requestFocus();
        }
    }

    @Override
    public void setVerticalLayout() {
        container.removeStyleName(WebProtegeClientBundle.BUNDLE.style().formStackHorizontal());
        container.addStyleName(WebProtegeClientBundle.BUNDLE.style().formStackVertical());
    }

    @Override
    public void setHorizontalLayout() {
        container.addStyleName(WebProtegeClientBundle.BUNDLE.style().formStackHorizontal());
        container.removeStyleName(WebProtegeClientBundle.BUNDLE.style().formStackVertical());
    }
}
