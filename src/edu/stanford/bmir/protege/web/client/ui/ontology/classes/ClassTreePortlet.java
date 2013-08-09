package edu.stanford.bmir.protege.web.client.ui.ontology.classes;

import java.util.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Node;
import com.gwtext.client.dd.DragData;
import com.gwtext.client.dd.DragDrop;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.CycleButton;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Tool;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.CheckItem;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtext.client.widgets.menu.event.BaseItemListener;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtext.client.widgets.menu.event.CheckItemListener;
import com.gwtext.client.widgets.menu.event.CheckItemListenerAdapter;
import com.gwtext.client.widgets.tree.DefaultSelectionModel;
import com.gwtext.client.widgets.tree.DropNodeCallback;
import com.gwtext.client.widgets.tree.MultiSelectionModel;
import com.gwtext.client.widgets.tree.TreeNode;
import com.gwtext.client.widgets.tree.TreePanel;
import com.gwtext.client.widgets.tree.TreeSelectionModel;
import com.gwtext.client.widgets.tree.event.DefaultSelectionModelListenerAdapter;
import com.gwtext.client.widgets.tree.event.MultiSelectionModelListener;
import com.gwtext.client.widgets.tree.event.TreeNodeListenerAdapter;
import com.gwtext.client.widgets.tree.event.TreePanelListenerAdapter;

import edu.stanford.bmir.protege.web.client.csv.CSVImportDialogController;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.dispatch.actions.*;
import edu.stanford.bmir.protege.web.client.Application;
import edu.stanford.bmir.protege.web.client.rpc.*;
import edu.stanford.bmir.protege.web.client.ui.library.dlg.*;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.ui.library.msgbox.YesNoHandler;
import edu.stanford.bmir.protege.web.client.ui.notes.editor.DiscussionThreadDialog;
import edu.stanford.bmir.protege.web.client.ui.ontology.entity.CreateEntityDialog;
import edu.stanford.bmir.protege.web.client.ui.ontology.entity.CreateEntityDialogController;
import edu.stanford.bmir.protege.web.client.ui.ontology.entity.CreateEntityInfo;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractOWLEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.upload.UploadFileDialog;
import edu.stanford.bmir.protege.web.client.ui.upload.UploadFileResultHandler;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.client.project.Project;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.ui.search.SearchUtil;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionListener;
import edu.stanford.bmir.protege.web.client.ui.util.GlobalSelectionManager;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;
import edu.stanford.bmir.protege.web.shared.ObjectPath;
import edu.stanford.bmir.protege.web.shared.csv.CSVImportDescriptor;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.event.*;
import edu.stanford.bmir.protege.web.shared.hierarchy.*;
import edu.stanford.bmir.protege.web.shared.watches.*;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Portlet for displaying class trees. It can be configured to show only a
 * subtree of an ontology, by setting the portlet property <code>topClass</code>
 * to the name of the top class to show. <br>
 * Also supports creating and editing classes.
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class ClassTreePortlet extends AbstractOWLEntityPortlet {

    private static final String SUFFIX_ID_LOCAL_ANNOTATION_COUNT = "_locAnnCnt";

    private static final String SUFFIX_ID_LOCAL_ANNOTATION_IMG = "_locAnnImg";

    protected static final String WATCH_ICON_STYLE_STRING = "style=\"position:relative; top:3px; left:2px;\"";

    private static final String PLACE_HOLDER_PANEL = "placeHolderPanel";

    private final String linkPattern = "{0}?ontology={1}&tab={2}&id={3}";

    private TreePanel treePanel;

    protected ToolbarButton createButton;

    protected ToolbarButton deleteButton;

    protected CycleButton watchButton;

    private CheckItem lastSelectedWatchType;

    private CheckItem watchBranchItem;

    private CheckItem unwatchBranchItem;

    private boolean expandDisabled = false;

    private boolean showToolbar = true;

    private boolean showTitle = true;

    private boolean showTools = true;

    private boolean allowsMultiSelection = true;

    private String hierarchyProperty = null;

    private String topClass = null;

    private Collection<EntityData> initialSelection = null;

    private TreeNodeListenerAdapter nodeListener;

    private boolean registeredEventHandlers = false;

    /*
    * Configuration constants and defaults
    */
    private static Set<EntityData> nodesWithNotesOpen = new HashSet<EntityData>();

    public ClassTreePortlet(final Project project) {
        this(project, true, true, true, true, null);
    }

    public ClassTreePortlet(final Project project, final boolean showToolbar, final boolean showTitle, final boolean showTools, final boolean allowsMultiSelection, final String topClass) {
        super(project, false);
        this.showToolbar = showToolbar;
        this.showTitle = showTitle;
        this.showTools = showTools;
        this.allowsMultiSelection = allowsMultiSelection;
        this.topClass = topClass;
        registerEventHandlers();

        initialize();
    }

    private void registerEventHandlers() {
        if(registeredEventHandlers) {
            return;
        }
        GWT.log("Registering event handlers for ClassTreePortlet " + this);
        registeredEventHandlers = true;
        ///////////////////////////////////////////////////////////////////////////
        //
        // Registration of event handlers that we are interested in
        //
        ///////////////////////////////////////////////////////////////////////////

        addProjectEventHandler(BrowserTextChangedEvent.TYPE, new BrowserTextChangedHandler() {
            @Override
            public void browserTextChanged(BrowserTextChangedEvent event) {
//                if (isEventForThisProject(event)) {
                    onEntityBrowserTextChanged(event);
//                }
            }
        });

        addProjectEventHandler(EntityNotesChangedEvent.TYPE, new EntityNotesChangedHandler() {
            @Override
            public void entityNotesChanged(EntityNotesChangedEvent event) {
                if (isEventForThisProject(event)) {
                    onNotesChanged(event);
                }
            }
        });

        addProjectEventHandler(WatchAddedEvent.TYPE, new WatchAddedHandler() {
            @Override
            public void handleWatchAdded(WatchAddedEvent event) {
                if (isEventForThisProject(event)) {
                    onWatchAdded(event);
                }
            }
        });

        addProjectEventHandler(WatchRemovedEvent.TYPE, new WatchRemovedHandler() {
            @Override
            public void handleWatchRemoved(WatchRemovedEvent event) {
                if (isEventForThisProject(event)) {
                    onWatchRemoved(event);
                }
            }
        });

        addProjectEventHandler(EntityDeprecatedChangedEvent.TYPE, new EntityDeprecatedChangedHandler() {
            @Override
            public void handleEntityDeprecatedChangedEvent(EntityDeprecatedChangedEvent evt) {
                if (isEventForThisProject(evt)) {
                    onEntityDeprecatedChanged(evt.getEntity(), evt.isDeprecated());
                }
            }
        });

        addProjectEventHandler(ClassHierarchyParentAddedEvent.TYPE, new ClassHierarchyParentAddedHandler() {
            @Override
            public void handleClassHierarchyParentAdded(final ClassHierarchyParentAddedEvent event) {
                if (isEventForThisProject(event)) {
                    handleParentAddedEvent(event);
                }
            }
        });


        addProjectEventHandler(ClassHierarchyParentRemovedEvent.TYPE, new ClassHierarchyParentRemovedHandler() {
            @Override
            public void handleClassHierarchyParentRemoved(ClassHierarchyParentRemovedEvent event) {
                if (isEventForThisProject(event)) {
                    handleParentRemovedEvent(event);
                }
            }
        });

    }

    private void handleParentAddedEvent(final ClassHierarchyParentAddedEvent event) {
        final TreeNode tn = findTreeNode(event.getParent());
        if(tn != null) {
            RenderingServiceManager.getManager().execute(new GetRendering(getProjectId(), event), new AsyncCallback<GetRenderingResponse>() {
                @Override
                public void onFailure(Throwable caught) {
                    GWT.log("Problem getting classes", caught);
                }

                @Override
                public void onSuccess(GetRenderingResponse result) {
                    SubclassEntityData subClassData = new SubclassEntityData(event.getChild().toStringID(), result.getRendering(event.getChild().getIRI()).get().getBrowserText(), Collections.<EntityData>emptyList(), 0);
                    subClassData.setValueType(ValueType.Cls);
                    onSubclassAdded((EntityData) tn.getUserObject(), Arrays.<EntityData>asList(subClassData), false);
                }
            });

        }
    }

    private void handleParentRemovedEvent(ClassHierarchyParentRemovedEvent event) {
        TreeNode parentTn = findTreeNode(event.getParent());
        if (parentTn != null) {
            // We should check
            TreeNode childTn = findTreeNode(event.getChild());
            if(childTn != null) {
                for(Node existingChild : parentTn.getChildNodes()) {
//                    String parentId = parentTn.getId();
                    String childId = childTn.getId();
                    String existingChildId = existingChild.getId();
                    if(childId != null && existingChildId != null && childId.equals(existingChildId)) {
                        childTn.remove();
                        return;
                    }
                }
            }
        }
    }

    protected String getCreateClsDescription() {
        return UIUtil.getStringConfigurationProperty(getPortletConfiguration(), ClassTreePortletConstants.CREATE_ACTION_DESC_PROP, ClassTreePortletConstants.CREATE_ACTION_DESC_DEFAULT);
    }

    protected String getDeleteClsDescription() {
        return UIUtil.getStringConfigurationProperty(getPortletConfiguration(), ClassTreePortletConstants.DELETE_ACTION_DESC_PROP, ClassTreePortletConstants.DELETE_ACTION_DESC_DEFAULT);
    }

    protected String getRenameClsDescription() {
        return UIUtil.getStringConfigurationProperty(getPortletConfiguration(), ClassTreePortletConstants.RENAME_ACTION_DESC_PROP, ClassTreePortletConstants.RENAME_ACTION_DESC_DEFAULT);
    }

    protected String getMoveClsDescription() {
        return UIUtil.getStringConfigurationProperty(getPortletConfiguration(), ClassTreePortletConstants.MOVE_ACTION_DESC_PROP, ClassTreePortletConstants.MOVE_ACTION_DESC_DEFAULT);
    }

    protected String getCreateClsLabel() {
        return UIUtil.getStringConfigurationProperty(getPortletConfiguration(), ClassTreePortletConstants.CREATE_LABEL_PROP, ClassTreePortletConstants.CREATE_LABEL_DEFAULT);
    }

    protected String getDeleteClsLabel() {
        return UIUtil.getStringConfigurationProperty(getPortletConfiguration(), ClassTreePortletConstants.DELETE_LABEL_PROP, ClassTreePortletConstants.DELETE_LABEL_DEFAULT);
    }

    protected boolean getDeleteEnabled() {
        return UIUtil.getBooleanConfigurationProperty(getPortletConfiguration(), ClassTreePortletConstants.DELETE_ENABLED_PROP, ClassTreePortletConstants.DELETE_ENABLED_DEFAULT);
    }

    protected String getWatchClsLabel() {
        return UIUtil.getStringConfigurationProperty(getPortletConfiguration(), ClassTreePortletConstants.WATCH_LABEL_PROP, ClassTreePortletConstants.WATCH_LABEL_DEFAULT);
    }

    protected String getWatchBranchClsLabel() {
        return UIUtil.getStringConfigurationProperty(getPortletConfiguration(), ClassTreePortletConstants.WATCH_BRANCH_LABEL_PROP, ClassTreePortletConstants.WATCH_BRANCH_LABEL_DEFAULT);
    }

    protected String getUnwatchClsLabel() {
        return UIUtil.getStringConfigurationProperty(getPortletConfiguration(), ClassTreePortletConstants.UNWATCH_LABEL_PROP, ClassTreePortletConstants.UNWATCH_LABEL_DEFAULT);
    }

    protected boolean getInheritMetaClasses() {
        return UIUtil.getBooleanConfigurationProperty(getPortletConfiguration(), ClassTreePortletConstants.INHERIT_METACLASSES_PROP, ClassTreePortletConstants.INHERIT_METACLASSES_DEFAULT);
    }

    @Override
    public void reload() {
    }

    @Override
    public void initialize() {
        setLayout(new FitLayout());

        setTools(getTools());

        if (showTitle) {
            setTitle("Classes");
        }

        if (showToolbar) {
            addToolbarButtons();
        }

        final Panel bogusPanel = new Panel();
        bogusPanel.setId(PLACE_HOLDER_PANEL);
        bogusPanel.setHeight(560);
        add(bogusPanel);
        updateButtonStates();
        if (nodeListener == null) {
            //listener for click on the comment icon to display notes
            nodeListener = new TreeNodeListenerAdapter() {

                //TODO: this functionality is similar to that found in AbstractFieldWidget and
                @Override
                public boolean doBeforeClick(final Node node, final EventObject e) {

                    if (!nodesWithNotesOpen.contains(node.getUserObject())) { //not the second click in a double click
                        onCellClickOrDblClick(node, e);
                    }
                    return true;
                }

                private void onCellClickOrDblClick(Node node, EventObject e) {
                    final Element target = e.getTarget();
                    if (target != null) {
                        final String targetId = target.getId();
                        if (targetId.endsWith(SUFFIX_ID_LOCAL_ANNOTATION_IMG) || targetId.endsWith(SUFFIX_ID_LOCAL_ANNOTATION_COUNT)) {
                            showClassNotes(node);
                        }
                    }
                }

                @Override
                public void onContextMenu(final Node node, EventObject e) {
                    treePanel.getSelectionModel().select((TreeNode) node);
                    Menu contextMenu = new Menu();
                    contextMenu.setWidth("140px");

                    MenuItem menuShowInternalID = new MenuItem();
                    menuShowInternalID.setText("Show internal ID");
                    menuShowInternalID.addListener(new BaseItemListenerAdapter() {
                        @Override
                        public void onClick(BaseItem item, EventObject event) {
                            super.onClick(item, event);
                            showInternalID((EntityData) node.getUserObject());
                        }
                    });
                    contextMenu.addItem(menuShowInternalID);

                    MenuItem menuShowDirectLink = new MenuItem();
                    menuShowDirectLink.setText("Show direct link");
                    menuShowDirectLink.setIcon("images/link.png");
                    menuShowDirectLink.addListener(new BaseItemListenerAdapter() {
                        @Override
                        public void onClick(BaseItem item, EventObject event) {
                            super.onClick(item, event);
                            showDirectLink((EntityData) node.getUserObject());
                        }
                    });
                    contextMenu.addItem(menuShowDirectLink);

                    contextMenu.showAt(e.getXY()[0] + 5, e.getXY()[1] + 5);
                }
            };
        }



    }


    private String getWatchIconName(Watch w) {
        if(w instanceof EntityFrameWatch) {
            return "eye.png";
        }
        else {
            return "eye-down.png";
        }
    }

    private void onNotesChanged(EntityNotesChangedEvent event) {
        String name = event.getEntity().getIRI().toString();
        TreeNode node = findTreeNode(name);
        if(node != null) {
            final Object userObject = node.getUserObject();
            if (userObject instanceof EntityData) {
                EntityData subclassEntityData = (EntityData) userObject;
                int presentCount = subclassEntityData.getLocalAnnotationsCount();
                int delta = event.getTotalNotesCount() - presentCount;
                subclassEntityData.setLocalAnnotationsCount(event.getTotalNotesCount());
                String nodeText = createNodeRenderText(node);
                node.setText(nodeText);
                TreeNode childNode = node;
            }
            // TODO: Think about this
//            updateAncestorNoteCounts(delta, childNode);
        }
    }

    private void onWatchAdded(WatchAddedEvent event) {
        if(!event.getUserId().equals(getUserId())) {
            return;
        }
        Watch watch = event.getWatch();
        if(watch instanceof EntityBasedWatch) {
            TreeNode tn = findTreeNode(((EntityBasedWatch) watch).getEntity().getIRI().toString());
            if(tn != null) {
                SubclassEntityData data = (SubclassEntityData) tn.getUserObject();
                data.addWatch(watch);
                updateTreeNodeRendering(tn);
            }
        }
    }

    private void updateTreeNodeRendering(TreeNode tn) {
        tn.setText(createNodeRenderText(tn));
        updateTreeNodeIcon(tn);
    }

    private void onWatchRemoved(WatchRemovedEvent event) {
        if(!event.getUserId().equals(getUserId())) {
            return;
        }
        Watch watch = event.getWatch();
        if(watch instanceof EntityBasedWatch) {
            OWLEntity entity = ((EntityBasedWatch) watch).getEntity();
            TreeNode tn = findTreeNode(entity.getIRI().toString());
            if(tn != null) {
                SubclassEntityData data = (SubclassEntityData) tn.getUserObject();
                data.clearWatches();
                updateTreeNodeRendering(tn);
            }
        }
    }

//    private void updateAncestorNoteCounts(int nodeDelta, TreeNode forNode) {
//        TreeNode parentNode = (TreeNode) forNode.getParentNode();
//        while(parentNode != null && parentNode.getUserObject() instanceof SubclassEntityData) {
//            SubclassEntityData parentData = (SubclassEntityData) parentNode.getUserObject();
//            SubclassEntityData childData = (SubclassEntityData) forNode.getUserObject();
//            parentData.setChildrenAnnotationsCount(parentData.getChildrenAnnotationsCount() + nodeDelta);
//            String parentText = createNodeRenderText(parentNode);
//            parentNode.setText(parentText);
//            forNode = parentNode;
//            parentNode = (TreeNode) parentNode.getParentNode();
//        }
//    }

    private void showInternalID(EntityData entity) {
        String className = entity.getName();
        MessageBox.showMessage(entity.getBrowserText() + " Internal Id", className);
    }

    private void showDirectLink(EntityData entity) {

        String url = "";
        try {
            String applicationURL = Window.Location.getHref();
            if (applicationURL.contains("?")) {
                applicationURL = applicationURL.substring(0, applicationURL.indexOf("?"));
            }
            if (applicationURL.contains("#")) {
                applicationURL = applicationURL.substring(0, applicationURL.indexOf("#"));
            }
            String tabName = getTab().getTabConfiguration().getName();
            if (tabName.contains(".")) {
                tabName = tabName.substring(tabName.lastIndexOf(".") + 1);
            }
            String className = entity.getName();
//            url = linkMessage.format(new Object[]{
//                    applicationURL,
//                    URL.encodeQueryString(getProject().getEscapedProjectName()),
//                    tabName,
//                    className == null ? "" : URL.encodeQueryString(className)
//            }, new StringBuffer(), new FieldPosition(0)).toString();
            url = linkPattern.replace("{0}", applicationURL).
                    replace("{1}", URL.encodeQueryString(getProjectId().getId())).
                    replace("{2}", tabName).
                    replace("{3}", className == null ? "" : URL.encodeQueryString(className));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        MessageBox.showMessage("Direct link to " + entity.getBrowserText(), url);

    }

    public TreePanel createTreePanel() {
        treePanel = new TreePanel();
        treePanel.setHeight(560);
        treePanel.setAutoWidth(true);
        treePanel.setAnimate(true);
        treePanel.setAutoScroll(true);
        treePanel.setEnableDD(true);

        if (allowsMultiSelection) {
            treePanel.setSelectionModel(new MultiSelectionModel());
        }

        treePanel.addListener(new TreePanelListenerAdapter() {
            @Override
            public void onExpandNode(final TreeNode node) {
                // if (!expandDisabled && !treePanel.getRootNode().equals(node)) {
                if (!expandDisabled) {
                    GWT.log("Expand node " + node.getUserObject(), null);
                    getSubclasses(((EntityData) node.getUserObject()).getName(), node);
                }
            }
        });

        addDragAndDropSupport();
//        addProjectListeners();

        return treePanel;
    }

    protected void createSelectionListener() {
        final TreeSelectionModel selModel = treePanel.getSelectionModel();
        if (selModel instanceof DefaultSelectionModel) {
            ((DefaultSelectionModel) selModel).addSelectionModelListener(new DefaultSelectionModelListenerAdapter() {
                @Override
                public void onSelectionChange(final DefaultSelectionModel sm, final TreeNode node) {
                    notifySelectionListeners(new SelectionEvent(ClassTreePortlet.this));
                }
            });
        }
        else if (selModel instanceof MultiSelectionModel) {
            ((MultiSelectionModel) selModel).addSelectionModelListener(new MultiSelectionModelListener() {
                public void onSelectionChange(final MultiSelectionModel sm, final TreeNode[] nodes) {
                    notifySelectionListeners(new SelectionEvent(ClassTreePortlet.this));
                }
            });
        }
        else {
            GWT.log("Unknown tree selection model for class tree: " + selModel, null);
        }
    }

//    protected void addProjectListeners() {
//        if (hierarchyProperty != null) { // hierarchy of property
//            // TODO: CLEAR UP
//            getProject().addOntologyListener(new OntologyListenerAdapter() {
//
//                @Override
//                public void propertyValueAdded(final PropertyValueEvent propertyValueEvent) {
//                    GWT.log("Property value added event: " + propertyValueEvent.getEntity() + " " + propertyValueEvent.getProperty() + " " + propertyValueEvent.getAddedValues(), null);
//                    if (propertyValueEvent.getProperty().getName().equals(hierarchyProperty)) {
//                        onSubclassAdded(propertyValueEvent.getEntity(), propertyValueEvent.getAddedValues(), false);
//                    }
//                }
//
//                @Override
//                public void propertyValueRemoved(final PropertyValueEvent propertyValueEvent) {
//                    GWT.log("Property value removed event: " + propertyValueEvent.getEntity() + " " + propertyValueEvent.getProperty() + " " + propertyValueEvent.getRemovedValues(), null);
//                    if (propertyValueEvent.getProperty().getName().equals(hierarchyProperty)) {
////                        onSubclassRemoved(propertyValueEvent.getEntity(), propertyValueEvent.getRemovedValues());
//                    }
//                }
//
//                @Override
//                public void propertyValueChanged(final PropertyValueEvent propertyValueEvent) {
//                    GWT.log("Property value changed event: " + propertyValueEvent.getEntity() + " " + propertyValueEvent.getProperty() + " " + propertyValueEvent.getAddedValues() + "  " + propertyValueEvent.getRemovedValues(), null);
//                }
//
//            });
//        }
//        else { // subclass of
//        }
//    }
//


    @Override
    protected Tool[] getTools() {
        return showTools ? super.getTools() : new Tool[]{};
    }

    protected void addToolbarButtons() {
        setTopToolbar(new Toolbar());
        final Toolbar toolbar = getTopToolbar();

        createButton = createCreateButton();
        if (createButton != null) {
            toolbar.addButton(createButton);
        }

        deleteButton = createDeleteButton();
        if (deleteButton != null) {
            toolbar.addButton(deleteButton);
        }

        watchButton = createWatchButton();
        if (watchButton != null) {
            toolbar.addElement(watchButton.getElement());
        }

        final Component searchField = createSearchField();
        if (searchField != null) {
            toolbar.addSpacer();
            toolbar.addSeparator();
            toolbar.addText("&nbsp<i>Search</i>:&nbsp&nbsp");
            toolbar.addElement(searchField.getElement());
        }
    }

    protected ToolbarButton createCreateButton() {
        createButton = new ToolbarButton(getCreateClsLabel());
        createButton.setCls("toolbar-button");
        createButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(final Button button, final EventObject e) {
                onCreateCls(e.isShiftKey() ? CreateClassesMode.IMPORT_CSV : CreateClassesMode.CREATE_SUBCLASSES);
            }
        });
        createButton.setDisabled(!hasWritePermission());
        return createButton;
    }

    protected ToolbarButton createDeleteButton() {
        deleteButton = new ToolbarButton(getDeleteClsLabel());
        deleteButton.setCls("toolbar-button");
        deleteButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(final Button button, final EventObject e) {
                onDeleteCls();
            }
        });
        deleteButton.setDisabled(!getProject().hasWritePermission(Application.get().getUserId()));
        deleteButton.setDisabled(!getDeleteEnabled());
        return deleteButton;
    }

    protected CycleButton createWatchButton() {
        watchButton = new CycleButton();
        watchButton.setShowText(true);
        watchButton.setCls("toolbar-button");

        final CheckItem watchItem = new CheckItem();
        watchItem.setText(getWatchClsLabel());
        watchItem.setCls("toolbar-button");
//        watchItem.setIcon("images/eye.png");
        watchButton.addItem(watchItem);

        watchBranchItem = new CheckItem();
        watchBranchItem.setText(getWatchBranchClsLabel());
        watchBranchItem.setCls("toolbar-button");
//        watchBranchItem.setIcon("images/eye-down.png");
        watchBranchItem.setChecked(true);
        watchButton.addItem(watchBranchItem);

        unwatchBranchItem = new CheckItem();
        unwatchBranchItem.setText(getUnwatchClsLabel());
        unwatchBranchItem.setCls("toolbar-button");
        watchButton.addItem(unwatchBranchItem);

        //the listener is needed to override behavior of cycle button
        CheckItemListener checkItemListener = new CheckItemListenerAdapter() {
            @Override
            public boolean doBeforeCheckChange(CheckItem item, boolean checked) {
                return false;
            }
        };

        BaseItemListener baseItemListener = new BaseItemListenerAdapter() {
            @Override
            public void onClick(BaseItem item, EventObject e) {
                CheckItem activeItem = (CheckItem) item;
                watchButton.setActiveItem(activeItem);
                if (activeItem != unwatchBranchItem) {
                    lastSelectedWatchType = activeItem;
                }

                //this is optional, and can be removed if it is confusing to the user:
                //if a user clicks in the watch menu on an item, we perform the operation right away to avoid one more click.
                if (activeItem.equals(watchItem)) {
                    onWatchCls();
                }
                else if (activeItem.equals(watchBranchItem)) {
                    onWatchBranchCls();
                }
                else if (activeItem.equals(unwatchBranchItem)) {
                    onUnwatchCls();
                }
            }
        };

        watchItem.addListener(baseItemListener);
        watchBranchItem.addListener(baseItemListener);
        unwatchBranchItem.addListener(baseItemListener);

        watchItem.addListener(checkItemListener);
        watchBranchItem.addListener(checkItemListener);
        unwatchBranchItem.addListener(checkItemListener);

        //listener for performing the specific watch action
        watchButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                CheckItem activeItem = watchButton.getActiveItem();
                if (activeItem.equals(watchItem)) {
                    onWatchCls();
                }
                else if (activeItem.equals(watchBranchItem)) {
                    onWatchBranchCls();
                }
                else if (activeItem.equals(unwatchBranchItem)) {
                    onUnwatchCls();
                }
            }
        });

        //listener for adjusting the watch button to the selection in tree
        addSelectionListener(new SelectionListener() {
            public void selectionChanged(SelectionEvent event) {
                updateWatchedMenuState(event.getSelectable().getSelection());
            }
        });

        return watchButton;
    }

    protected void updateWatchedMenuState(Collection<EntityData> entities) {
        if (entities == null || entities.size() == 0) {
            return;
        }
        EntityData entityData = entities.iterator().next();
        Set<Watch<?>> watches = entityData.getWatches();
        if (!watches.isEmpty()) {
            watchButton.setActiveItem(unwatchBranchItem);
        }
        else {
            watchButton.setActiveItem(lastSelectedWatchType == null ? watchBranchItem : lastSelectedWatchType);
        }
    }

    protected Component createSearchField() {
        final TextField searchField = new TextField("Search: ", "search");
        searchField.setSelectOnFocus(true);
        searchField.setAutoWidth(true);
        searchField.setEmptyText("Type search string");
        searchField.addListener(new TextFieldListenerAdapter() {
            @Override
            public void onSpecialKey(final Field field, final EventObject e) {
                if (e.getKey() == EventObject.ENTER) {
                    SearchUtil searchUtil = new SearchUtil(getProjectId(), ClassTreePortlet.this, getSearchAsyncCallback());
                    searchUtil.setBusyComponent(searchField);
                    searchUtil.setSearchedValueType(ValueType.Cls);
                    searchUtil.search(searchField.getText());
                }
            }
        });
        return searchField;
    }

    protected AsyncCallback<Boolean> getSearchAsyncCallback() {
        return new AsyncCallback<Boolean>() {
            public void onFailure(Throwable caught) {
                MessageBox.showAlert("Search error", "An error occurred during the search. Please try again.");
            }

            public void onSuccess(Boolean result) {
            }
        };
    }

    protected void addDragAndDropSupport() {
        treePanel.addListener(new TreePanelListenerAdapter() {
            @Override
            public boolean doBeforeNodeDrop(final TreePanel treePanel, final TreeNode target, final DragData dragData, final String point, final DragDrop source, final TreeNode dropNode, final DropNodeCallback dropNodeCallback) {
                if (hasWritePermission()) {
                    final boolean success = Window.confirm("Are you sure you want to move " + getNodeBrowserText(dropNode) + " from parent " + getNodeBrowserText(dropNode.getParentNode()) + " to parent " + getNodeBrowserText(target) + " ?");
                    if (success) {
                        moveClass((EntityData) dropNode.getUserObject(), (EntityData) dropNode.getParentNode().getUserObject(), (EntityData) target.getUserObject());
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
        });
    }

    protected void onSubclassAdded(final EntityData parent, final Collection<EntityData> subclasses, final boolean selectNewNode) {
        if (subclasses == null || subclasses.size() == 0) {
            return;
        }

        final EntityData subclassEntity = ((List<EntityData>) subclasses).get(0); //there is always just one
        final TreeNode parentNode = findTreeNode(parent.getName());

        if (parentNode == null) {
            return; // nothing to be done
        }

        TreeNode subclassNode = findTreeNode(subclassEntity.getName());
        if (subclassNode == null) {
            subclassNode = createTreeNode(subclassEntity);
            parentNode.appendChild(subclassNode);
            getSubclasses(parent.getName(), parentNode);
        }
        else { // tricky if it already exists
            if (!hasChild(parentNode, subclassEntity.getName())) { //multiple parents
                subclassNode = createTreeNode(subclassEntity);
                if (subclassEntity instanceof SubclassEntityData) {
                    final int childrenCount = ((SubclassEntityData) subclassEntity).getSubclassCount();
                    if (childrenCount > 0) {
                        subclassNode.setExpandable(true);
                    }
                }
                parentNode.appendChild(subclassNode);
            }
        }

    }

    protected TreeNode findTreeNode(OWLClass cls) {
        return findTreeNode(cls.getIRI().toString());
    }

    protected TreeNode findTreeNode(final String id) {
        if(treePanel == null) {
            return null;
        }
        final TreeNode root = treePanel.getRootNode();
        final TreeNode node = findTreeNode(root, id, new ArrayList<TreeNode>());
        return node;
    }

    protected TreeNode findTreeNode(final TreeNode node, final String id, final ArrayList<TreeNode> visited) {
        if (getNodeClsName(node).equals(id)) {
            return node;
        }
        else {
            visited.add(node);
            final Node[] children = node.getChildNodes();
            for (final Node element2 : children) {
                final TreeNode n = findTreeNode((TreeNode) element2, id, visited);
                if (n != null) {
                    return n;
                }
            }
            return null;
        }
    }

//    protected void onSubclassRemoved(final EntityData entity, final Collection<EntityData> subclasses) {
//        if (subclasses == null || subclasses.size() == 0) {
//            return;
//        }
//
//        final EntityData subclass = ((List<EntityData>) subclasses).get(0);
//        final TreeNode parentNode = findTreeNode(entity.getName());
//
//        if (parentNode == null) {
//            return;
//        }
//
//        // final TreeNode subclassNode = findTreeNode(parentNode, subclass.getName(), new ArrayList<TreeNode>());
//        final TreeNode subclassNode = getDirectChild(parentNode, subclass.getName());
//        if (subclassNode == null) {
//            return;
//        }
//
//        //if (subclassNode.getParentNode().equals(parentNode)) {
//        parentNode.removeChild(subclassNode);
//        if (parentNode.getChildNodes().length < 1) {
//            parentNode.setExpandable(false);
//        }
//        //}
//    }

    /**
     * Called to update the browser text in the tree
     * @param event The event that describes the browser text change that happened.
     */
    protected void onEntityBrowserTextChanged(BrowserTextChangedEvent event) {
        OWLEntity entity = event.getEntity();
        TreeNode tn = findTreeNode(entity.getIRI().toString());
        if(tn == null) {
            return;
        }
        EntityData ed = (EntityData) tn.getUserObject();
        ed.setBrowserText(event.getNewBrowserText());
        updateTreeNodeRendering(tn);
    }


    protected void onEntityDeprecatedChanged(OWLEntity entity, boolean deprecated) {
        TreeNode tn = findTreeNode(entity.getIRI().toString());
        if(tn == null) {
            return;
        }
        if (tn.getUserObject() instanceof SubclassEntityData) {
            SubclassEntityData entityData = (SubclassEntityData) tn.getUserObject();
            entityData.setDeprecated(deprecated);
            updateTreeNodeRendering(tn);
        }
    }

    protected void onClassCreated(final OWLClass freshClass, final List<OWLClass> superClasses) {

    }



    protected void insertNodeInTree(final TreeNode parentNode, final EntityData child) {
        if (!hasChild(parentNode, child.getName())) {
            parentNode.appendChild(createTreeNode(child));
        }
    }

    private enum CreateClassesMode {

        CREATE_SUBCLASSES,
        IMPORT_CSV
    }

    protected void onCreateCls(CreateClassesMode mode) {

        if (mode == CreateClassesMode.CREATE_SUBCLASSES) {
            createSubClasses();
        }
        else {
            createSubClassesByImportingCSVDocument();
        }



    }


    private void createSubClasses() {
        CreateEntityDialog dlg = new CreateEntityDialog(EntityType.CLASS, new CreateEntityDialogController.CreateEntityHandler() {
            @Override
            public void handleCreateEntity(CreateEntityInfo createEntityInfo) {
                final OWLClass superCls = getSelectedClass();
                final Set<String> browserTexts = new HashSet<String>(createEntityInfo.getBrowserTexts());
                if (browserTexts.size() > 1) {
                    DispatchServiceManager.get().execute(new CreateClassesAction(getProjectId(), superCls, browserTexts), getCreateClassesActionAsyncHandler());
                }
                else {
                    DispatchServiceManager.get().execute(new CreateClassAction(getProjectId(), browserTexts.iterator().next(), superCls), getCreateClassAsyncHandler());
                }
            }
        });
        dlg.setVisible(true);
    }

    private void createSubClassesByImportingCSVDocument() {
        UploadFileDialog d = new UploadFileDialog("Upload CSV", new UploadFileResultHandler() {
            @Override
            public void handleFileUploaded(final DocumentId fileDocumentId) {
                WebProtegeDialog<CSVImportDescriptor> csvImportDialog = new WebProtegeDialog<CSVImportDescriptor>(new CSVImportDialogController(getProjectId(), fileDocumentId, getSelectedClass()));
                csvImportDialog.setVisible(true);

            }

            @Override
            public void handleFileUploadFailed(String errorMessage) {
                UIUtil.hideLoadProgessBar();
                MessageBox.showAlert("Error uploading CSV file", errorMessage);
            }
        });
        d.setVisible(true);



    }

    private AsyncCallback<CreateClassesResult> getCreateClassesActionAsyncHandler() {
        return new AsyncCallback<CreateClassesResult>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("There was a problem creating the classes.  Please try again.");
            }

            @Override
            public void onSuccess(CreateClassesResult result) {
                Set<OWLClass> createdClasses = result.getCreatedClasses();
                for(TreeNode node : getSelectedTreeNodes()) {
                    Set<OWLClass> existingClasses = new HashSet<OWLClass>();
                    for(Node childNode : node.getChildNodes()) {
                        OWLClass childCls = DataFactory.getOWLClass(getNodeClsName(childNode));
                        existingClasses.add(childCls);
                    }
                    for(OWLClass createdCls : createdClasses) {
                        if(!existingClasses.contains(createdCls)) {
                            final SubclassEntityData entityData = new SubclassEntityData(createdCls.getIRI().toString(), result.getBrowserText(createdCls).or(""), Collections.<EntityData>emptySet(), 0);
                            entityData.setValueType(ValueType.Cls);
                            Node n = createTreeNode(entityData);
                            node.appendChild(n);
                        }
                    }
                }

            }
        };
    }

    protected boolean isValidClassName(String value) {
        return value != null && value.length() > 0 && UIUtil.getIdentifierStart(value, value.length()) == 0;
    }

    /**
     * Gets the selected class.
     * @return The selected class, or {@code null} if there is not selection.
     */
    protected OWLClass getSelectedClass() {
        final EntityData currentSelection = getSingleSelection();
        if(currentSelection == null) {
            return null;
        }
        return DataFactory.getOWLClass(currentSelection.getName());
    }

    protected void createCls(final String className) {
        OWLClass superCls = getSelectedClass();
        if(superCls == null) {
            superCls = DataFactory.getOWLThing();
        }
        DispatchServiceManager.get().execute(new CreateClassAction(getProjectId(), className, superCls), getCreateClassAsyncHandler());
//        OntologyServiceManager.getInstance().createCls(projectId, className, superCls, getInheritMetaClasses(), userId, getCreateClsDescription() + " " + className, getCreateClassAsyncHandler(superCls, className));
    }

    protected AbstractAsyncHandler<CreateClassResult> getCreateClassAsyncHandler() {
        return new CreateClassHandler();
    }

    protected void onDeleteCls() {
        final EntityData currentSelection = getSingleSelection();
        if (currentSelection == null) {
            MessageBox.showAlert("No class selected", "Please select a class to delete.");
            return;
        }

        final String displayName = currentSelection.getBrowserText();
        final String clsName = currentSelection.getName();

        MessageBox.showYesNoConfirmBox("Delete class?", "Are you sure you want to delete class \"" + displayName + "\"?", new YesNoHandler() {
            @Override
            public void handleYes() {
                deleteCls(clsName);
            }

            @Override
            public void handleNo() {
            }
        });
    }

    protected void deleteCls(final String className) {
        GWT.log("Should delete class with name: " + className, null);
        if (className == null) {
            return;
        }

        OWLClass entity = DataFactory.getOWLClass(className);
        DispatchServiceManager.get().execute(new DeleteEntityAction(entity, getProjectId()), new DeleteClassHandler());

        refreshFromServer(500);
    }

    protected void onWatchCls() {
        final OWLClass sel = getSelectedClass();
        if(sel == null) {
            return;
        }
        EntityFrameWatch entityWatch = new EntityFrameWatch(sel);
        DispatchServiceManager.get().execute(new AddWatchAction(entityWatch, getProjectId(), getUserId()), new AsyncCallback<AddWatchResult>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Problem adding watch");
            }

            @Override
            public void onSuccess(AddWatchResult result) {

            }
        });

//        ChAOServiceManager.getInstance().addWatchedEntity(project.getDisplayName(), GlobalSettings.get().getUserName(), currentSelection.getName(), new AddWatchedCls(getSingleSelectedTreeNode()));
    }

    protected void onWatchBranchCls() {
        final OWLClass sel = getSelectedClass();
        if(sel == null) {
            return;
        }
        Watch watch = new HierarchyBranchWatch(sel);
        DispatchServiceManager.get().execute(new AddWatchAction(watch, getProjectId(), getUserId()), new AsyncCallback<AddWatchResult>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(AddWatchResult result) {

            }
        });

//        ChAOServiceManager.getInstance().addWatchedBranchEntity(project.getDisplayName(), GlobalSettings.get().getUserName(), currentSelection.getName(), new AddWatchedCls(getSingleSelectedTreeNode()));
    }




    protected void onUnwatchCls() {
        for(TreeNode selTreeNode : getSelectedTreeNodes()) {
            Object userObject = selTreeNode.getUserObject();
            if(userObject instanceof EntityData) {
                Set<Watch<?>> watches = ((EntityData) userObject).getWatches();
                DispatchServiceManager.get().execute(new RemoveWatchesAction(watches, getProjectId(), getUserId()), new AsyncCallback<RemoveWatchesResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(RemoveWatchesResult result) {
                    }
                });
            }
        }

    }

    protected void renameClass(final String oldName, final String newName) {
        GWT.log("Should rename class from " + oldName + " to " + newName, null);
        if (oldName.equals(newName) || newName == null || newName.length() == 0) {
            return;
        }

        OntologyServiceManager.getInstance().renameEntity(getProjectId(), oldName, newName, Application.get().getUserId(), getRenameClsDescription() + " " + "Old name: " + oldName + ", New name: " + newName, new RenameClassHandler());
    }

    public TreePanel getTreePanel() {
        return treePanel;
    }

    @Override
    protected void afterRender() {
        getRootCls();
    }

    private void updateTreeNodeIcon(TreeNode treeNode) {
        setTreeNodeIcon(treeNode, (EntityData) treeNode.getUserObject());
    }

    public void setTreeNodeIcon(final TreeNode node, EntityData entityData) {
        if(entityData instanceof SubclassEntityData && ((SubclassEntityData) entityData).isDeprecated()) {
            node.setIconCls("protege-deprecated-class-icon");
        }
        else {
            node.setIconCls("protege-class-icon");
        }

    }

    public void setTreeNodeTooltip(final TreeNode node, EntityData entityData) {
        //node.setTooltip(entityData.getBrowserText());
    }

    public void getSubclasses(final String parentClsName, final TreeNode parentNode) {
        if (isSubclassesLoaded(parentNode)) {
            return;
        }
        if (hierarchyProperty == null) {
            invokeGetSubclassesRemoteCall(parentClsName, getSubclassesCallback(parentClsName, parentNode));
        }
        else {
            final List<String> subjects = new ArrayList<String>();
            subjects.add(parentClsName);
            final List<String> props = new ArrayList<String>();
            props.add(hierarchyProperty);
            OntologyServiceManager.getInstance().getEntityTriples(getProjectId(), subjects, props, new GetPropertyHierarchySubclassesOfClassHandler(parentClsName, parentNode));
        }
    }

    protected void invokeGetSubclassesRemoteCall(final String parentClsName, AsyncCallback<List<SubclassEntityData>> callback) {
        OntologyServiceManager.getInstance().getSubclasses(getProjectId(), parentClsName, callback);
    }

    protected AsyncCallback<List<SubclassEntityData>> getSubclassesCallback(final String parentClsName, final TreeNode parentNode) {
        return new GetSubclassesOfClassHandler(parentClsName, parentNode, null);
    }

    protected String getStoredSubclassCount(final TreeNode node) {
        return node.getAttribute("scc");
    }

    public boolean isSubclassesLoaded(final TreeNode node) {
        final String val = node.getAttribute("subclassesLoaded");
        return val != null && val.equals("true");
    }

    public void setSubclassesLoaded(final TreeNode node, final boolean loaded) {
        node.setAttribute("subclassesLoaded", loaded ? "true" : "false");
    }

    public void getRootCls() {
        final String rootClsName = getRootClsName();
        if (rootClsName != null) {
            OntologyServiceManager.getInstance().getEntity(getProjectId(), rootClsName, new GetRootClassHandler());
        }
        else {
            OntologyServiceManager.getInstance().getRootEntity(getProjectId(), new GetRootClassHandler());
        }
    }

    protected String getRootClsName() {
        final PortletConfiguration portletConfiguration = getPortletConfiguration();
        if (portletConfiguration == null) {
            return topClass;
        }
        final Map<String, Object> props = portletConfiguration.getProperties();
        if (props == null) {
            return topClass;
        }
        // TODO: move from here
        final String title = (String) props.get("label");
        setTitle(title == null ? "Class Tree" : title);
        hierarchyProperty = (String) props.get("hierarchyProperty");

        //cache it so that we can set it
        if (topClass == null) {
            topClass = (String) props.get(ClassTreePortletConstants.TOP_CLASS_PROP);
        }
        return topClass;
    }

    /**
     * To take effect, it has to be called before {@link #afterRender()}.
     * @param topClass
     */
    public void setTopClass(final String topClass) {
        this.topClass = topClass;
    }

    protected void moveClass(final EntityData cls, final EntityData oldParent, final EntityData newParent) {
        if (oldParent.equals(newParent)) {
            return;
        }
        OntologyServiceManager.getInstance().moveCls(getProjectId(), cls.getName(), oldParent.getName(), newParent.getName(), false, Application.get().getUserId(), getMoveClsOperationDescription(cls, oldParent, newParent), new MoveClassHandler(cls.getName(), oldParent.getName(), newParent.getName()));
    }

    protected String getMoveClsOperationDescription(final EntityData cls, final EntityData oldParent, final EntityData newParent) {
        return getMoveClsDescription() + ": " + UIUtil.getDisplayText(cls) + ". Old parent: " + UIUtil.getDisplayText(oldParent) + ", New parent: " + UIUtil.getDisplayText(newParent);
    }

    public void getPathToRoot(final EntityData entity) {
        OntologyServiceManager.getInstance().getPathToRoot(getProjectId(), entity.getName(), new GetPathToRootHandler());
    }

    public List<EntityData> getSelection() {
        if (treePanel == null) {
            return null;
        }
        final List<EntityData> selections = new ArrayList<EntityData>();
        final TreeSelectionModel selectionModel = treePanel.getSelectionModel();
        if (selectionModel instanceof MultiSelectionModel) {
            final TreeNode[] selection = ((MultiSelectionModel) selectionModel).getSelectedNodes();
            for (final TreeNode node : selection) {
                final EntityData ed = (EntityData) node.getUserObject();
                selections.add(ed);
            }
        }
        else if (selectionModel instanceof DefaultSelectionModel) {
            final TreeNode node = ((DefaultSelectionModel) selectionModel).getSelectedNode();
            if (node != null) {
                selections.add((EntityData) node.getUserObject());
            }
        }
        return selections;
    }

    public List<TreeNode> getSelectedTreeNodes() {
        if (treePanel == null) {
            return null;
        }
        final List<TreeNode> selections = new ArrayList<TreeNode>();
        final TreeSelectionModel selectionModel = treePanel.getSelectionModel();
        if (selectionModel instanceof MultiSelectionModel) {
            final TreeNode[] selection = ((MultiSelectionModel) selectionModel).getSelectedNodes();
            for (final TreeNode node : selection) {
                selections.add(node);
            }
        }
        else if (selectionModel instanceof DefaultSelectionModel) {
            final TreeNode node = ((DefaultSelectionModel) selectionModel).getSelectedNode();
            selections.add(node);
        }
        return selections;
    }

    public TreeNode getSingleSelectedTreeNode() {
        return UIUtil.getFirstItem(getSelectedTreeNodes());
    }

    public EntityData getSingleSelection() {
        return UIUtil.getFirstItem(getSelection());
    }

    @Override
    public void setSelection(final Collection<EntityData> selection) {
        if (selection == null || selection.isEmpty()) {
            TreeNode node = getSingleSelectedTreeNode();
            if (node != null) {
                node.unselect();
            }
            return;
        }

        //happens only at initialization
        if (!isRendered() || treePanel == null || treePanel.getRootNode() == null) {
            this.initialSelection = selection;
            return;
        }

        GWT.log("Select in class tree: " + selection, null);
        final EntityData data = selection.iterator().next();

        // FIXME: just takes first element in selection for now; support multiple selections
        getPathToRoot(data);
    }

    public void selectPathInTree(List<EntityData> path) {
        int topIndex = -1;
        if (topClass != null) { //adjust path
            for (int i = 0; i < path.size() && topIndex == -1; i++) {
                if (path.get(i).getName().equals(topClass)) {
                    topIndex = i;
                }
            }
            if (topIndex != -1) {
                path = path.subList(topIndex, path.size());
            }
        }

        selectPathInTree(path, treePanel.getRootNode(), 0);
    }

    private void selectPathInTree(ObjectPath<OWLClass> path) {
        List<EntityData> entityDataPath = new ArrayList<EntityData>();
        for(OWLClass cls : path) {
            entityDataPath.add(new EntityData(cls.getIRI().toString()));
        }
        selectPathInTree(entityDataPath);
    }

    private void selectPathInTree(final List<EntityData> path, TreeNode parentNode, final int index) {
        for (int i = index; i < path.size(); i++) {
            final String clsName = path.get(i).getName();
            final TreeNode node = findTreeNode(clsName);
            if (node == null) {
                final EntityData parentEntityData = (EntityData) parentNode.getUserObject();
                invokeGetSubclassesRemoteCall(parentEntityData.getName(), getSelectInTreeCallback(parentNode, path, i));
                return;
            }
            else {
                parentNode = node;
                if (i == path.size() - 1) {
                    node.select();
                    if (!node.equals(treePanel.getRootNode())) {
                        node.ensureVisible();
                    }
                }
                else {
                    expandDisabled = true;
                    node.expand();
                    expandDisabled = false;
                }
            }
        }
    }

    protected AsyncCallback<List<SubclassEntityData>> getSelectInTreeCallback(TreeNode parentNode, List<EntityData> path, int index) {
        return new SelectInTreeHandler(parentNode, path, index);
    }

    protected TreeNode createTreeNode(final EntityData entityData) {
        final TreeNode node = new TreeNode(UIUtil.getDisplayText(entityData));
        node.setHref(null);
        node.setUserObject(entityData);
        node.setAllowDrag(true);
        node.setAllowDrop(true);
        setTreeNodeIcon(node, entityData);
        setTreeNodeTooltip(node, entityData);

        updateTreeNodeRendering(node);

        node.addListener(nodeListener);

        return node;
    }

    protected String createNodeRenderText(TreeNode node) {
        EntityData entityData = (EntityData) node.getUserObject();
        return createNodeText(entityData) + createNodeNoteCount(entityData, node) + createNodeWatchLabel(entityData);
    }

    protected String createNodeText(EntityData entityData) {
        boolean deprecated = false;
        if(entityData instanceof SubclassEntityData) {
            deprecated = ((SubclassEntityData) entityData).isDeprecated();
        }
        if(deprecated) {
            return "<span style=\"opacity: 0.5;\"><del>" + entityData.getBrowserText() + "</del></span>";
        }
        else {
            return entityData.getBrowserText();
        }
    }

    protected String createNodeNoteCount(EntityData entityData, TreeNode node) {
        String text = "";

        final int localAnnotationsCount = entityData.getLocalAnnotationsCount();
        if (localAnnotationsCount > 0) {
            final String idLocalAnnotationImg = node.getId() + SUFFIX_ID_LOCAL_ANNOTATION_IMG;
            final String idLocalAnnotationCnt = node.getId() + SUFFIX_ID_LOCAL_ANNOTATION_COUNT;

            // TODO: add a css for this
            text = text + "<span style=\"padding-left: 2px;\"><img id=\"" + idLocalAnnotationImg + "\" src=\"images/comment-small-filled.png\" title=\"" + UIUtil.getNiceNoteCountText(localAnnotationsCount) + " on this category. \nClick on the icon to see and edit the notes\" /></span>" + "<span id=\"" + idLocalAnnotationCnt + "\" style=\"font-size:95%;color:#15428B;font-weight:bold;\">" + localAnnotationsCount + "</span>";
        }

        final int childrenAnnotationsCount = entityData.getChildrenAnnotationsCount();
        if (childrenAnnotationsCount > 0) {
            text = text + " <span style=\"padding-left: 2px;\"><img src=\"images/comment-small.png\" title=\"" + UIUtil.getNiceNoteCountText(childrenAnnotationsCount) + " on the children of this category\" /></span>" + "<span style=\"font-size:90%;color:#999999;\">" + childrenAnnotationsCount + "</span>";
        }

        return text;
    }

    protected String createNodeWatchLabel(EntityData cls) {
        Set<Watch<?>> w = cls.getWatches();
        if (w.isEmpty()) {
            return "";
        }
//        switch (watchType) {
        if(w.iterator().next() instanceof EntityFrameWatch) {
            return "<img src=\"images/eye.png\" " + ClassTreePortlet.WATCH_ICON_STYLE_STRING + " title=\"" + " Watched\"></img>";
//            return "<img src=\"images/tag_blue.png\" " + ClassTreePortlet.WATCH_ICON_STYLE_STRING + " title=\"" + " Watched\"></img>";
        }
        else {
            return "<img src=\"images/eye-down.png\" " + ClassTreePortlet.WATCH_ICON_STYLE_STRING + " title=\"" + " Watched branch\"></img>";
//            return "<img src=\"images/tag_blue_add.png\" " + ClassTreePortlet.WATCH_ICON_STYLE_STRING + " title=\"" + " Watched branch\"></img>";
        }
//            case ENTITY_WATCH:
//
////                return "<img src=\"images/tag_blue.png\" " + ClassTreePortlet.WATCH_ICON_STYLE_STRING + " title=\"" + " Watched\"></img>";
//            case BRANCH_WATCH:
//
////                return "<img src=\"images/tag_blue_add.png\" " + ClassTreePortlet.WATCH_ICON_STYLE_STRING + " title=\"" + " Watched branch\"></img>";
//            case BOTH:
//                return "<img src=\"images/tag_blue.png\" " + ClassTreePortlet.WATCH_ICON_STYLE_STRING + " title=\"" + " Watched\"></img>" + "<img src=\"images/tag_blue_add.png\" " + ClassTreePortlet.WATCH_ICON_STYLE_STRING + " title=\"" + " Watched branch\"></img>";
//            default:
//                return "";
//        }
    }

    private void showClassNotes(final Node node) {
        SubclassEntityData subClassData = (SubclassEntityData) node.getUserObject();
        String name = subClassData.getName();
        OWLClass cls = DataFactory.getOWLClass(name);
        DiscussionThreadDialog.showDialog(getProjectId(), cls);
    }

    private boolean hasChild(final TreeNode parentNode, final String childId) {
        return getDirectChild(parentNode, childId) != null;
    }

    protected void createRoot(EntityData rootEnitity) {
        if (rootEnitity == null) {
            GWT.log("Root entity is null", null);
            rootEnitity = new EntityData("Root", "Root node is not defined");
        }
        remove(PLACE_HOLDER_PANEL);

        treePanel = createTreePanel();
        final TreeNode root = createTreeNode(rootEnitity);
        treePanel.setRootNode(root);
        add(treePanel);
        createSelectionListener();

        try {
            // FIXME: could not figure out why it throws exceptions sometimes, not elegant but it works
            doLayout();
        }
        catch (final Exception e) {
            GWT.log("Error at doLayout in class tree", e);
        }

        root.select();
        // MH: createTreeNode calls get subclasses, so it was being called twice
//        getSubclasses(rootEnitity.getName(), root);
        root.expand(); // TODO: does not seem to work always

        if (initialSelection == null) { //try to cover the links, not ideal
            initialSelection = GlobalSelectionManager.getGlobalSelection(getProjectId());
        }

        //happens only at initialization - if WebProtege is called with arguments to jump to a particular class..
        if (initialSelection != null) {
            setSelection(initialSelection);
            initialSelection = null;
        }
    }

    protected TreeNode getDirectChild(final TreeNode parentNode, final String childId) {
        final Node[] children = parentNode.getChildNodes();
        for (final Node child : children) {
            if (getNodeClsName(child).equals(childId)) {
                return (TreeNode) child;
            }
        }
        return null;
    }

    @Override
    protected void onRefresh() {
        if (treePanel == null) {
            return;
        }
        final Collection<EntityData> existingSelection = getSelection();
        // TODO: not ideal
        final TreeNode root = treePanel.getRootNode();

        treePanel.setVisible(false);
        root.collapse();

        final Node[] children = root.getChildNodes();
        if (children != null) {
            for (final Node element2 : children) {
                final TreeNode child = (TreeNode) element2;
                root.removeChild(child);
                setSubclassesLoaded(child, false);
            }
        }

        treePanel.setVisible(true);

        setSubclassesLoaded(root, false);

        // Try to set the previous selection
        // FIXME: This might not work: setting the selection should happen in the
        // async callback, but there is no easy way of doing this.
        if (existingSelection != null && existingSelection.size() > 0) {
            EntityData singleSelection = existingSelection.iterator().next();
            EntityData rootEntity = (EntityData) root.getUserObject();
            if (singleSelection.equals(rootEntity)) {
                root.expand();
            }
            setSelection(existingSelection);
        }
        else {
            root.expand(); //should trigger the loading of the children of root
        }
    }

    public void updateButtonStates() {
        if (hasWritePermission()) {
            if (createButton != null) {
                createButton.enable();
            }
            if (deleteButton != null && getDeleteEnabled()) {
                deleteButton.enable();
            }
        }
        else {
            if (createButton != null) {
                createButton.disable();
            }
            if (deleteButton != null) {
                deleteButton.disable();
            }
        }

        if (watchButton != null) {
            watchButton.setDisabled(Application.get().isGuestUser());
        }
    }

    public String getNodeClsName(final Node node) {
        final EntityData data = (EntityData) node.getUserObject();
        return data.getName();
    }

    public String getNodeBrowserText(final Node node) {
        final EntityData data = (EntityData) node.getUserObject();
        return data.getBrowserText();
    }

    @Override
    public void onPermissionsChanged() {
        updateButtonStates();
        onRefresh();
    }


    private EntityData toEntityData(OWLClassData clsData) {
        return new EntityData(clsData.getEntity().getIRI().toString(), clsData.getBrowserText());
    }

    /*
     * ************ Remote procedure calls *****************
     */

    class GetRootClassHandler extends AbstractAsyncHandler<EntityData> {

        @Override
        public void handleFailure(final Throwable caught) {
            if (getEl() != null) {
                // getEl().unmask();
            }
            GWT.log("RPC error at getting classes root ", caught);
        }

        @Override
        public void handleSuccess(final EntityData rootEnitity) {
            if (getEl() != null) {
                //   getEl().unmask();
            }
            createRoot(rootEnitity);
        }
    }

    class GetSubclassesOfClassHandler extends AbstractAsyncHandler<List<SubclassEntityData>> {

        private final String clsName;

        private final TreeNode parentNode;

        private final AsyncCallback<Object> endCallback;

        public GetSubclassesOfClassHandler(final String className, final TreeNode parentNode, final AsyncCallback<Object> endCallback) {
            super();
            this.clsName = className;
            this.parentNode = parentNode;
            this.endCallback = endCallback;
        }

        @Override
        public void handleFailure(final Throwable caught) {
            //getEl().unmask();
            GWT.log("RPC error at getting subclasses of " + clsName, caught);
            if (endCallback != null) {
                endCallback.onFailure(caught);
            }
        }

        @Override
        public void handleSuccess(final List<SubclassEntityData> children) {
//            boolean isFresh = !isSubclassesLoaded(parentNode);
            Set<OWLClass> existingSubclasses = new HashSet<OWLClass>();
            for(Node childNode : parentNode.getChildNodes()) {
                existingSubclasses.add(DataFactory.getOWLClass(getNodeClsName(childNode)));
            }

            for (final SubclassEntityData subclassEntityData : children) {
                OWLClass currentCls = DataFactory.getOWLClass(subclassEntityData.getName());
                if(!existingSubclasses.contains(currentCls)) {
                    final TreeNode childNode = createTreeNode(subclassEntityData);
                    if (subclassEntityData.getSubclassCount() > 0) {
                        childNode.setExpandable(true);
                    }
                    parentNode.appendChild(childNode);
//                    updateAncestorNoteCounts(subclassEntityData.getLocalAnnotationsCount(), childNode);
                }
            }

            setSubclassesLoaded(parentNode, true);
            if (endCallback != null) {
                endCallback.onSuccess(children);
            }
        }
    }

    class GetPropertyHierarchySubclassesOfClassHandler extends AbstractAsyncHandler<List<Triple>> {

        private final String clsName;

        private final TreeNode parentNode;

        public GetPropertyHierarchySubclassesOfClassHandler(final String className, final TreeNode parentNode) {
            super();
            this.clsName = className;
            this.parentNode = parentNode;
        }

        @Override
        public void handleFailure(final Throwable caught) {
            // getEl().unmask();
            GWT.log("RPC error at getting subproperties of " + clsName, caught);
        }

        @Override
        public void handleSuccess(final List<Triple> childTriples) {
            // getEl().unmask();
            if (childTriples != null) {
                for (final Triple childTriple : childTriples) {
                    final EntityData childData = childTriple.getValue();
                    if (!hasChild(parentNode, childData.getName())) {
                        final TreeNode childNode = createTreeNode(childData);
                        childNode.setExpandable(true); // TODO - we need to get the
                        // own slot values count
                        parentNode.appendChild(childNode);
                    }
                }
            }

            setSubclassesLoaded(parentNode, true);
        }
    }

    class CreateClassHandler extends AbstractAsyncHandler<CreateClassResult> {


        public CreateClassHandler() {
        }

        @Override
        public void handleFailure(final Throwable caught) {
            GWT.log("Error at creating class", caught);
            MessageBox.showErrorMessage("Class not created", caught);
        }

        @Override
        public void handleSuccess(final CreateClassResult result) {
            onClassCreated(result.getObject(), result.getSuperClasses());
            SubclassEntityData subClassData = new SubclassEntityData(result.getObject().getIRI().toString(), result.getBrowserText(result.getObject()).or(""), Collections.<EntityData>emptyList(), 0);
            onSubclassAdded(new EntityData(result.getPathToRoot().getSecondToLastElement().getIRI().toString()), Arrays.<EntityData>asList(subClassData), false);
            selectPathInTree(result.getPathToRoot());
        }
    }

    class DeleteClassHandler extends AbstractAsyncHandler<DeleteEntityResult> {

        @Override
        public void handleFailure(final Throwable caught) {
            GWT.log("Error at deleting class", caught);
            MessageBox.showErrorMessage("Class not deleted", caught);
        }

        @Override
        public void handleSuccess(final DeleteEntityResult result) {
            GWT.log("Delete successfully class ", null);

        }
    }

    public class MoveClassHandler extends AbstractAsyncHandler<List<EntityData>> {

        private final String clsName;

        private final String oldParentName;

        private final String newParentName;

        public MoveClassHandler(final String clsName, final String oldParentName, final String newParentName) {
            this.clsName = clsName;
            this.oldParentName = oldParentName;
            this.newParentName = newParentName;
        }

        @Override
        public void handleFailure(final Throwable caught) {
            GWT.log("Error at moving class", caught);
            MessageBox.showErrorMessage("Class not moved", caught);
            // TODO: refresh oldParent and newParent
        }

        @Override
        public void handleSuccess(final List<EntityData> result) {
            GWT.log("Moved successfully class " + clsName, null);
            if (result == null) {
                //MessageBox.alert("Success", "Class moved successfully.");
            }
            else {
                GWT.log("Cycle warning after moving class " + clsName + ": " + result, null);


                String warningMsg = "<B>WARNING! There is a cycle in the hierarchy: </B><BR><BR>";
                for (EntityData p : result) {
                    warningMsg += "&nbsp;&nbsp;&nbsp;&nbsp;" + p.getBrowserText() + "<BR>";
                }
                warningMsg += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...";
                MessageBox.showAlert("Cycles introduced during class move", "Class moved successfully.<BR>" +
                        "<BR>" +
                        warningMsg);
            }

        }
    }

    class RenameClassHandler extends AbstractAsyncHandler<EntityData> {

        @Override
        public void handleFailure(final Throwable caught) {
            MessageBox.showErrorMessage("Class not renamed", caught);
        }

        @Override
        public void handleSuccess(final EntityData result) {
            GWT.log("Rename succeded", null);
        }
    }

    class GetPathToRootHandler extends AbstractAsyncHandler<List<EntityData>> {

        @Override
        public void handleFailure(final Throwable caught) {
            GWT.log("Error at finding path to root", caught);
        }

        @Override
        public void handleSuccess(final List<EntityData> result) {
            GWT.log(result.toString(), null);
            if (result == null || result.size() == 0) {
                GWT.log("Could not find path in the tree", null);
                return;
            }
            String path = "";
            for (final EntityData entity : result) {
                path = path + entity.getName() + " --> <br/>";
            }
            path = path.substring(0, path.length() - 10);
            GWT.log("Selection path in tree: " + path, null);
            selectPathInTree(result);
        }
    }

    class SelectInTreeHandler extends AbstractAsyncHandler<List<SubclassEntityData>> {

        private final TreeNode parentNode;

        private final List<EntityData> path;

        private final int index;

        public SelectInTreeHandler(final TreeNode parentNode, final List<EntityData> path, final int index) {
            super();
            this.parentNode = parentNode;
            this.index = index;
            this.path = path;
        }

        @Override
        public void handleFailure(final Throwable caught) {
            //getEl().unmask();
            GWT.log("RPC error at select in tree for " + parentNode.getUserObject(), caught);
        }

        @Override
        public void handleSuccess(final List<SubclassEntityData> children) {
            // getEl().unmask();

            TreeNode pathTreeNode = null;

            final EntityData nextParent = path.get(index);

            for (final SubclassEntityData subclassEntityData : children) {
                final SubclassEntityData childData = subclassEntityData;
                if (!hasChild(parentNode, childData.getName())) {
                    final TreeNode childNode = createTreeNode(childData);
                    if (childData.getSubclassCount() > 0) {
                        childNode.setExpandable(true);
                    }
                    parentNode.appendChild(childNode);
                }
                if (childData.equals(nextParent)) {
                    pathTreeNode = getDirectChild(parentNode, childData.getName());
                }
            }

            setSubclassesLoaded(parentNode, true);

            if (pathTreeNode != null) {
                expandDisabled = true;
                parentNode.expand();
                expandDisabled = false;
                if (path.size() - 1 == index) {
                    pathTreeNode.select();
                    final EntityData entityData = (EntityData) pathTreeNode.getUserObject();
                    setEntity(entityData);
                }
                else {
                    selectPathInTree(path, pathTreeNode, index + 1);
                }
            }
            else {
                GWT.log("Error at select in tree: could not find child " + nextParent + " of " + parentNode.getUserObject(), null);
            }
        }
    }




}
