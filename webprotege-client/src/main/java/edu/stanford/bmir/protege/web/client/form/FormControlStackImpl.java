package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueEditorFactory;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorDirection;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorPresenter;
import edu.stanford.bmir.protege.web.client.pagination.PaginatorView;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.FormPageRequest.SourceType;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageChangedHandler;
import edu.stanford.bmir.protege.web.shared.form.FormRegionPageRequest;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.FormSubject;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionId;
import edu.stanford.bmir.protege.web.shared.form.field.Repeatability;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormControlStackImpl extends Composite implements FormControlStack {

    interface FormControlStackImplUiBinder extends UiBinder<HTMLPanel, FormControlStackImpl> {

    }

    private static FormControlStackImplUiBinder ourUiBinder = GWT.create(FormControlStackImplUiBinder.class);



    @UiField
    SimplePanel editorHolder;

    @Nonnull
    private final PaginatorPresenter paginatorPresenter;

    @UiField(provided = true)
    PaginatorView paginatorView;

    @Nonnull
    private FormRegionPageChangedHandler formRegionPageChangedHandler = () -> {};

    @Nonnull
    private final FormControlStackBackingEditor delegateEditor;

    public FormControlStackImpl(@Nonnull ValueEditorFactory<FormControlData> editorFactory,
                                @Nonnull Repeatability repeatability,
                                @Nonnull FormRegionPosition formRegionPosition,
                                @Nonnull PaginatorPresenter paginatorPresenter) {
        this.paginatorPresenter = paginatorPresenter;
        this.paginatorView = paginatorPresenter.getView();
        paginatorPresenter.setPageNumberChangedHandler(pn -> formRegionPageChangedHandler.handleFormRegionPageChanged());
        initWidget(ourUiBinder.createAndBindUi(this));
        ValueEditorFactory<FormControlData> valueEditorFactory = () -> {
            ValueEditor<FormControlData> editor = editorFactory.createEditor();
            FormControl formControl = (FormControl) editor;
            formControl.setRegionPosition(formRegionPosition);
            return editor;
        };
        if(repeatability == Repeatability.REPEATABLE_HORIZONTALLY || repeatability == Repeatability.REPEATABLE_VERTICALLY) {
            ValueListFlexEditorImpl<FormControlData> delegate = new ValueListFlexEditorImpl<>(valueEditorFactory);
            delegateEditor = new RepeatingFormControlStackBackingEditor(delegate);
            if (repeatability == Repeatability.REPEATABLE_HORIZONTALLY) {
                delegate.setDirection(ValueListFlexEditorDirection.ROW);
            }
            else {
                delegate.setDirection(ValueListFlexEditorDirection.COLUMN);
            }
        }
        else {
            delegateEditor = new NonRepeatingFormControlStackBackingEditor((FormControl) valueEditorFactory.createEditor());
        }
        editorHolder.setWidget(delegateEditor);
    }

    @Override
    public void requestFocus() {
        delegateEditor.requestFocus();
    }

    @Nonnull
    @Override
    public ImmutableList<FormRegionPageRequest> getPageRequests(FormSubject formSubject,
                                                                FormRegionId formRegionId) {

        // Page requests for this stack
        int pageNumber = paginatorPresenter.getPageNumber();
        int pageSize = paginatorPresenter.getPageSize();
        PageRequest pageRequest = PageRequest.requestPageWithSize(pageNumber, pageSize);

        FormRegionPageRequest formRegionPageRequest = FormRegionPageRequest.get(formSubject,
                                                                                formRegionId,
                                                                                SourceType.CONTROL_STACK, pageRequest);

        // Page requests for sub-regions
        ImmutableList.Builder<FormRegionPageRequest> resultBuilder = ImmutableList.builder();
        resultBuilder.add(formRegionPageRequest);
        delegateEditor.forEachFormControl(formControl -> {
            resultBuilder.addAll(formControl.getPageRequests(formSubject, formRegionId));
        });
        return resultBuilder.build();
    }

    @Override
    public void setEnabled(boolean enabled) {
        delegateEditor.setEnabled(enabled);
    }

    @Override
    public void setValue(List<FormControlData> object) {
        delegateEditor.setValue(object);
        delegateEditor.forEachFormControl(formControl -> formControl.setFormRegionPageChangedHandler(
                formRegionPageChangedHandler));
    }

    @Override
    public void clearValue() {
        delegateEditor.clearValue();
    }

    @Override
    public Optional<List<FormControlData>> getValue() {
        return delegateEditor.getValue();
    }

    @Override
    public boolean isDirty() {
        return delegateEditor.isDirty();
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return delegateEditor.addDirtyChangedHandler(handler);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<List<FormControlData>>> handler) {
        return delegateEditor.addValueChangeHandler(handler);
    }

    @Override
    public boolean isWellFormed() {
        return delegateEditor.isWellFormed();
    }

    @Override
    public void setPageCount(int pageCount) {
        paginatorPresenter.setPageCount(pageCount);
        paginatorView.setVisible(pageCount > 1);
    }

    @Override
    public void setPageNumber(int pageNumber) {
        paginatorPresenter.setPageNumber(pageNumber);
    }

    @Override
    public int getPageNumber() {
        return paginatorPresenter.getPageNumber();
    }

    @Override
    public void setPageNumberChangedHandler(PageNumberChangedHandler handler) {
        paginatorPresenter.setPageNumberChangedHandler(handler);
    }

    @Override
    public void setFormRegionPageChangedHandler(@Nonnull FormRegionPageChangedHandler handler) {
        this.formRegionPageChangedHandler = checkNotNull(handler);
    }
}
