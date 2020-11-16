package edu.stanford.bmir.protege.web.client.merge_add;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.merge.MergeClientBundle;

public class EnterOntologyIRIViewImpl extends Composite implements EnterOntologyIRIView, HasInitialFocusable {

    interface EnterOntologyIRIViewImplUiBinder extends UiBinder<HTMLPanel, EnterOntologyIRIViewImpl> {
    }

    private static EnterOntologyIRIViewImplUiBinder ourUiBinder = GWT.create(EnterOntologyIRIViewImplUiBinder.class);

    @UiField
    protected TextArea ontologyIRI;

    public EnterOntologyIRIViewImpl() {
        MergeClientBundle.BUNDLE.style().ensureInjected();
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public String getOntologyIRI(){
        return ontologyIRI.getText().trim().replace(' ','_');
    }

    @Override
    public java.util.Optional<HasRequestFocus> getInitialFocusable() {
        return java.util.Optional.of(() -> ontologyIRI.setFocus(true));
    }
}
