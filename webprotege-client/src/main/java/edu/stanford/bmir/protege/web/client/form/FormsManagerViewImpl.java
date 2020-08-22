package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.FormsMessages;
import edu.stanford.bmir.protege.web.client.library.dlg.DialogButton;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.list.ListBox;
import edu.stanford.bmir.protege.web.shared.form.FormId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-17
 */
public class FormsManagerViewImpl extends Composite implements FormsManagerView {

    private CopyFormsFromProjectHandler copyFormsFromProjectHandler = () -> {};

    @UiField
    protected SimplePanel formsListContainer;

    private ExportFormsHander exportFormsHandler = () -> {};

    interface FormsManagerViewImplUiBinder extends UiBinder<HTMLPanel, FormsManagerViewImpl> {

    }

    private static FormsManagerViewImplUiBinder ourUiBinder = GWT.create(FormsManagerViewImplUiBinder.class);

    @UiField
    Button copyFormsFromProjectButton;

    @UiField
    Button exportFormsFromProjectButton;

    @Nonnull
    private final MessageBox messageBox;

    @Nonnull
    private final FormsMessages formsMessages;

    @Inject
    public FormsManagerViewImpl(@Nonnull MessageBox messageBox,
                                @Nonnull FormsMessages formsMessages) {
        this.messageBox = messageBox;
        this.formsMessages = formsMessages;
        initWidget(ourUiBinder.createAndBindUi(this));
        copyFormsFromProjectButton.addClickHandler(this::handleCopyFormsFromProject);
        exportFormsFromProjectButton.addClickHandler(this::handleExportForms);
    }

    private void handleExportForms(ClickEvent event) {
        exportFormsHandler.handleExportForms();
    }


    @Override
    public void clear() {
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFormsListContainer() {
        return formsListContainer;
    }


    private void handleCopyFormsFromProject(ClickEvent event) {
        copyFormsFromProjectHandler.handleCopyFromsFromProject();
    }

    @Override
    public void setCopyFormsFromProjectHandler(@Nonnull CopyFormsFromProjectHandler handler) {
        this.copyFormsFromProjectHandler = checkNotNull(handler);
    }

    @Override
    public void setExportFormsHandler(@Nonnull ExportFormsHander exportFormsHandler) {
        this.exportFormsHandler = checkNotNull(exportFormsHandler);
    }
}
