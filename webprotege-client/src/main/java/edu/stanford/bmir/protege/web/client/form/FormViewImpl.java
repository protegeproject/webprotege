package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.form.field.ElementRun;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;

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

    private List<FormElementView> elementViews = new ArrayList<>();

    @Inject
    public FormViewImpl(Provider<FormViewRow> rowProvider) {
        this.rowProvider = rowProvider;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void addFormElementView(FormElementView view,
                                   ElementRun elementRun) {

        if(currentRow == null) {
            createAndAddNewRow();
        }
        else if(elementRun.isStart()) {
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
    public List<FormElementView> getElementViews() {
        return new ArrayList<>(elementViews);
    }

    @Override
    public void clear() {
        holder.clear();
    }
}
