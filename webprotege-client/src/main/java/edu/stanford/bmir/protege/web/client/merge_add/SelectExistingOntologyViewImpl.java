package edu.stanford.bmir.protege.web.client.merge_add;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.shared.project.OntologyDocumentId;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.ArrayList;
import java.util.List;

public class SelectExistingOntologyViewImpl extends Composite implements SelectExistingOntologyView, HasInitialFocusable {
    interface SelectExistingOntologyViewImplUiBinder extends UiBinder<HTMLPanel, SelectExistingOntologyViewImpl>{
    }

    private static SelectExistingOntologyViewImplUiBinder ourUiBinder = GWT.create(SelectExistingOntologyViewImplUiBinder.class);

    @UiField
    protected ListBox listBox;

    private List<OntologyDocumentId> idList = new ArrayList<>();

    public SelectExistingOntologyViewImpl(List<OntologyDocumentId> idList) {
        MergeAddClientBundle.BUNDLE.style().ensureInjected();
        initWidget(ourUiBinder.createAndBindUi(this));
        this.idList.addAll(idList);
        listBox.setMultipleSelect(false);
        for (OntologyDocumentId id : idList) {
            listBox.addItem(id.getId());
        }
    }

    @Override
    public OntologyDocumentId getOntology(){
        for (int i=0;i<listBox.getItemCount();i++){
            if(listBox.isItemSelected(i)){
                return idList.get(i);
            }
        }
        return null;
    }

    @Override
    public java.util.Optional<HasRequestFocus> getInitialFocusable() {
        return java.util.Optional.of(() -> listBox.setFocus(true));
    }
}
