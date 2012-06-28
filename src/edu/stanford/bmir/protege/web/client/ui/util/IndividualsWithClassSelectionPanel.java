package edu.stanford.bmir.protege.web.client.ui.util;

import java.util.Collection;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.VerticalLayout;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.ontology.classes.ClassTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.individuals.IndividualsListPortlet;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionListener;

public class IndividualsWithClassSelectionPanel extends Panel {
    private Project project;
    private Collection<EntityData> clses;
    private boolean allowMultipleSelection;
    private boolean showClsesPanel;

    private ClassTreePortlet clsTreePortlet;
    private IndividualsListPortlet indListPorlet;
    private Anchor seeAllClassesAnchor;
    private boolean seeAllClasses = false;

    public IndividualsWithClassSelectionPanel(Project project, Collection<EntityData> clses, boolean allowMultipleSelection, boolean showClsesPanel) {
        this.project = project;
        this.clses = clses;
        this.allowMultipleSelection = allowMultipleSelection;
        this.showClsesPanel = showClsesPanel;
        buildUI();
    }

    protected void buildUI() {
        setLayout(new ColumnLayout());

        indListPorlet = createIndividualsListPorlet();
        add(indListPorlet, new ColumnLayoutData(showClsesPanel ? 0.5 : 1));

        if (showClsesPanel) {
            Panel clsesPanel = createClsesPanel();
            add(clsesPanel, new ColumnLayoutData(0.5));
        } else {
            indListPorlet.setEntity(UIUtil.getFirstItem(clses)); //TODO: handle multiple classes
        }
    }

    protected Panel createClsesPanel() {
        final Panel clsesPanel = new Panel();
        clsesPanel.setLayout(new VerticalLayout(5));
        clsesPanel.setAutoWidth(true);

        clsTreePortlet = createClassTreePortlet(seeAllClasses);
        seeAllClassesAnchor = createSeeAllClassesAnchor();

        clsesPanel.add(clsTreePortlet);
        clsesPanel.add(seeAllClassesAnchor);

        seeAllClassesAnchor.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                seeAllClasses = !seeAllClasses;
                remove(clsesPanel.getId());
                clsesPanel.removeFromParent();
                Panel clsesPanel1 = createClsesPanel();
                insert(0, clsesPanel1);
                doLayout();
            }
        });

        clsTreePortlet.addSelectionListener(new SelectionListener() {
            public void selectionChanged(SelectionEvent event) {
                indListPorlet.setEntity(clsTreePortlet.getSelection().get(0));
            }
        });

        return clsesPanel;
    }

    protected IndividualsListPortlet createIndividualsListPorlet() {
       IndividualsListPortlet indPortlet = new IndividualsListPortlet(project);
       indPortlet.setDraggable(false);
       indPortlet.setClosable(false);
       indPortlet.setCollapsible(false);
       indPortlet.setHeight(390);
        return indPortlet;
    }

    protected ClassTreePortlet createClassTreePortlet(boolean allClasses) {
        EntityData topCls = allClasses ? null : UIUtil.getFirstItem(clses);
        ClassTreePortlet clsPortlet = new ClassTreePortlet(project, true, true, true, allowMultipleSelection, topCls == null ?  null : topCls.getName());
        clsPortlet.setDraggable(false);
        clsPortlet.setClosable(false);
        clsPortlet.setCollapsible(false);
        clsPortlet.setHeight(390);
        clsPortlet.setWidth(375);
        return clsPortlet;
    }

    protected Anchor createSeeAllClassesAnchor() {
        Anchor anchor = new Anchor(getAnchorText(), true);
        return anchor;
    }

    protected void setAnchorText() {
        seeAllClassesAnchor.setText(getAnchorText());
    }

    protected String getAnchorText() {
        return seeAllClasses ? "Display only classes in the domain" : "Display all classes";
    }

    public Collection<EntityData> getSelection() {
        return indListPorlet.getSelection();
    }

}
