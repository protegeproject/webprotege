package edu.stanford.bmir.protege.web.client.ui.util;

import com.google.common.base.Optional;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.VerticalLayout;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.ontology.classes.ClassTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.individuals.IndividualsListPortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.LegacyCompatUtil;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;

import java.util.Collection;

public class IndividualsWithClassSelectionPanel extends Panel {
    private Project project;
    private Collection<EntityData> clses;
    private boolean allowMultipleSelection;
    private boolean showClsesPanel;

    private ClassTreePortlet clsTreePortlet;
    private IndividualsListPortlet indListPorlet;
    private Anchor seeAllClassesAnchor;
    private boolean seeAllClasses = false;

    private final EventBus eventBus;

    private final DispatchServiceManager dispatchServiceManager;

    public IndividualsWithClassSelectionPanel(Project project, EventBus eventBus, DispatchServiceManager dispatchServiceManager, Collection<EntityData> clses, boolean allowMultipleSelection, boolean showClsesPanel) {
        this.project = project;
        this.eventBus = eventBus;
        this.dispatchServiceManager = dispatchServiceManager;
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
            EntityData firstItem = UIUtil.getFirstItem(clses);
            Optional<OWLEntityData> firstItemAsOWLEntityData = LegacyCompatUtil.toOWLEntityData(firstItem);
            if (firstItemAsOWLEntityData.isPresent()) {
                indListPorlet.getSelectionModel().setSelection(firstItemAsOWLEntityData.get()); //TODO: handle multiple classes
            }
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

        // TODO: SELECTION
//        clsTreePortlet.addSelectionListener(new SelectionListener() {
//            public void selectionChanged(SelectionEvent event) {
//                indListPorlet.setEntity(clsTreePortlet.getSelection().get(0));
//            }
//        });

        return clsesPanel;
    }

    protected IndividualsListPortlet createIndividualsListPorlet() {
        SelectionModel selectionModel = SelectionModel.create();
       IndividualsListPortlet indPortlet = new IndividualsListPortlet(selectionModel, eventBus, dispatchServiceManager, project);
       indPortlet.setDraggable(false);
       indPortlet.setClosable(false);
       indPortlet.setCollapsible(false);
       indPortlet.setHeight(390);
        return indPortlet;
    }

    protected ClassTreePortlet createClassTreePortlet(boolean allClasses) {
        SelectionModel selectionModel = SelectionModel.create();
        EntityData topCls = allClasses ? null : UIUtil.getFirstItem(clses);
        ClassTreePortlet clsPortlet = new ClassTreePortlet(selectionModel, eventBus, dispatchServiceManager, project, true, true, true, allowMultipleSelection, topCls == null ?  null : topCls.getName());
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
