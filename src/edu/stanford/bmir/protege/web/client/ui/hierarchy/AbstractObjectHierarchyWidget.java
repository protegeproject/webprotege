package edu.stanford.bmir.protege.web.client.ui.hierarchy;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.*;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/03/2013
 */
public class AbstractObjectHierarchyWidget<N> extends Composite {
//
//

    private SelectionModel<OWLEntity> selectionModel = new SingleSelectionModel<OWLEntity>();

    private CellTree tree;

    public AbstractObjectHierarchyWidget(CellTree tree) {
        this.tree = tree;

    }

    //
//    public AbstractObjectHierarchyWidget() {
//        TreeViewModel hierarchyTreeViewModel = new TreeViewModel() {
//            @Override
//            public <T> NodeInfo<?> getNodeInfo(T value) {
//                return new OWLEntityNodeInfo(value);
//            }
//
//            @Override
//            public boolean isLeaf(Object value) {
//                return false;
//            }
//        };
//        AbstractDataProvider<OWLEntity> dataProvider = new ListDataProvider<OWLEntity>();
//
//        this.tree = new CellTree(hierarchyTreeViewModel, );
//    }
//
//
//
//    private class OWLEntityNodeInfo extends TreeViewModel.DefaultNodeInfo<OWLEntity> {
//
//        private OWLEntityNodeInfo(AbstractDataProvider<OWLEntity> dataProvider, Cell<OWLEntity> cell) {
//            super(dataProvider, cell);
//        }
//    }
//
//
//    private class MyDataProvider extends AsyncDataProvider<OWLEntity> {
//
//        @Override
//        protected void onRangeChanged(HasData<OWLEntity> display) {
//            updateRowCount();
//        }
//    }

}
