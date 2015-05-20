package edu.stanford.bmir.protege.web.client.ui.ontology.classes;

import com.google.common.base.Optional;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.ontology.hierarchy.ParentsPanel;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import java.util.Collection;

public class SuperclassesPortlet extends AbstractOWLEntityPortlet {

    private ParentsPanel parentsPanel;

    public SuperclassesPortlet(SelectionModel selectionModel, Project project) {
        super(selectionModel, project);
    }

    @Override
    public void initialize() {
       setTitle("Parents");
       setLayout(new FitLayout());
       parentsPanel = new ParentsPanel(getProjectId(), false, false, false, false, true);
       parentsPanel.setContainerPortlet(this);
       parentsPanel.setAutoScroll(false);
       Panel wrappingPanel = new Panel();
       wrappingPanel.add(parentsPanel.getParentsPanel());
       wrappingPanel.setAutoScroll(true);
       add(wrappingPanel);
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntityData> sel) {
        if(sel.isPresent()) {
            OWLEntityData entityData = sel.get();
            String entityDisplayText = entityData.getBrowserText();
            if (entityDisplayText.length() > 63) {
                entityDisplayText = entityDisplayText.substring(0, 60) + "...";
            }
            setTitle("Parents of " + entityDisplayText);
            // TODO: Re-implement
            throw new RuntimeException("This needs reimplementing");
//            parentsPanel.setClsEntity(getEntity());
        }
        else {
            setTitle("Parents");
        }
    }
}
