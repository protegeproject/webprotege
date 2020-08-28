package edu.stanford.bmir.protege.web.client.portlet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.action.UIAction;
import edu.stanford.bmir.protege.web.client.app.ForbiddenView;
import edu.stanford.bmir.protege.web.client.app.NothingSelectedView;
import edu.stanford.bmir.protege.web.client.filter.FilterButtonImpl;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.progress.BusyView;
import edu.stanford.bmir.protege.web.client.tooltip.Tooltip;
import edu.stanford.bmir.protege.web.client.tooltip.TooltipOptions;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.filter.FilterSet;
import edu.stanford.bmir.protege.web.shared.filter.FilterSetting;
import edu.stanford.protege.widgetmap.client.view.ViewTitleChangedEvent;
import edu.stanford.protege.widgetmap.client.view.ViewTitleChangedHandler;
import edu.stanford.protege.widgetmap.shared.node.NodeProperties;
import elemental.client.Browser;
import elemental.dom.Element;
import elemental.html.HtmlElement;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;
import java.util.function.BiConsumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10/02/16
 */
public class PortletUiImpl extends Composite implements PortletUi {

    interface PortletUiImplUiBinder extends UiBinder<HTMLPanel, PortletUiImpl> {

    }

    private static PortletUiImplUiBinder ourUiBinder = GWT.create(PortletUiImplUiBinder.class);

    @Nonnull
    private final List<Tooltip> tooltips = new ArrayList<>();

    @UiField
    protected FlowPanel toolbar;

    @UiField
    protected PortletContentHolder contentHolder;

    @UiField
    protected FilterButtonImpl filterButton;

    @UiField
    protected BusyView busyView;

    @UiField
    HTMLPanel toolbarButtonRun;

    @Nonnull
    private final NothingSelectedView nothingSelectedView;

    @Nonnull
    private final ForbiddenView forbiddenView;

    private Optional<IsWidget> content;

    private Optional<FilterView> filterView = Optional.empty();

    private boolean toolbarVisible = false;

    private String viewTitle = "View";

    private String viewSubtitle = "";

    private NodeProperties nodeProperties = NodeProperties.emptyNodeProperties();

    private BiConsumer<PortletUi, NodeProperties> nodePropertiesChangedHandler = (ui, np) -> {};

    @Inject
    public PortletUiImpl(@Nonnull ForbiddenView forbiddenView,
                         @Nonnull NothingSelectedView nothingSelectedView) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.forbiddenView = forbiddenView;
        this.nothingSelectedView = nothingSelectedView;
        setToolbarVisible(false);
        setMenuButtonVisible(false);
        Tooltip filterTooltip = Tooltip.create(filterButton, "Filter");
        tooltips.add(filterTooltip);
    }

    @Override
    public void setTitle(@Nonnull String title) {
        this.viewTitle = checkNotNull(title);
        fireEvent(new ViewTitleChangedEvent(getViewTitle()));
    }

    @Override
    public void setSubtitle(@Nonnull String subtitle) {
        this.viewSubtitle = checkNotNull(subtitle);
        fireEvent(new ViewTitleChangedEvent(getViewTitle()));
    }

    @Override
    public String getViewTitle() {
        return viewTitle + (viewSubtitle.isEmpty() ? "" : ": " + viewSubtitle);
    }

    @Override
    public HandlerRegistration addViewTitleChangedHandler(ViewTitleChangedHandler viewTitleChangedHandler) {
        return addHandler(viewTitleChangedHandler, ViewTitleChangedEvent.getType());
    }

    @Override
    public void setWidget(IsWidget isWidget) {
        content = Optional.of(isWidget);
        contentHolder.setWidget(isWidget);
    }

    @Override
    public void setToolbarVisible(boolean visible) {
        toolbar.setVisible(visible);
        this.toolbarVisible = visible;
    }

    @Override
    public void setBusy(boolean busy) {
        busyView.setVisible(busy);
    }

    @Override
    public void setMenuButtonVisible(boolean visible) {
//        menuButton.setVisible(visible);
    }

//    @UiHandler("menuButton")
//    protected void handleMenuButtonClicked(ClickEvent event) {
//        popupMenu.showRelativeTo(menuButton);
//    }

    @UiHandler("filterButton")
    protected void handleFilterButtonClicked(ClickEvent event) {
        GWT.log("[PortletUi] Filter button clicked");
        if(!filterView.isPresent()) {
            return;
        }
        PopupPanel popup = new PopupPanel();
        popup.setAutoHideEnabled(true);
        popup.setWidget(filterView.get());
        popup.showRelativeTo(filterButton);
        popup.addCloseHandler(closeEvent -> {
            FilterSet filterSet = filterView.get().getFilterSet();
            filterButton.setFiltered(filterSet.containsFilterSetting(FilterSetting.OFF));
            ValueChangeEvent.fire(filterView.get(), filterSet);
        });
    }

    @Override
    public void setFilterView(FilterView filterView) {
        this.filterView = Optional.of(filterView);
        filterButton.setVisible(true);
        filterView.closeCurrentGroup();
    }

    @Override
    public void setFilterApplied(boolean filterApplied) {
        filterButton.setFiltered(filterApplied);
    }

    @Override
    public void addAction(final UIAction action) {
        SimplePanel simplePanel = new SimplePanel();
        simplePanel.addStyleName(WebProtegeClientBundle.BUNDLE.buttons().btnGlyphContainer());
        Tooltip tooltip = Tooltip.createOnBottom(simplePanel, action.getLabel());
        tooltips.add(tooltip);
        final Button button = new Button();
        if(action.hasIcon()) {
            button.addStyleName(WebProtegeClientBundle.BUNDLE.buttons().btnGlyph());
            button.addStyleName(WebProtegeClientBundle.BUNDLE.toolbar().toolbarGlyphButton());
            button.addStyleName(action.getStyle());
            button.setText("");
        }
        else {
            button.addStyleName(WebProtegeClientBundle.BUNDLE.toolbar().toolbarButton());
            button.setText(action.getLabel());
        }
        simplePanel.setWidget(button);
        toolbarButtonRun.add(simplePanel);
        button.addMouseDownHandler(DomEvent::preventDefault);
        button.addClickHandler(event -> action.execute());
        action.setStateChangedHandler(value -> {
            button.setEnabled(value.isEnabled());
            if (!action.hasIcon()) {
                button.setText(value.getLabel());
            }
            else {
                button.setText("");
            }
        });
        setToolbarVisible(true);
    }

    @Override
    public void setForbiddenMessage(String message) {
        if (message.isEmpty()) {
            forbiddenView.clearSubMessage();
        }
        else {
            forbiddenView.setSubMessage(message);
        }
    }

    @Override
    public void setForbiddenVisible(boolean forbiddenVisible) {
        if (forbiddenVisible) {
            toolbar.setVisible(false);
            contentHolder.setWidget(forbiddenView);
        }
        else {
            toolbar.setVisible(toolbarVisible);
            content.ifPresent(widget -> contentHolder.setWidget(widget));
        }
    }

    @Override
    public void setNothingSelectedVisible(boolean nothingSelectedVisible) {
        GWT.log("Set nothing selected: " + nothingSelectedVisible);
        if(nothingSelectedVisible) {
            toolbar.setVisible(false);
            contentHolder.setWidget(nothingSelectedView);
        }
        else {
            toolbar.setVisible(toolbarVisible);
            content.ifPresent(widget -> contentHolder.setWidget(widget));
        }
    }

    @Override
    public void addMenuAction(final PortletAction action) {
    }

    @Nonnull
    @Override
    public NodeProperties getNodeProperties() {
        return nodeProperties;
    }

    @Override
    public void setNodeProperties(@Nonnull NodeProperties nodeProperties) {
        this.nodeProperties = checkNotNull(nodeProperties);
        nodePropertiesChangedHandler.accept(this, this.nodeProperties);
    }

    @Override
    public void setNodeProperty(@Nonnull String propertyName, @Nonnull String propertyValue) {
        String currentValue = nodeProperties.getPropertyValue(propertyName, "");
        if(!Objects.equals(currentValue, propertyValue)) {
            nodeProperties = nodeProperties.toBuilder().setValue(propertyName, propertyValue).build();
            nodePropertiesChangedHandler.accept(this, nodeProperties);
        }
    }

    @Override
    public String getNodeProperty(@Nonnull String propertyName, String defaultValue) {
        return nodeProperties.getPropertyValue(propertyName, defaultValue);
    }

    @Override
    public void setNodePropertiesChangedHandler(BiConsumer<PortletUi, NodeProperties> handler) {
        this.nodePropertiesChangedHandler = handler;
    }

    @Override
    public void dispose() {
        tooltips.forEach(Tooltip::dispose);
    }
}
