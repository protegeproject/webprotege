package edu.stanford.bmir.protege.web.client.merge_add;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;

public class SelectOptionViewImpl extends Composite implements SelectOptionView, HasInitialFocusable {

    interface SelectOptionViewImplUiBinder extends UiBinder<HTMLPanel, SelectOptionViewImpl> {
    }

    private static SelectOptionViewImplUiBinder ourUiBinder = GWT.create(SelectOptionViewImplUiBinder.class);

    @UiField
    protected RadioButton radioButton1;

    @UiField
    protected RadioButton radioButton2;

    public SelectOptionViewImpl(){
//        radioButton1 = new RadioButton("Group","Merge into new ontology");
//        radioButton2 = new RadioButton("Group", "Merge into existing ontology");
        MergeAddClientBundle.BUNDLE.style().ensureInjected();
        initWidget(ourUiBinder.createAndBindUi(this));
        radioButton1.setValue(true);
        radioButton2.setValue(false);
    }

    @Override
    public int getSelectedOption() {
        if(radioButton1.getValue()) return 1;
        else return 2;
    }

    @Override
    public java.util.Optional<HasRequestFocus> getInitialFocusable() {
        return java.util.Optional.of(() -> radioButton1.setFocus(true));
    }
}
