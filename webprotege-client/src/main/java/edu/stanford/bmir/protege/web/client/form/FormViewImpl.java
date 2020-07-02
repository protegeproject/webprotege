package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.shared.form.field.FieldRun;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public class FormViewImpl extends Composite implements FormView {

    private final Provider<FormViewRow> rowProvider;

    interface FormViewImplUiBinder extends UiBinder<HTMLPanel, FormViewImpl> {

    }

    private static FormViewImplUiBinder ourUiBinder = GWT.create(FormViewImplUiBinder.class);

    @UiField
    HTMLPanel holder;

    private FormViewRow currentRow;

    private List<FormFieldView> elementViews = new ArrayList<>();

    @Inject
    public FormViewImpl(Provider<FormViewRow> rowProvider) {
        this.rowProvider = rowProvider;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void addFormElementView(FormFieldView view,
                                   FieldRun fieldRun) {

        if(currentRow == null) {
            createAndAddNewRow();
        }
        else if(fieldRun.isStart()) {
            createAndAddNewRow();
        }
        currentRow.add(view);
        elementViews.add(view);
    }

    private void createAndAddNewRow() {
        currentRow = rowProvider.get();
        holder.add(currentRow);
    }

    @Override
    public List<FormFieldView> getFieldViews() {
        return new ArrayList<>(elementViews);
    }

    @Override
    public void clear() {
        holder.clear();
    }

    @Override
    public void requestFocus() {
        if(elementViews.isEmpty()) {
            return;
        }
        elementViews.get(0).getEditor().requestFocus();
    }
}
