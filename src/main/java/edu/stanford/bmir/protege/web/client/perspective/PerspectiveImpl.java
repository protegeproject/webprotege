package edu.stanford.bmir.protege.web.client.perspective;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.assistedinject.Assisted;
import edu.stanford.bmir.protege.web.client.portlet.WebProtegePortlet;
import edu.stanford.bmir.protege.web.client.ui.LayoutUtil;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.protege.widgetmap.client.RootNodeChangedHandler;
import edu.stanford.protege.widgetmap.client.WidgetMapPanel;
import edu.stanford.protege.widgetmap.client.WidgetMapPanelManager;
import edu.stanford.protege.widgetmap.client.WidgetMapRootWidget;
import edu.stanford.protege.widgetmap.shared.node.Node;
import edu.stanford.protege.widgetmap.shared.node.NodeProperties;
import edu.stanford.protege.widgetmap.shared.node.TerminalNode;
import edu.stanford.protege.widgetmap.shared.node.TerminalNodeId;

import javax.inject.Inject;

import static com.google.common.base.MoreObjects.toStringHelper;


public final class PerspectiveImpl extends Composite implements IsWidget, Perspective {

    private final PerspectiveId perspectiveId;

    private final WidgetMapPanel widgetMapPanel;

    private final PortletWidgetMapper widgetMapper;

    private Optional<Node> rootNode;

    @Inject
    public PerspectiveImpl(@Assisted final PerspectiveId perspectiveId, PortletWidgetMapper widgetMapper) {
        super();
        this.perspectiveId = perspectiveId;
        this.widgetMapper = widgetMapper;
        WidgetMapRootWidget rootWidget = new WidgetMapRootWidget();
        WidgetMapPanelManager panelManager = new WidgetMapPanelManager(rootWidget, widgetMapper);
        widgetMapPanel = new WidgetMapPanel(rootWidget, panelManager);
        initWidget(widgetMapPanel);
        rootNode = Optional.absent();
    }

    @Override
    public void setViewsCloseable(boolean closeable) {
        widgetMapper.setViewsCloseable(closeable);
    }

    @Override
    public void setRootNodeChangedHandler(RootNodeChangedHandler handler) {
        widgetMapPanel.addRootNodeChangedHandler(handler);
    }

    @Override
    public void setEmptyPerspectiveWidget(IsWidget widget) {
        widgetMapPanel.setEmptyWidget(widget);
    }

    @Override
    public PerspectiveId getPerspectiveId() {
        return perspectiveId;
    }

    private void removePortlet(final WebProtegePortlet portlet) {
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

    public void dropView(String className) {
        NodeProperties nodeProperties = NodeProperties.builder().setValue("portlet", className).build();
        TerminalNode terminalNode = new TerminalNode(new TerminalNodeId(), nodeProperties);
        widgetMapPanel.doDrop(terminalNode);
    }

    @Override
    public Optional<Node> getRootNode() {
        return widgetMapPanel.getRootNode();
    }

    @Override
    public void setRootNode(Optional<Node> rootNode) {
        GWT.log("[Perspective] Root node set: " + rootNode);
        this.rootNode = rootNode;
        if (rootNode.isPresent()) {
            widgetMapPanel.setRootNode(rootNode.get());
        }
        else {
            // TODO:
        }
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
