package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import edu.stanford.bmir.protege.web.client.portlet.*;
import edu.stanford.bmir.protege.web.shared.PortletId;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.protege.widgetmap.client.HasFixedPrimaryAxisSize;
import edu.stanford.protege.widgetmap.client.WidgetMapper;
import edu.stanford.protege.widgetmap.client.view.FixedSizeViewHolder;
import edu.stanford.protege.widgetmap.client.view.ViewHolder;
import edu.stanford.protege.widgetmap.shared.node.NodeProperties;
import edu.stanford.protege.widgetmap.shared.node.TerminalNode;
import edu.stanford.protege.widgetmap.shared.node.TerminalNodeId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17/02/16
 */
public class PortletWidgetMapper implements WidgetMapper {

    private static final String DROP_ZONE = "drop-zone";

    private final Map<TerminalNodeId, ViewHolder> nodeId2ViewHolderMap = new HashMap<>();

    private final PortletFactory portletFactory;

    private boolean viewsCloseable = true;

    private final Provider<PortletUi> portletUiProvider;

    private final Provider<WebProtegeEventBus> eventBusProvider;

    @Inject
    public PortletWidgetMapper(@Nonnull PortletFactory portletFactory,
                               Provider<PortletUi> portletUiProvider,
                               Provider<WebProtegeEventBus> eventBusProvider) {
        this.portletFactory = portletFactory;
        this.portletUiProvider = portletUiProvider;
        this.eventBusProvider = eventBusProvider;
    }


    public boolean isViewsCloseable() {
        return viewsCloseable;
    }

    public void setViewsCloseable(boolean viewsCloseable) {
        this.viewsCloseable = viewsCloseable;
    }

    @Override
    public IsWidget getWidget(TerminalNode terminalNode) {
        GWT.log("[PortletWidgetMapper] Getting widget for TerminalNode: " + terminalNode);
        ViewHolder cachedViewHolder = nodeId2ViewHolderMap.get(terminalNode.getNodeId());
        if (cachedViewHolder != null) {
            GWT.log("[PortletWidgetMapper] Using cached view: " + terminalNode);
            return cachedViewHolder;
        }
        String portletClass = terminalNode.getNodeProperties().getPropertyValue("portlet", null);
        GWT.log("[PortletWidgetMapper] Instantiate portlet: " + portletClass);
        ViewHolder viewHolder;
        if (portletClass != null) {
            Optional<WebProtegePortlet> thePortlet = portletFactory.createPortlet(new PortletId(portletClass));
            if (thePortlet.isPresent()) {
                GWT.log("[PortletWidgetMapper] Created portlet from auto-generated factory");
                viewHolder = createViewHolder(terminalNode.getNodeId(),
                                              thePortlet.get(),
                                              terminalNode.getNodeProperties());
            }
            else {
                CouldNotFindPortletWidget childWidget = new CouldNotFindPortletWidget();
                childWidget.setPortletId(portletClass);
                viewHolder = new ViewHolder(childWidget, NodeProperties.emptyNodeProperties());
            }

        }
        else {
            viewHolder = new ViewHolder(new Label("No view class specified"), NodeProperties.emptyNodeProperties());
        }
        viewHolder.setCloseable(viewsCloseable);
        nodeId2ViewHolderMap.put(terminalNode.getNodeId(), viewHolder);
        return viewHolder;

    }


    private ViewHolder createViewHolder(@Nonnull TerminalNodeId nodeId,
                                        @Nonnull WebProtegePortlet portlet,
                                        @Nonnull NodeProperties nodeProperties) {
        PortletUi portletUi = portletUiProvider.get();
        WebProtegeEventBus eventBus = eventBusProvider.get();
        portletUi.setViewTitle(portlet.getPortletDescriptor().getTitle());
        WebProtegePortletPresenter portletPresenter = portlet.getPresenter();
        portletPresenter.start(portletUi, eventBus);
        ViewHolder viewHolder;
        if (portletPresenter instanceof HasFixedPrimaryAxisSize) {
            viewHolder = new FixedSizeViewHolder(portletUi.asWidget(), NodeProperties.emptyNodeProperties(), ((HasFixedPrimaryAxisSize) portletPresenter).getFixedPrimaryAxisSize());
        }
        else {
            viewHolder = new ViewHolder(portletUi, NodeProperties.emptyNodeProperties());
        }
        viewHolder.addStyleName(DROP_ZONE);
        viewHolder.addCloseHandler(event -> {
            eventBus.dispose();
            portletPresenter.dispose();
            nodeId2ViewHolderMap.remove(nodeId);
        });
        return viewHolder;
    }
}
