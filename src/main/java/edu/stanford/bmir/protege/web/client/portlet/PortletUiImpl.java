package edu.stanford.bmir.protege.web.client.portlet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.app.ForbiddenView;
import edu.stanford.bmir.protege.web.client.filter.FilterButtonImpl;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.filter.FilterSet;
import edu.stanford.bmir.protege.web.shared.filter.FilterSetting;
import edu.stanford.protege.widgetmap.client.view.ViewTitleChangedEvent;
import edu.stanford.protege.widgetmap.client.view.ViewTitleChangedHandler;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

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


    @UiField
    protected FlowPanel toolbar;

    @UiField
    protected PortletContentHolder contentHolder;

    @UiField
    protected FilterButtonImpl filterButton;

    @Nonnull
    private final ForbiddenView forbiddenView;

    private Optional<IsWidget> content;

    private Optional<FilterView> filterView = Optional.empty();

    private boolean toolbarVisible = false;

    private String viewTitle = "View";

    @Inject
    public PortletUiImpl(@Nonnull ForbiddenView forbiddenView) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.forbiddenView = forbiddenView;
        setToolbarVisible(false);
        setMenuButtonVisible(false);
    }

    @Override
    public void setViewTitle(String title) {
        this.viewTitle = checkNotNull(title);
    }

    @Override
    public String getViewTitle() {
        return viewTitle;
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
    public void addPortletAction(final PortletAction action) {
        final Button button = new Button(action.getName());
        button.addStyleName(WebProtegeClientBundle.BUNDLE.buttons().toolbarButton());
        toolbar.add(button);
        button.addClickHandler(event -> action.getActionHandler().handleActionInvoked(action, event));
        action.setStateChangedHandler(value -> {
            button.setEnabled(value.isEnabled());
            button.setText(value.getName());
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
            setToolbarVisible(false);
            contentHolder.setWidget(forbiddenView);
        }
        else {
            setToolbarVisible(toolbarVisible);
            content.ifPresent(widget -> contentHolder.setWidget(widget));
        }
    }

    @Override
    public void addMenuAction(final PortletAction action) {
    }
}