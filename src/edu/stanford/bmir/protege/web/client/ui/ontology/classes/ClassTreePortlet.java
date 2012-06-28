package edu.stanford.bmir.protege.web.client.ui.ontology.classes;

import java.util.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.data.Node;
import com.gwtext.client.dd.DragData;
import com.gwtext.client.dd.DragDrop;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Container;
import com.gwtext.client.widgets.CycleButton;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.MessageBoxConfig;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Tool;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.ValidationException;
import com.gwtext.client.widgets.form.Validator;
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

import edu.stanford.bmir.protege.web.client.model.GlobalSettings;
import edu.stanford.bmir.protege.web.client.model.Project;
import edu.stanford.bmir.protege.web.client.model.event.*;
import edu.stanford.bmir.protege.web.client.model.listener.OntologyListenerAdapter;
import edu.stanford.bmir.protege.web.client.rpc.AbstractAsyncHandler;
import edu.stanford.bmir.protege.web.client.rpc.ChAOServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.OntologyServiceManager;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.SubclassEntityData;
import edu.stanford.bmir.protege.web.client.rpc.data.Triple;
import edu.stanford.bmir.protege.web.client.rpc.data.ValueType;
import edu.stanford.bmir.protege.web.client.rpc.data.Watch;
import edu.stanford.bmir.protege.web.client.rpc.data.layout.PortletConfiguration;
import edu.stanford.bmir.protege.web.client.ui.ontology.notes.NoteInputPanel;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.search.SearchUtil;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionEvent;
import edu.stanford.bmir.protege.web.client.ui.selection.SelectionListener;
import edu.stanford.bmir.protege.web.client.ui.util.GlobalSelectionManager;
import edu.stanford.bmir.protege.web.client.ui.util.UIUtil;

/**
 * Portlet for displaying class trees. It can be configured to show only a
 * subtree of an ontology, by setting the portlet property <code>topClass</code>
 * to the name of the top class to show. <br>
 * Also supports creating and editing classes.
 * @author Tania Tudorache <tudorache@stanford.edu>
 */
public class ClassTreePortlet extends AbstractEntityPortlet {

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

    private boolean allowsMultiSelection = false;

    private String hierarchyProperty = null;

    private String topClass = null;

    private Collection<EntityData> initialSelection = null;

    private TreeNodeListenerAdapter nodeListener;


    /*
    * Configuration constants and defaults
    */
    private static Set<EntityData> nodesWithNotesOpen = new HashSet<EntityData>();

    public ClassTreePortlet(final Project project) {
        this(project, true, true, true, false, null);
    }

    public ClassTreePortlet(final Project project, final boolean showToolbar, final boolean showTitle, final boolean showTools, final boolean allowsMultiSelection, final String topClass) {
        super(project, false);
        this.showToolbar = showToolbar;
        this.showTitle = showTitle;
        this.showTools = showTools;
        this.allowsMultiSelection = allowsMultiSelection;
        this.topClass = topClass;

        initialize();
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

    private void showInternalID(EntityData entity) {
        String classDisplayName = entity.getBrowserText();
        String className = entity.getName();
        String message = "The internal ID of the class ";
        message += "<BR>&nbsp;&nbsp;&nbsp;&nbsp;<I>" + classDisplayName + "</I>";
        message += "<BR>is:";
        message += "<BR>&nbsp;&nbsp;&nbsp;&nbsp;<B>" + className + "</B>";
        MessageBox.alert("Internal ID", message);
    }

    private void showDirectLink(EntityData entity) {
        MessageBoxConfig mbConfig = new MessageBoxConfig();
        mbConfig.setTitle("Direct link");
        mbConfig.setMinWidth(300);
        mbConfig.setWidth(600);
        mbConfig.setMaxWidth(800);
        mbConfig.setMsg("If you wish to link to this class from a different application, first select the URL below, then copy and paste it into your application.<BR>");
        mbConfig.setPrompt(true);

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
                    replace("{1}", URL.encodeQueryString(getProject().getProjectName())).
                    replace("{2}", tabName).
                    replace("{3}", className == null ? "" : URL.encodeQueryString(className));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        mbConfig.setValue(url);
        mbConfig.setButtons(MessageBox.OK);
        MessageBox.show(mbConfig);

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
        addProjectListeners();

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

    protected void addProjectListeners() {
        if (hierarchyProperty != null) { // hierarchy of property
            project.addOntologyListener(new OntologyListenerAdapter() {
                @Override
                public void entityRenamed(final EntityRenameEvent renameEvent) {
                    onClassRename(renameEvent.getEntity(), renameEvent.getOldName());
                }

                @Override
                public void propertyValueAdded(final PropertyValueEvent propertyValueEvent) {
                    GWT.log("Property value added event: " + propertyValueEvent.getEntity() + " " + propertyValueEvent.getProperty() + " " + propertyValueEvent.getAddedValues(), null);
                    if (propertyValueEvent.getProperty().getName().equals(hierarchyProperty)) {
                        onSubclassAdded(propertyValueEvent.getEntity(), propertyValueEvent.getAddedValues(), false);
                    }
                }

                @Override
                public void propertyValueRemoved(final PropertyValueEvent propertyValueEvent) {
                    GWT.log("Property value removed event: " + propertyValueEvent.getEntity() + " " + propertyValueEvent.getProperty() + " " + propertyValueEvent.getRemovedValues(), null);
                    if (propertyValueEvent.getProperty().getName().equals(hierarchyProperty)) {
                        onSubclassRemoved(propertyValueEvent.getEntity(), propertyValueEvent.getRemovedValues());
                    }
                }

                @Override
                public void propertyValueChanged(final PropertyValueEvent propertyValueEvent) {
                    GWT.log("Property value changed event: " + propertyValueEvent.getEntity() + " " + propertyValueEvent.getProperty() + " " + propertyValueEvent.getAddedValues() + "  " + propertyValueEvent.getRemovedValues(), null);
                }

                @Override
                public void entityBrowserTextChanged(EntityBrowserTextChangedEvent event) {
                    onEntityBrowserTextChanged(event);
                }
            });
        }
        else { // subclass of
            project.addOntologyListener(new OntologyListenerAdapter() {
                @Override
                public void entityCreated(final EntityCreateEvent ontologyEvent) {
                    if (ontologyEvent.getType() == EventType.SUBCLASS_ADDED) {
                        onSubclassAdded(ontologyEvent.getEntity(), ontologyEvent.getSuperEntities(), false);
                    }
                    else if (ontologyEvent.getType() == EventType.SUBCLASS_REMOVED) {
                        onSubclassRemoved(ontologyEvent.getEntity(), ontologyEvent.getSuperEntities());
                    }
                }

                @Override
                public void entityRenamed(final EntityRenameEvent renameEvent) {
                    onClassRename(renameEvent.getEntity(), renameEvent.getOldName());
                }

                @Override
                public void entityBrowserTextChanged(EntityBrowserTextChangedEvent event) {
                    onEntityBrowserTextChanged(event);
                }
            });
        }
    }



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
                onCreateCls();
            }
        });
        createButton.setDisabled(!project.hasWritePermission(GlobalSettings.getGlobalSettings().getUserName()));
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
        deleteButton.setDisabled(!project.hasWritePermission(GlobalSettings.getGlobalSettings().getUserName()));
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
        watchItem.setIcon("images/tag_blue.png");
        watchButton.addItem(watchItem);

        watchBranchItem = new CheckItem();
        watchBranchItem.setText(getWatchBranchClsLabel());
        watchBranchItem.setCls("toolbar-button");
        watchBranchItem.setIcon("images/tag_blue_add.png");
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
        Watch watch = entityData.getWatch();
        if (watch == Watch.BOTH || watch == Watch.ENTITY_WATCH || watch == Watch.BRANCH_WATCH) {
            watchButton.setActiveItem(unwatchBranchItem);
        }
        else if (watch == null) {
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
                    SearchUtil searchUtil = new SearchUtil(project, ClassTreePortlet.this, getSearchAsyncCallback());
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
                MessageBox.alert("Error.", "There were problems at search, please try again later.");
            }

            public void onSuccess(Boolean result) {
                if (!result) {
                    MessageBox.alert("No results", "No results were found. Please try a different query. <br />" + "<BR>" + "<B>Hint:</B> You may use wildcards (*) in your search query. <br />" + "&nbsp;&nbsp;&nbsp;&nbsp;(Wildcards are automatically added before and after query strings that&nbsp;&nbsp;&nbsp;&nbsp;<br />" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;have at least 3 characters AND do not already start or end with a wildcard.)&nbsp;&nbsp;&nbsp;&nbsp;");
                }
            }
        };
    }

    protected void addDragAndDropSupport() {
        treePanel.addListener(new TreePanelListenerAdapter() {
            @Override
            public boolean doBeforeNodeDrop(final TreePanel treePanel, final TreeNode target, final DragData dragData, final String point, final DragDrop source, final TreeNode dropNode, final DropNodeCallback dropNodeCallback) {
                if (project.hasWritePermission()) {
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

    protected TreeNode findTreeNode(final String id) {
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

    protected void onSubclassRemoved(final EntityData entity, final Collection<EntityData> subclasses) {
        if (subclasses == null || subclasses.size() == 0) {
            return;
        }

        final EntityData subclass = ((List<EntityData>) subclasses).get(0);
        final TreeNode parentNode = findTreeNode(entity.getName());

        if (parentNode == null) {
            return;
        }

        // final TreeNode subclassNode = findTreeNode(parentNode, subclass.getName(), new ArrayList<TreeNode>());
        final TreeNode subclassNode = getDirectChild(parentNode, subclass.getName());
        if (subclassNode == null) {
            return;
        }

        //if (subclassNode.getParentNode().equals(parentNode)) {
        parentNode.removeChild(subclassNode);
        if (parentNode.getChildNodes().length < 1) {
            parentNode.setExpandable(false);
        }
        //}
    }

    /**
     * Called to update the browser text in the tree
     * @param event The event that describes the browser text change that happened.
     */
    protected void onEntityBrowserTextChanged(EntityBrowserTextChangedEvent event) {
        EntityData entity = event.getEntity();
        updateBrowserTextForEntity(entity);
    }

    /**
     * Updates the browser text of nodes that display the specified entity in the tree.
     * @param entity The entity.
     */
    private void updateBrowserTextForEntity(EntityData entity) {
        TreeNode node = findTreeNode(entity.getName());
        if(node != null) {
            node.setText(entity.getBrowserText());
        }
    }


    protected void onClassCreated(final EntityData entity, final List<EntityData> superEntities) {
        if (superEntities == null) {
            GWT.log("Entity created: " + entity.getName() + " but unknown superEntities", null);
            return;
        }
//        for (final EntityData entityData : superEntities) {
//            final EntityData superEntity = entityData;
//            final TreeNode parentNode = findTreeNode(superEntity.getName());
//            if (parentNode != null && !isSubclassesLoaded(parentNode)) {
//                insertNodeInTree(parentNode, entity);
//            }
//        }
    }

    protected void onClassRename(final EntityData entity, final String oldName) {
        final TreeNode oldNode = findTreeNode(entity.getName());
        if (oldNode == null) {
            return;
        }
        final TreeNode newNode = createTreeNode(entity);
        oldNode.getParentNode().replaceChild(newNode, oldNode);

    }


    protected void insertNodeInTree(final TreeNode parentNode, final EntityData child) {
        if (!hasChild(parentNode, child.getName())) {
            parentNode.appendChild(createTreeNode(child));
        }
    }

    protected void onCreateCls() {
        final com.gwtext.client.widgets.Window window = new com.gwtext.client.widgets.Window();
        window.setTitle("Create new class");
        window.setWidth(510);
        window.setHeight(115);
        window.setPaddings(5);
        window.setLayout(new FitLayout());
        window.setButtonAlign(Position.CENTER);
        window.setModal(true);

        FormPanel panel = new FormPanel();
        panel.setPaddings(5);
        final TextField textField = new TextField("New class name", "className", 350);

        final Button okButton = new Button("OK", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                String text = textField.getText();
                createCls(text);
                window.hide();
                window.destroy();
            }
        });

        Button cancelButton = new Button("Cancel", new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject e) {
                window.hide();
                window.destroy();
            }
        });

        window.addListener(new PanelListenerAdapter() {
            @Override
            public void onAfterLayout(Container self) {
                //TT: I really did not want to use a timer, but could not find another way of making the focus work
                Timer timer = new Timer() {
                    @Override
                    public void run() {
                        textField.focus();
                    }
                };
                timer.schedule(100);
            }
        });

        textField.setValidator(new Validator() {
            public boolean validate(String value) throws ValidationException {
                boolean valid = isValidClassName(value);
                okButton.setDisabled(!valid);
                okButton.setTooltip(valid ? "" : "Class name is invalid");
                return valid;
            }
        });

        textField.addListener(new TextFieldListenerAdapter() {
            @Override
            public void onSpecialKey(Field field, EventObject e) {
                if (e.getKey() == KeyCodes.KEY_ENTER) {
                    if (textField.isValid()) {
                        createCls(textField.getText());
                        window.hide();
                        window.destroy();
                    }
                }
            }
        });

        panel.add(textField);
        panel.addButton(okButton);
        panel.addButton(cancelButton);
        window.add(panel);

        window.show();
    }

    protected boolean isValidClassName(String value) {
        return value != null && value.length() > 0 && UIUtil.getIdentifierStart(value, value.length()) == 0;
    }

    protected void createCls(final String className) {
        String superClsName = null;
        final EntityData currentSelection = getSingleSelection();
        if (currentSelection != null) {
            superClsName = currentSelection.getName();
        }

        OntologyServiceManager.getInstance().createCls(project.getProjectName(), className, superClsName, getInheritMetaClasses(), GlobalSettings.getGlobalSettings().getUserName(), getCreateClsDescription() + " " + className, getCreateClassAsyncHandler(superClsName, className));
    }

    protected AbstractAsyncHandler<EntityData> getCreateClassAsyncHandler(final String superClsName, final String className) {
        return new CreateClassHandler(superClsName, className);
    }

    protected void onDeleteCls() {
        final EntityData currentSelection = getSingleSelection();
        if (currentSelection == null) {
            Window.alert("Please select first a class to delete.");
            return;
        }

        final String displayName = currentSelection.getBrowserText();
        final String clsName = currentSelection.getName();

        MessageBox.confirm("Confirm", "Are you sure you want to delete class <br> " + displayName + " ?", new MessageBox.ConfirmCallback() {
            public void execute(final String btnID) {
                if (btnID.equals("yes")) {
                    deleteCls(clsName);
                }
            }
        });
    }

    protected void deleteCls(final String className) {
        GWT.log("Should delete class with name: " + className, null);
        if (className == null) {
            return;
        }

        OntologyServiceManager.getInstance().deleteEntity(project.getProjectName(), className, GlobalSettings.getGlobalSettings().getUserName(), getDeleteClsDescription() + " " + className, new DeleteClassHandler());

        refreshFromServer(500);
    }

    protected void onWatchCls() {
        final EntityData currentSelection = getSingleSelection();

        Watch watch = currentSelection.getWatch();
        if (currentSelection == null || watch == Watch.ENTITY_WATCH) {
            return;
        }

        ChAOServiceManager.getInstance().addWatchedEntity(project.getProjectName(), GlobalSettings.getGlobalSettings().getUserName(), currentSelection.getName(), new AddWatchedCls(getSingleSelectedTreeNode()));
    }

    protected void onWatchBranchCls() {
        final EntityData currentSelection = getSingleSelection();

        Watch watch = currentSelection.getWatch();
        if (currentSelection == null || watch == Watch.BRANCH_WATCH) {
            return;
        }

        ChAOServiceManager.getInstance().addWatchedBranchEntity(project.getProjectName(), GlobalSettings.getGlobalSettings().getUserName(), currentSelection.getName(), new AddWatchedCls(getSingleSelectedTreeNode()));
    }

    protected void onUnwatchCls() {
        final EntityData currentSelection = getSingleSelection();

        Watch watch = currentSelection.getWatch();
        if (currentSelection == null || watch == null) {
            return;
        }

        ChAOServiceManager.getInstance().removeWatchedEntity(project.getProjectName(), GlobalSettings.getGlobalSettings().getUserName(), currentSelection.getName(), new RemoveWatchedCls(getSingleSelectedTreeNode()));
    }

    protected void renameClass(final String oldName, final String newName) {
        GWT.log("Should rename class from " + oldName + " to " + newName, null);
        if (oldName.equals(newName) || newName == null || newName.length() == 0) {
            return;
        }

        OntologyServiceManager.getInstance().renameEntity(project.getProjectName(), oldName, newName, GlobalSettings.getGlobalSettings().getUserName(), getRenameClsDescription() + " " + "Old name: " + oldName + ", New name: " + newName, new RenameClassHandler());
    }

    public TreePanel getTreePanel() {
        return treePanel;
    }

    @Override
    protected void afterRender() {
        getRootCls();
    }

    public void setTreeNodeIcon(final TreeNode node, EntityData entityData) {
        node.setIconCls("protege-class-icon");
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
            OntologyServiceManager.getInstance().getEntityTriples(project.getProjectName(), subjects, props, new GetPropertyHierarchySubclassesOfClassHandler(parentClsName, parentNode));
        }
    }

    protected void invokeGetSubclassesRemoteCall(final String parentClsName, AsyncCallback<List<SubclassEntityData>> callback) {
        OntologyServiceManager.getInstance().getSubclasses(project.getProjectName(), parentClsName, callback);
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
            OntologyServiceManager.getInstance().getEntity(project.getProjectName(), rootClsName, new GetRootClassHandler());
        }
        else {
            OntologyServiceManager.getInstance().getRootEntity(project.getProjectName(), new GetRootClassHandler());
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
        OntologyServiceManager.getInstance().moveCls(project.getProjectName(), cls.getName(), oldParent.getName(), newParent.getName(), false, GlobalSettings.getGlobalSettings().getUserName(), UIUtil.getAppliedToTransactionString(getMoveClsOperationDescription(cls, oldParent, newParent), cls.getName()), new MoveClassHandler(cls.getName(), oldParent.getName(), newParent.getName()));
    }

    protected String getMoveClsOperationDescription(final EntityData cls, final EntityData oldParent, final EntityData newParent) {
        return getMoveClsDescription() + ": " + UIUtil.getDisplayText(cls) + ". Old parent: " + UIUtil.getDisplayText(oldParent) + ", New parent: " + UIUtil.getDisplayText(newParent);
    }

    public void getPathToRoot(final EntityData entity) {
        OntologyServiceManager.getInstance().getPathToRoot(project.getProjectName(), entity.getName(), new GetPathToRootHandler());
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

        node.setText(createNodeRenderText(node));

        node.addListener(nodeListener);

        return node;
    }

    protected String createNodeRenderText(TreeNode node) {
        EntityData entityData = (EntityData) node.getUserObject();
        String text = createNodeText(entityData) + createNodeNoteCount(entityData, node) + createNodeWatchLabel(entityData);
        return text;
    }

    protected String createNodeText(EntityData entityData) {
        return entityData.getBrowserText();
    }

    protected String createNodeNoteCount(EntityData entityData, TreeNode node) {
        String text = "";

        final int localAnnotationsCount = entityData.getLocalAnnotationsCount();
        if (localAnnotationsCount > 0) {
            final String idLocalAnnotationImg = node.getId() + SUFFIX_ID_LOCAL_ANNOTATION_IMG;
            final String idLocalAnnotationCnt = node.getId() + SUFFIX_ID_LOCAL_ANNOTATION_COUNT;

            // TODO: add a css for this
            text = text + "<img id=\"" + idLocalAnnotationImg + "\" src=\"images/comment.gif\" title=\"" + UIUtil.getNiceNoteCountText(localAnnotationsCount) + " on this category. \nClick on the icon to see and edit the notes\" />" + "<span id=\"" + idLocalAnnotationCnt + "\" style=\"font-size:95%;color:#15428B;font-weight:bold;\">" + localAnnotationsCount + "</span>";
        }

        final int childrenAnnotationsCount = entityData.getChildrenAnnotationsCount();
        if (childrenAnnotationsCount > 0) {
            text = text + " <img src=\"images/comment_small.gif\" title=\"" + UIUtil.getNiceNoteCountText(childrenAnnotationsCount) + " on the children of this category\" />" + "<span style=\"font-size:90%;color:#999999;\">" + childrenAnnotationsCount + "</span>";
        }

        return text;
    }

    protected String createNodeWatchLabel(EntityData cls) {
        Watch watch = cls.getWatch();
        if (watch == null) {
            return "";
        }
        switch (watch) {
            case ENTITY_WATCH:
                return "<img src=\"images/tag_blue.png\" " + ClassTreePortlet.WATCH_ICON_STYLE_STRING + " title=\"" + " Watched\"></img>";
            case BRANCH_WATCH:
                return "<img src=\"images/tag_blue_add.png\" " + ClassTreePortlet.WATCH_ICON_STYLE_STRING + " title=\"" + " Watched branch\"></img>";
            case BOTH:
                return "<img src=\"images/tag_blue.png\" " + ClassTreePortlet.WATCH_ICON_STYLE_STRING + " title=\"" + " Watched\"></img>" + "<img src=\"images/tag_blue_add.png\" " + ClassTreePortlet.WATCH_ICON_STYLE_STRING + " title=\"" + " Watched branch\"></img>";
            default:
                return "";
        }
    }

    private void showClassNotes(final Node node) {
        final EntityData entity = (EntityData) node.getUserObject();

        final com.gwtext.client.widgets.Window window = new com.gwtext.client.widgets.Window();
        window.setTitle("View/Edit Notes on " + entity.getBrowserText());
        window.setWidth(600);
        window.setHeight(480);
        window.setMinWidth(300);
        window.setMinHeight(350);
        window.setLayout(new FitLayout());
        window.setPaddings(5);
        window.setButtonAlign(Position.CENTER);

        //window.setCloseAction(Window.HIDE);
        window.setPlain(true);

        window.addListener(new WindowListenerAdapter() {
            @Override
            public void onClose(Panel panel) {
                nodesWithNotesOpen.remove(entity);
            }

        });

        final NoteInputPanel nip = new NoteInputPanel(getProject(), "Please enter your note:", true, entity, window);

        window.add(nip);
        window.show();

        nodesWithNotesOpen.add(entity);
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
            initialSelection = GlobalSelectionManager.getGlobalSelection(getProject().getProjectName());
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
        if (project.hasWritePermission(GlobalSettings.getGlobalSettings().getUserName())) {
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
            watchButton.setDisabled(!GlobalSettings.getGlobalSettings().isLoggedIn());
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
    public void onPermissionsChanged(final Collection<String> permissions) {
        updateButtonStates();
        onRefresh();
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
            //getEl().unmask();

            // TODO:  MH - This is very very slow
            int i = 0;
            boolean isFresh = !isSubclassesLoaded(parentNode);
            for (final SubclassEntityData subclassEntityData : children) {
                final SubclassEntityData childData = subclassEntityData;
                if (isFresh || !hasChild(parentNode, childData.getName())) {
                    final TreeNode childNode = createTreeNode(childData);
                    if (childData.getSubclassCount() > 0) {
                        childNode.setExpandable(true);
                    }
                    parentNode.appendChild(childNode);
                }
//                System.out.println("Done: " + i);
//                i++;
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

    class CreateClassHandler extends AbstractAsyncHandler<EntityData> {

        private final String superClsName;

        private final String className;

        public CreateClassHandler(final String superClsName, final String className) {
            this.superClsName = superClsName;
            this.className = className;
        }

        @Override
        public void handleFailure(final Throwable caught) {
            GWT.log("Error at creating class", caught);
            MessageBox.alert("There were errors at creating class.<br>" + " Message: " + caught.getMessage());
        }

        @Override
        public void handleSuccess(final EntityData entityData) {
            if (entityData != null) {
                GWT.log("Created successfully class " + entityData.getName(), null);

                OntologyServiceManager.getInstance().getPathToRoot(project.getProjectName(), entityData.getName(), new AsyncCallback<List<EntityData>>() {
                    public void onFailure(Throwable caught) {
                    }

                    public void onSuccess(List<EntityData> result) {
//                        System.out.println("The path to root is");
//                        StringBuilder sb = new StringBuilder();
//                        for (EntityData ed : result) {
//                            System.out.println("\t" + ed.getName());
//                            sb.append(ed.getName());
//                        }
                        EntityData parentData = result.get(result.size() - 2);
                        onClassCreated(entityData, Arrays.asList(parentData));
                        selectPathInTree(result);
                    }
                });

                project.forceGetEvents();

                final Timer timer = new Timer() {
                    @Override
                    public void run() {
                        final TreeNode parentNode = findTreeNode(superClsName);
                        if (parentNode != null) {
                            parentNode.expand();
                        }

                        final TreeNode newNode = getDirectChild(parentNode, className);
                        if (newNode != null) {
                            newNode.select();
                        }
                    }
                };
                timer.schedule(1000);


            }
            else {
                GWT.log("Problem at creating class", null);
                MessageBox.alert("Class creation failed.");
            }
        }
    }

    class DeleteClassHandler extends AbstractAsyncHandler<Void> {

        @Override
        public void handleFailure(final Throwable caught) {
            GWT.log("Error at deleting class", caught);
            MessageBox.alert("There were errors at deleting class.<br>" + " Message: " + caught.getMessage());
        }

        @Override
        public void handleSuccess(final Void result) {
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
            MessageBox.alert("There were errors at moving class.<br>" + " Message: " + caught.getMessage());
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
                MessageBox.alert("Warning", "Class moved successfully.<BR>" +
                        "<BR>" +
                        warningMsg);
            }

        }
    }

    class RenameClassHandler extends AbstractAsyncHandler<EntityData> {

        @Override
        public void handleFailure(final Throwable caught) {
            MessageBox.alert("Class rename failed.<br>" + "Message: " + caught.getMessage());
        }

        @Override
        public void handleSuccess(final EntityData result) {
            GWT.log("Rename succeded!", null);
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

    class AddWatchedCls extends AbstractAsyncHandler<EntityData> {

        private final TreeNode node;

        public AddWatchedCls(final TreeNode node) {
            this.node = node;
        }

        @Override
        public void handleFailure(final Throwable caught) {
            GWT.log("Error at add watched entity", caught);
            MessageBox.alert("Error", "There was an error at adding the new watched entity. Please try again later.");
        }

        @Override
        public void handleSuccess(final EntityData entityData) {
            node.setUserObject(entityData);
            node.setText(createNodeRenderText(node));
            updateWatchedMenuState(UIUtil.createCollection(entityData));
        }
    }

    class RemoveWatchedCls extends AbstractAsyncHandler<Void> {

        private final TreeNode node;

        public RemoveWatchedCls(final TreeNode node) {
            this.node = node;
        }

        @Override
        public void handleSuccess(final Void result) {
            //FIXME: this should return an entity data and have the same implementation as AddWatch, until then, a workaround..
            EntityData entityData = (EntityData) node.getUserObject();
            entityData.setWatch(null);
            node.setText(createNodeRenderText(node));
            updateWatchedMenuState(UIUtil.createCollection(entityData));
        }

        @Override
        public void handleFailure(final Throwable caught) {
            GWT.log("Error at remove watched entity", caught);
            MessageBox.alert("Error", "There was an error at adding the new watched entity. Pleas try again later.");
        }

    }

}
