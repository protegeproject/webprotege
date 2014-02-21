package edu.stanford.bmir.protege.web.client.ui.ontology.classes;

import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.FitLayout;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.ontology.hierarchy.ParentsPanel;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.tab.AbstractTab;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

import java.util.ArrayList;
import java.util.Collection;

public class SuperclassesPortlet extends AbstractOWLEntityPortlet {

    private ParentsPanel parentsPanel;

    public SuperclassesPortlet(Project project) {
        super(project);
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
    public void reload() {
        String entityDisplayText = UIUtil.getDisplayText(getEntity());
        if (entityDisplayText.length() > 63) {
            entityDisplayText = entityDisplayText.substring(0, 60) + "...";
        }
        setTitle(getEntity() == null ? "Parents" : "Parents of " + entityDisplayText);
        parentsPanel.setClsEntity(getEntity());
    }

    /*
     * Always returns an empty collection.
     */
    public Collection<EntityData> getSelection() {
        return new ArrayList<EntityData>();
    }

    @Override
    public void setSelection(Collection<EntityData> newSelection) {
        AbstractTab containerTab = getTab();
        if (containerTab != null) {
            containerTab.setSelection(newSelection);
        }
    }
}
