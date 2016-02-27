package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.portlet.CouldNotFindPortletWidget;
import edu.stanford.bmir.protege.web.client.portlet.EntityPortlet;
import edu.stanford.bmir.protege.web.client.ui.generated.UIFactory;
import edu.stanford.protege.widgetmap.client.HasFixedPrimaryAxisSize;
import edu.stanford.protege.widgetmap.client.WidgetMapper;
import edu.stanford.protege.widgetmap.client.view.FixedSizeViewHolder;
import edu.stanford.protege.widgetmap.client.view.ViewHolder;
import edu.stanford.protege.widgetmap.shared.node.NodeProperties;
import edu.stanford.protege.widgetmap.shared.node.TerminalNode;
import edu.stanford.protege.widgetmap.shared.node.TerminalNodeId;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
public class PortletWidgetMapper implements WidgetMapper {

    private final Map<TerminalNodeId, ViewHolder> nodeId2ViewHolderMap = new HashMap<>();

    private final UIFactory uiFactory;

    @Inject
    public PortletWidgetMapper(UIFactory uiFactory) {
        this.uiFactory = uiFactory;
    }

    @Override
    public IsWidget getWidget(TerminalNode terminalNode) {
        GWT.log("[PortletWidgetMapper] Getting widget for TerminalNode: " + terminalNode);
        ViewHolder cachedViewHolder = nodeId2ViewHolderMap.get(terminalNode.getNodeId());
        if(cachedViewHolder != null) {
            GWT.log("[PortletWidgetMapper] Using cached view: " + terminalNode);
            return cachedViewHolder;
        }
        String portletClass = terminalNode.getNodeProperties().getPropertyValue("portlet", null);
        GWT.log("[PortletWidgetMapper] Instantiate portlet: " + portletClass);
        ViewHolder viewHolder;
        if(portletClass != null) {
            final EntityPortlet entityPortlet = uiFactory.createPortlet(portletClass);
            if (entityPortlet != null) {
                GWT.log("[PortletWidgetMapper] Created portlet: " + entityPortlet.getClass().getName());
                viewHolder = createViewHolder(terminalNode.getNodeId(), entityPortlet);
            }
            else {
                viewHolder = new ViewHolder(new CouldNotFindPortletWidget(), NodeProperties.emptyNodeProperties());
            }
        }
        else {
            viewHolder = new ViewHolder(new Label("No view class specified"), NodeProperties.emptyNodeProperties());
        }
        nodeId2ViewHolderMap.put(terminalNode.getNodeId(), viewHolder);
        return viewHolder;

    }


    private ViewHolder createViewHolder(final TerminalNodeId nodeId, final EntityPortlet entityPortlet) {
        ViewHolder viewHolder;
        if(entityPortlet instanceof HasFixedPrimaryAxisSize) {
            viewHolder = new FixedSizeViewHolder(entityPortlet.asWidget(), NodeProperties.emptyNodeProperties(), ((HasFixedPrimaryAxisSize) entityPortlet).getFixedPrimaryAxisSize());
        }
        else {
            viewHolder = new ViewHolder(entityPortlet, NodeProperties.emptyNodeProperties());
        }
        entityPortlet.asWidget().setSize("100%", "100%");
        viewHolder.addStyleName("drop-zone");
        viewHolder.addCloseHandler(new CloseHandler<Widget>() {
            @Override
            public void onClose(CloseEvent<Widget> event) {
                entityPortlet.dispose();
                nodeId2ViewHolderMap.remove(nodeId);
            }
        });
        return viewHolder;
    }
}
