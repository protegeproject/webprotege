package edu.stanford.bmir.protege.web.client.perspective;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.*;
import com.google.inject.assistedinject.Assisted;
import edu.stanford.bmir.protege.web.client.ui.LayoutUtil;
import edu.stanford.bmir.protege.web.client.portlet.EntityPortlet;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.protege.widgetmap.client.*;
import edu.stanford.protege.widgetmap.shared.node.*;

import javax.inject.Inject;

import static com.google.common.base.MoreObjects.toStringHelper;


public final class PerspectiveImpl extends Composite implements IsWidget, Perspective {

    private final PerspectiveId perspectiveId;

    private final WidgetMapPanel widgetMapPanel;

    private final WidgetMapPanelManager panelManager;

    private Node rootNode;

    @Inject
    public PerspectiveImpl(@Assisted final PerspectiveId perspectiveId, PortletWidgetMapper widgetMapper) {
        super();
        this.perspectiveId = perspectiveId;
        WidgetMapRootWidget rootWidget = new WidgetMapRootWidget();
        panelManager = new WidgetMapPanelManager(rootWidget, widgetMapper);
        widgetMapPanel = new WidgetMapPanel(rootWidget, panelManager);
        initWidget(widgetMapPanel);

        rootNode = new ParentNode(Direction.ROW);

        widgetMapPanel.setRootNode(rootNode);

        widgetMapPanel.addRootNodeChangedHandler(new RootNodeChangedHandler() {
            @Override
            public void handleRootNodeChanged(RootNodeChangedEvent rootNodeChangedEvent) {
                GWT.log("[PerspectiveContent] Root node changed");
            }
        });
    }

    @Override
    public PerspectiveId getPerspectiveId() {
        return perspectiveId;
    }

    private void removePortlet(final EntityPortlet portlet) {
        portlet.dispose();
    }

    public String getLabel() {
        return getTitle();
    }

    public void setLabel(final String label) {
        setTitle(label);
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
//        portletMap.put(key, createViewHolder(key, portlet));
        widgetMapPanel.doDrop(new TerminalNode(key));
    }

    @Override
    public Optional<Node> getRootNode() {
        return Optional.<Node>of(rootNode);
    }

    public void setRootNode(Node rootNode) {
        GWT.log("[Perspective] Root node set: " + rootNode);
        this.rootNode = rootNode;
        widgetMapPanel.setRootNode(rootNode);
    }

    @Override
    public void onResize() {
        GWT.log("[Perspective] onResize");
        LayoutUtil.setBounds(widgetMapPanel, 0, 0, 0, 0);
        widgetMapPanel.onResize();

    }

    @Override
    public void dispose() {
    }


    @Override
    public String toString() {
        return toStringHelper("Perspective")
                .add("perspectiveId", perspectiveId)
                .toString();
    }
}
