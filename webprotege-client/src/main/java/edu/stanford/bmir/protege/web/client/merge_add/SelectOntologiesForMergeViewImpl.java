package edu.stanford.bmir.protege.web.client.merge_add;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import org.semanticweb.owlapi.model.OWLOntologyID;

import java.util.ArrayList;
import java.util.List;

public class SelectOntologiesForMergeViewImpl extends Composite implements SelectOntologiesForMergeView, HasInitialFocusable {

    interface SelectOntologiesForMergeViewImplUiBinder extends UiBinder<HTMLPanel, SelectOntologiesForMergeViewImpl>{
    }

    private static SelectOntologiesForMergeViewImplUiBinder ourUiBinder = GWT.create(SelectOntologiesForMergeViewImplUiBinder.class);

    @UiField
    protected ListBox listBox;

    private List<OWLOntologyID> idList;

    public SelectOntologiesForMergeViewImpl(List<OWLOntologyID> list) {
        MergeAddClientBundle.BUNDLE.style().ensureInjected();
        initWidget(ourUiBinder.createAndBindUi(this));
        this.idList = list;
        listBox.setMultipleSelect(true);
        for (OWLOntologyID id: list){
            if(id.getOntologyIRI().isPresent())
                listBox.addItem(id.getOntologyIRI().get().toString());
        }
    }

    @Override
    public List<OWLOntologyID> getSelectedOntologies(){
        List<OWLOntologyID>  list = new ArrayList<>();
        for (int i=0;i<listBox.getItemCount();i++){
            if(listBox.isItemSelected(i)){
                list.add(idList.get(i));
                GWT.log(idList.get(i).toString());
            }
        }
        return list;
    }

    @Override
    public java.util.Optional<HasRequestFocus> getInitialFocusable() {
        return java.util.Optional.of(() -> listBox.setFocus(true));
    }
}
