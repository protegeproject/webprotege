package edu.stanford.bmir.protege.web.client.ui.ontology.hierarchy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.layout.ColumnLayout;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.ui.icd.ICDClassTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.ontology.classes.ClassTreePortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.EntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.selection.Selectable;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;


public class ParentsPanel extends Panel {

    private Project project;
    private Panel parentsPanel;
    private Map<EntityData, HTML> entityToParentPanelMap = new HashMap<EntityData, HTML>();
    private boolean showRemove;
    private boolean showAdd;
    private boolean showLabel;
    private boolean clickableParents;
    private EntityPortlet containerPortlet = null;
    private Window selectWindow;
    private Selectable selectable;
    private EntityData cls;
    private Set<EntityData> initialParents = new HashSet<EntityData>();
    private HandlerRegistration addParentHandlerRegistration;
    private HandlerRegistration parentActionHandlerRegistration;
    private ClickHandler parentActionHandler;
    private String topClass;

    public ParentsPanel(Project project) {
        this(project, true, true, true, true, false);
        setHeight(70);
    }

    public ParentsPanel(Project project, boolean showAdd, boolean showRemove) {
        this(project, showAdd, showRemove, true, true, false);
        setHeight(70);
    }

    public ParentsPanel(Project project, boolean showAdd, boolean showRemove, boolean showLabel, boolean showBorder, boolean clickableParents) {
        super();
        this.project = project;
        this.showAdd = showAdd;
        this.showRemove = showRemove;
        this.showLabel = showLabel;
        setClickableParents(clickableParents);

        setLayout(new ColumnLayout());
        if (this.showLabel) {
            add(getLabelPanel());
        }
        add(parentsPanel=createParentsPanel(showBorder), new ColumnLayoutData(1));

        setMargins(0, 0, 0, 15);
    }

    @Override
    public void setHeight(int height) {
        parentsPanel.setHeight(height);
        super.setHeight(height);
    }

    @Override
    public void setHeight(String height) {
        parentsPanel.setHeight(height);
        super.setHeight(height);
    }

    public void setClickableParents(boolean clickableParents){
        this.clickableParents = clickableParents;
    }
    
    public void setContainerPortlet(EntityPortlet containerPortlet) {
        this.containerPortlet  = containerPortlet;
    }
    
    protected Panel getLabelPanel() {
        Panel labelPanel = new Panel();
        labelPanel.add(new Label("Parents:"));
        if (showAdd) {
//            Hyperlink hl = new Hyperlink("<a href=\"" + UIUtil.LOCAL + UIUtil.ADD_PREFIX +
//                    "\" style=\"text-decoration: underline;\">Add parent(s)</a>", "");
            Hyperlink hl = new Hyperlink("<u>Add parent(s)</u>", true, "");
            labelPanel.add(hl);
            addParentHandlerRegistration = hl.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    onAdd();
                }
            });
            labelPanel.add(hl);
        }
        labelPanel.setBaseCls("parents-label");
        return labelPanel;
    }


    protected Panel createParentsPanel(boolean showBorder) {
        Panel pPanel = new Panel();
        pPanel.setBaseCls(showBorder ? "parents" : "parents-no-border");
        pPanel.setAutoScroll(true);
        return pPanel;
    }

    public Panel getParentsPanel() {
        return parentsPanel;
    }

    protected ClickHandler getParentActionHandler() {
        if (parentActionHandler == null) {
            parentActionHandler = new ClickHandler() {
                public void onClick(ClickEvent event) {
                    EventTarget eventTarget = event.getNativeEvent().getEventTarget();
                    Element target = eventTarget.cast();
                    GWT.log("In generic handler: target of click: " + target.getId(), null);
                    if ("a".equalsIgnoreCase(getTagName(target))) {
                        String href = DOM.getElementAttribute(target, "href");
                        final String local_remove_prefix = UIUtil.LOCAL + UIUtil.REMOVE_PREFIX;
                        final String local_goto_prefix = UIUtil.LOCAL + UIUtil.GOTO_PREFIX;
                        if (href.contains(local_remove_prefix)) {
                            onRemove(URL.decodeComponent(
                                    href.substring(
                                            href.indexOf(local_remove_prefix) +
                                            local_remove_prefix.length() ) ));
                        }
                        else if (href.contains(local_goto_prefix)) {
                            if(containerPortlet != null) {
                                String parentName = URL.decodeComponent(
                                        href.substring(
                                                href.indexOf(local_goto_prefix) +
                                                local_goto_prefix.length() ) );
                                containerPortlet.setSelection(UIUtil.createCollection(new EntityData(parentName)));
                            }
                        }

                    }
                }
            };
        }
        return parentActionHandler;
    }

    native String getTagName(Element element)
    /*-{
        return element.tagName;
    }-*/;


    public void setClsEntity(EntityData classEntity) {
        this.cls = classEntity;
        getParents();
    }

    public EntityData getClsEntity() {
        return cls;
    }

    protected void getParents() {
        if (cls == null) {
            updatePanel(new ArrayList<EntityData>());
        } else {
            OntologyServiceManager.getInstance().getParents(project.getProjectName(), cls.getName(), true, new GetParentsHandler());
        }
    }

    protected void updatePanel(Collection<EntityData> parents) {
        parentsPanel.clear();
        entityToParentPanelMap.clear();
        for (EntityData parent : parents) {
            HTML parentHtml = getHTML(parent);
            parentsPanel.add(parentHtml);
            entityToParentPanelMap.put(parent, parentHtml);
        }
        parentsPanel.doLayout();
    }


    protected HTML getHTML(EntityData parent) {
        String buffer = new String();
        buffer += "<table width=\"100%\" border=\"0\" cellspacing=\"3\"  class=\"restriction_table\">";
        buffer += "<tr>";
        if (clickableParents) {
            buffer += "<td class=\"parent-link\"><a href=\"";
            buffer += UIUtil.LOCAL;
            buffer += UIUtil.GOTO_PREFIX;
            buffer += URL.encodeComponent(parent.getName());
            buffer += "\">" + UIUtil.getDisplayText(parent) + "</a></td>";
        }
        else {
            buffer += "<td>";
            buffer += UIUtil.getDisplayText(parent);
            buffer += "</td>";
        }
        if (showRemove) {
            buffer += "<td class=\"parent-column-right\"><a href=\"";
            buffer += UIUtil.LOCAL;
            buffer += UIUtil.REMOVE_PREFIX;
            buffer += URL.encodeComponent(parent.getName());
            buffer += "\">remove</a></td>";
        }
        buffer += "</tr></table>";
        HTML html = new HTML(buffer);
        parentActionHandlerRegistration = html.addClickHandler(getParentActionHandler());
        return html;
    }

    protected void onRemove(String entityNameToRemove) {
        GWT.log("To remove: " + entityNameToRemove, null);
        EntityData entityToRemove = getEntityData(entityNameToRemove);
        HTML htmlPanel = entityToParentPanelMap.get(entityToRemove);
        if (htmlPanel != null) {
            parentsPanel.remove(htmlPanel);
            entityToParentPanelMap.remove(entityToRemove);
            parentsPanel.doLayout();
        }
    }

    protected void onAdd() {
        // Collection<EntityData> selection = SelectionUtil.selectClses(project, true); //does not work
        selectNewParents();
    }

    protected void selectNewParents() {
        selectWindow = getSelectionWindow();
        if (!selectWindow.isVisible()) {
            selectWindow.show();
            selectWindow.center();
        }
    }

    private EntityData getEntityData(String name) {
        for (EntityData entity : entityToParentPanelMap.keySet()) {
            if (entity.getName().equals(name)) {
                return entity;
            }
        }
        return null;
    }

    public Set<EntityData> getParentsToRemove() {
        Set<EntityData> parentsToRemove = new HashSet<EntityData>();
        Set<EntityData> keySet = entityToParentPanelMap.keySet();
        for (EntityData parent : initialParents) {
            if (!keySet.contains(parent)) {
                parentsToRemove.add(parent);
            }
        }
        return parentsToRemove;
    }

    public Set<EntityData> getParentsToAdd() {
        Set<EntityData> parentsToAdd = new HashSet<EntityData>();
        for (EntityData parent : entityToParentPanelMap.keySet()) {
            if (!initialParents.contains(parent)) {
                parentsToAdd.add(parent);
            }
        }
        return parentsToAdd;
    }

    public Set<EntityData> getFinalParents() {
        return entityToParentPanelMap.keySet();
    }

    public String getTopClass() {
        return topClass;
    }

    public void setTopClass(String topClass) {
        this.topClass = topClass;
    }

    @Override
    protected void onDestroy() {
        if (addParentHandlerRegistration != null) {
            addParentHandlerRegistration.removeHandler();
        }
        if (parentActionHandlerRegistration != null) {
            parentActionHandlerRegistration.removeHandler();
        }
        parentActionHandler = null;
        super.onDestroy();
    }

    /*
     * The selection code should be in a util class, but it did not work for some reason.
     * No time to debug.
     */

    protected Window getSelectionWindow() {
        if (selectWindow == null) {
            selectWindow = new com.gwtext.client.widgets.Window();
            selectWindow.setTitle("Select parents");
            selectWindow.setWidth(600);
            selectWindow.setHeight(480);
            selectWindow.setMinWidth(300);
            selectWindow.setMinHeight(350);
            selectWindow.setLayout(new FitLayout());
            selectWindow.setPaddings(5);
            selectWindow.setButtonAlign(Position.CENTER);

            selectWindow.setCloseAction(Window.HIDE);
            selectWindow.setPlain(true);

            com.gwtext.client.widgets.Button cancelButton = new com.gwtext.client.widgets.Button("Cancel");
            cancelButton.addListener(new ButtonListenerAdapter() {
                @Override
                public void onClick(Button button, EventObject e) {
                    selectWindow.hide();
                }
            });

            com.gwtext.client.widgets.Button selectButton = new com.gwtext.client.widgets.Button("Select");
            selectButton.addListener(new ButtonListenerAdapter() {
                @Override
                public void onClick(Button button, EventObject e) {
                    Collection<EntityData> selection = getSelectable().getSelection();
                    if (selection == null || selection.size() == 0) {
                        MessageBox.alert("No selection", "No class selected. Please select a class from the tree.");
                        return;
                    }
                    Collection<EntityData> allParents = new HashSet<EntityData>(entityToParentPanelMap.keySet());
                    allParents.addAll(selection);

                    updatePanel(allParents);
                    selectWindow.hide();
                }
            });

            selectWindow.add((Component)getSelectable());
            selectWindow.addButton(selectButton);
            selectWindow.addButton(cancelButton);
        }
        return selectWindow;
    }

    public Selectable getSelectable() {
        if (selectable == null) {
            //FIXME: ICD specific!!!
            ClassTreePortlet selectableTree = new ICDClassTreePortlet(project, true, false, false, true, topClass);
            selectableTree.setDraggable(false);
            selectableTree.setClosable(false);
            selectableTree.setCollapsible(false);
            selectableTree.setHeight(300);
            selectableTree.setWidth(450);
            selectable = selectableTree;
        }
        return selectable;
    }

    /*
     * End selection code
     */


    /*
     * Remote calls
     */

    class GetParentsHandler extends AbstractAsyncHandler<List<EntityData>> {

        @Override
        public void handleFailure(Throwable caught) {
            MessageBox.alert("Error", "There was an error at retrieving parents of " + UIUtil.getDisplayText(cls) + " from the server.");
            GWT.log("Error at retrieving parents of " + cls.getName() + " from server.", caught);
        }

        @Override
        public void handleSuccess(List<EntityData> parents) {
            initialParents = new HashSet<EntityData>(parents);
            updatePanel(parents);
        }

    }

}
