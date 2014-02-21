package edu.stanford.bmir.protege.web.client.ui.ontology.individuals;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.ontology.classes.ClassTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.EntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionListener;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;

import java.util.Collection;
import java.util.List;

/**
 * A single view that shows the classes in an ontology.
 *
 * @author Jennifer Vendetti <vendetti@stanford.edu>
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class IndividualsTab extends AbstractTab {

    private ClassTreePortlet clsTreePortlet;
    private IndividualsListPortlet indListPorlet;

    public IndividualsTab(Project project) {
        super(project);
    }

    @Override
    public void setup() {
        super.setup();

        clsTreePortlet = (ClassTreePortlet) getPortletByClassName(ClassTreePortlet.class.getName());
        indListPorlet = (IndividualsListPortlet) getPortletByClassName(IndividualsListPortlet.class.getName());

        setControllingPortlet(indListPorlet);

        if (clsTreePortlet != null && indListPorlet != null) {
            clsTreePortlet.addSelectionListener(new SelectionListener() {
                public void selectionChanged(SelectionEvent event) {
                    List<EntityData> clsSel = clsTreePortlet.getSelection();
                    if (!clsSel.isEmpty()) {
                        EntityData selection = clsSel.get(0);
                        clsTreePortlet.setEntity(selection); //might cause later infinite cycles, if anything will happen in setEntity
                        Collection<EntityPortlet> portlets = getPortlets();
                        for (EntityPortlet portlet : portlets) {
                            portlet.setEntity(selection);
                        }
                    }

                }
            });
        }
    }


    //FIXME: To be improved
    @Override
    public void setSelection(Collection<EntityData> selection) {
        if (selection == null || selection.size() == 0) {
            return;
        }

        /*
         * FIXME: We need a mechanism that works for the selection of classes and instances.
         * For now, just be silly: try to select the class and also try to select the instance.
         * One of these might succeed.
         */

        // Try class selection, first
        clsTreePortlet.setSelection(selection);

        // Then, try the instance selection

        //TODO: support multiple selection
        final EntityData individual = selection.iterator().next();
        OntologyServiceManager.getInstance().getDirectTypes(this.project.getProjectId(), individual.getName(),
                new AbstractAsyncHandler<List<EntityData>>() {
            @Override
            public void handleFailure(Throwable caught) {
                GWT.log("Could not select " + individual);
            }

            @Override
            public void handleSuccess(final List<EntityData> types) {
                clsTreePortlet.setSelection(types);
            }
        });
    }
}
