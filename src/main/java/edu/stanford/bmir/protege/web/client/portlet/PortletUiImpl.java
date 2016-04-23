package edu.stanford.bmir.protege.web.client.portlet;

import com.google.common.base.Optional;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.filter.FilterButtonImpl;
import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.ui.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.ui.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;
import edu.stanford.bmir.protege.web.shared.StateChangedHandler;
import edu.stanford.bmir.protege.web.shared.filter.Filter;
import edu.stanford.bmir.protege.web.shared.filter.FilterId;
import edu.stanford.bmir.protege.web.shared.filter.FilterSet;
import edu.stanford.bmir.protege.web.shared.filter.FilterSetting;

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
    FilterButtonImpl filterButton;

    private Optional<FilterView> filterView = Optional.absent();

    public PortletUiImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        setToolbarVisible(false);
        setMenuButtonVisible(false);
    }

    @Override
    public void setWidget(IsWidget isWidget) {
        contentHolder.setWidget(isWidget);
    }

    @Override
    public void setToolbarVisible(boolean visible) {
        toolbar.setVisible(visible);
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
        popup.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                FilterSet filterSet = filterView.get().getFilterSet();
                filterButton.setFiltered(filterSet.containsFilterSetting(FilterSetting.OFF));
                ValueChangeEvent.fire(filterView.get(), filterSet);
            }
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
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                action.getActionHandler().handleActionInvoked(action, event);
            }
        });
        action.setStateChangedHandler(new StateChangedHandler<PortletAction>() {
            @Override
            public void handleStateChanged(PortletAction value) {
                button.setEnabled(value.isEnabled());
                button.setText(value.getName());
            }
        });
    }

    @Override
    public void addMenuAction(final PortletAction action) {
    }
}