package edu.stanford.bmir.protege.web.client.collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.app.ForbiddenViewImpl;
import edu.stanford.bmir.protege.web.client.app.NothingSelectedViewImpl;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletUiImpl;
import edu.stanford.bmir.protege.web.shared.collection.CollectionItem;
import edu.stanford.protege.widgetmap.client.*;
import edu.stanford.protege.widgetmap.client.view.ViewHolder;
import edu.stanford.protege.widgetmap.shared.node.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Jul 2017
 */
public class CollectionViewImpl extends Composite implements CollectionView {

    private static final TerminalNodeId OBJECT_LIST_NODE_ID = new TerminalNodeId("ObjectList");

    private static final TerminalNodeId FORM_VIEW_NODE_ID = new TerminalNodeId("FormView");

    private final ViewHolder formHolder;

    @Nonnull
    private AddItemHandler addItemHandler = () -> {};

    @Nonnull
    private ClearItemDataHandler clearItemDataHandler = () -> {};

    interface CollectionViewImplUiBinder extends UiBinder<HTMLPanel, CollectionViewImpl> {

    }

    private static CollectionViewImplUiBinder ourUiBinder = GWT.create(CollectionViewImplUiBinder.class);

    private SimplePanel formContainer = new SimplePanel();

    private SimplePanel listContainer = new SimplePanel();

    @UiField(provided = true)
    WidgetMapPanel widgetMap;

    @UiField
    Label collectionTitle;

    @Inject
    public CollectionViewImpl() {
        ParentNode rootNode = new ParentNode(Direction.ROW);
        rootNode.addChild(new TerminalNode(OBJECT_LIST_NODE_ID), 0.3);
        rootNode.addChild(new TerminalNode(FORM_VIEW_NODE_ID), 0.7);
        WidgetMapRootWidget rootWidget = new WidgetMapRootWidget();
        PortletUiImpl ui = new PortletUiImpl(new ForbiddenViewImpl(),
                                             new NothingSelectedViewImpl());
        ui.setWidget(formContainer);
        ui.addPortletAction(new PortletAction("Clear", (action, event) -> clearItemDataHandler.handleClearItemData()));
        formHolder = new ViewHolder(ui, NodeProperties.emptyNodeProperties());
        formContainer.setWidth("100%");
        formContainer.setHeight("100%");
        formHolder.setCloseable(false);



        widgetMap = new WidgetMapPanel(rootWidget, new WidgetMapPanelManager(rootWidget, terminalNode -> {
            if(terminalNode.getNodeId().equals(OBJECT_LIST_NODE_ID)) {
                PortletUiImpl widgets = new PortletUiImpl(new ForbiddenViewImpl(),
                                                          new NothingSelectedViewImpl());
                widgets.setToolbarVisible(true);
                widgets.addPortletAction(new PortletAction("Add", (action, event) -> addItemHandler.handleAddItem()));
                widgets.setTitle("Amino Acids");
                widgets.setWidget(listContainer);
                ViewHolder viewHolder = new ViewHolder(widgets, terminalNode.getNodeProperties());
                viewHolder.setCloseable(false);
                return viewHolder;
            }
            else {
                return formHolder;
            }
        }));
        widgetMap.setRootNode(rootNode);
        Scheduler.get().scheduleDeferred(() -> {
            widgetMap.onResize();
        });
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setAddItemHandler(AddItemHandler handler) {
        this.addItemHandler = checkNotNull(handler);
    }

    @Override
    public void setClearItemDataHandler(ClearItemDataHandler handler) {
        this.clearItemDataHandler = checkNotNull(handler);
    }

    @Override
    public void onResize() {
        widgetMap.onResize();
    }

    @Override
    public void setCollectionTitle(@Nonnull String title) {
        this.collectionTitle.setText(checkNotNull(title));
    }

    @Override
    public void setItem(@Nonnull CollectionItem id) {
        this.formHolder.setViewLabel(id.getName());
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getFormContainer() {
        return formContainer;
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getListContainer() {
        return listContainer;
    }
}