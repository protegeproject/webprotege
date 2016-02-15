package edu.stanford.bmir.protege.web.client.perspective;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;
import com.google.inject.assistedinject.Assisted;
import edu.stanford.bmir.protege.web.client.ui.LayoutUtil;
import edu.stanford.bmir.protege.web.client.ui.portlet.AbstractEntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.portlet.EntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.tab.PortletContainer;
import edu.stanford.bmir.protege.web.client.ui.tab.TabId;
import edu.stanford.bmir.protege.web.shared.HasDispose;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.protege.widgetmap.client.*;
import edu.stanford.protege.widgetmap.client.view.ViewHolder;
import edu.stanford.protege.widgetmap.shared.node.*;

import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.MoreObjects.toStringHelper;


public final class Perspective extends Composite implements PortletContainer, IsWidget, HasRootNode, RequiresResize, HasDispose {

    private final List<EntityPortlet> entityPortlets = new ArrayList<>();


    private final PerspectiveId perspectiveId;


    private final WidgetMapPanel widgetMapPanel;

    private final WidgetMapPanelManager panelManager;



    private Node rootNode;

    private Map<TerminalNodeId, EntityPortlet> portletMap = new HashMap<>();

    @Inject
    public Perspective(@Assisted final PerspectiveId perspectiveId) {
        super();
        this.perspectiveId = perspectiveId;
        addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent event) {
                activatePortlets();
                panelManager.setRootNode(rootNode);
            }
        });

//        baseContainer.addListener(new PanelListenerAdapter() {
//            @Override
//            public void onActivate(Panel panel) {
//                activatePortlets();
//                panelManager.setRootNode(rootNode);
//            }
//
//            @Override
//            public void onShow(Component component) {
//                widgetMapPanel.onResize();
//            }
//        });
        WidgetMapRootWidget rootWidget = new WidgetMapRootWidget();
        panelManager = new WidgetMapPanelManager(rootWidget, new WidgetMapper() {
            @Override
            public IsWidget getWidget(TerminalNode terminalNode) {
                GWT.log("getWidget for TerminalNode: " + terminalNode);
                final EntityPortlet entityPortlet = portletMap.get(terminalNode.getNodeId());
                entityPortlet.asWidget().setSize("100%", "100%");
                ViewHolder viewHolder = new ViewHolder(entityPortlet, terminalNode.getNodeProperties());
                viewHolder.addStyleName("drop-zone");
                viewHolder.addCloseHandler(new CloseHandler<Widget>() {
                    @Override
                    public void onClose(CloseEvent<Widget> event) {
                        entityPortlet.dispose();
                    }
                });
                return viewHolder;
            }
        });
        widgetMapPanel = new WidgetMapPanel(rootWidget, panelManager);
        initWidget(widgetMapPanel);

        rootNode = new ParentNode(Direction.HORIZONTAL);

        widgetMapPanel.setRootNode(rootNode);

//        baseContainer.setWidget(widgetMapPanel);
        widgetMapPanel.addRootNodeChangedHandler(new RootNodeChangedHandler() {
            @Override
            public void handleRootNodeChanged(RootNodeChangedEvent rootNodeChangedEvent) {
                GWT.log("[PerspectiveContent] Root node changed");
            }
        });
//        baseContainer.getElement().getStyle().setPosition(Style.Position.ABSOLUTE);
//        baseContainer.getElement().getStyle().setTop(0, Style.Unit.PX);
//        baseContainer.getElement().getStyle().setBottom(0, Style.Unit.PX);
//        baseContainer.getElement().getStyle().setLeft(0, Style.Unit.PX);
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        com.google.gwt.user.client.Timer t = new com.google.gwt.user.client.Timer() {
            @Override
            public void run() {
                widgetMapPanel.onResize();
            }
        };
        t.run();
    }

    private void activatePortlets() {
        for(EntityPortlet entityPortlet : getPortlets()) {
            if(entityPortlet instanceof AbstractEntityPortlet) {
                ((AbstractEntityPortlet) entityPortlet).handleActivated();
            }
        }
    }

    @Override
    public List<EntityPortlet> getPortlets() {
        return new ArrayList<>(entityPortlets);
    }

    private void removePortlet(final EntityPortlet portlet) {
        entityPortlets.remove(portlet);
        portlet.dispose();
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public int getPortletCount(int columnIndex) {
        return getEntityPortlets(columnIndex).size();
    }


    @Override
    public EntityPortlet getPortletAt(int columnIndex, int portletIndex) {
        List<EntityPortlet> portlets = getEntityPortlets(columnIndex);
        EntityPortlet component = portlets.get(portletIndex);
        GWT.log("[AbstractTab] Portlet At (" + columnIndex + ", " + portletIndex + "): " + component);
        return component;
    }

    private List<EntityPortlet> getEntityPortlets(int columnIndex) {
        return Collections.emptyList();
    }

    @Override
    public double getColumnWidth(int columnIndex) {
        return 1;
    }

    @Override
    public void addColumn(double width) {
        ((ParentNode) rootNode).addChild(new ParentNode(Direction.VERTICAL), width);
    }


    @Override
    public void addPortletToColumn(final EntityPortlet entityPortlet, final int columnIndex) {
        ParentNode pn = (ParentNode)((ParentNode) rootNode).getChildAt(columnIndex);
        TerminalNodeId nodeId = new TerminalNodeId();
        portletMap.put(nodeId, entityPortlet);
        pn.addChild(new TerminalNode(nodeId), entityPortlet.getHeight());
        entityPortlets.add(entityPortlet);
    }

    public String getLabel() {
        return getTitle();
    }

    public void setLabel(final String label) {
        setTitle(label);
    }

    public void setClosable(boolean closable) {
    }

    public boolean isVisible() {
        return super.isVisible();
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    public void dropPortlet(EntityPortlet portlet) {
        TerminalNodeId key = new TerminalNodeId();
        portletMap.put(key, portlet);
        widgetMapPanel.doDrop(new TerminalNode(key));
    }

    @Override
    public Optional<Node> getRootNode() {
        return Optional.<Node>of(rootNode);
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    @Override
    public void onResize() {
        GWT.log("[Perspective] onResize");
        LayoutUtil.setBounds(widgetMapPanel, 0, 0, 0, 0);
        widgetMapPanel.onResize();

    }

    @Override
    public void dispose() {
        for(EntityPortlet portlet : entityPortlets) {
            portlet.dispose();
        }
    }


    @Override
    public String toString() {
        return toStringHelper("Perspective")
                .add("perspectiveId", perspectiveId)
                .toString();
    }
}
