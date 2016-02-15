package edu.stanford.bmir.protege.web.client.perspective;


import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 23/06/2014
 */
public class PerspectiveSwitcherViewImpl extends Composite implements PerspectiveSwitcherView {

    interface PerspectiveSwitcherViewImplUiBinder extends UiBinder<HTMLPanel, PerspectiveSwitcherViewImpl> {
    }

    private static PerspectiveSwitcherViewImplUiBinder ourUiBinder = GWT.create(PerspectiveSwitcherViewImplUiBinder.class);

    @UiField
    protected TabBar tabBar;

    @UiField
    protected HasClickHandlers newTabButton;


    private Optional<PerspectiveId> highlightedPerspective = Optional.absent();

    private final List<PerspectiveId> displayedPerspectives = Lists.newArrayList();

    private final PerspectiveLinkFactory linkFactory;


    private PerspectiveLinkActivatedHandler linkActivatedHandler = new PerspectiveLinkActivatedHandler() {
        public void handlePerspectiveLinkActivated(PerspectiveId perspectiveId) {
        }
    };

    private AddPerspectiveLinkRequestHandler addPerspectiveLinkRequestHandler = new AddPerspectiveLinkRequestHandler() {
        public void handleAddNewPerspectiveLinkRequest() {
        }
    };

    private RemovePerspectiveLinkRequestHandler removePerspectiveLinkRequestHandler = new RemovePerspectiveLinkRequestHandler() {
        public void handleRemovePerspectiveLinkRequest(PerspectiveId perspectiveId) {

        }
    };

    @Inject
    public PerspectiveSwitcherViewImpl(PerspectiveLinkFactory linkFactory) {
        this.linkFactory = linkFactory;
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @UiHandler("tabBar")
    protected void handlePerspectiveLinkClicked(BeforeSelectionEvent<Integer> event) {
        /**
         * Veto the selection if it does not correspond to the highlighted link
         */
        PerspectiveId link = displayedPerspectives.get(event.getItem());
        if(!highlightedPerspective.equals(Optional.<PerspectiveId>of(link))) {
            event.cancel();
        }
    }

    @UiHandler("newTabButton")
    protected void handleNewPerspectiveButtonClicked(ClickEvent clickEvent) {
        addPerspectiveLinkRequestHandler.handleAddNewPerspectiveLinkRequest();
    }

    public void setPerspectiveLinks(List<PerspectiveId> perspectives) {
        removeAllDisplayedPerspectives();
        for(final PerspectiveId perspectiveId : perspectives) {
            addPerspectiveLink(perspectiveId);
        }
        ensureHighlightedPerspectiveLinkIsSelected();
    }

    @Override
    public void addPerspectiveLink(final PerspectiveId perspectiveId) {
        this.displayedPerspectives.add(perspectiveId);
        PerspectiveLink linkWidget = linkFactory.createPerspectiveLink(perspectiveId);
        linkWidget.setLabel(perspectiveId.getId());
        linkWidget.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                GWT.log("[PerspectiveSwitcherViewImpl] link clicked");
                highlightedPerspective = Optional.of(perspectiveId);
                linkActivatedHandler.handlePerspectiveLinkActivated(perspectiveId);
            }
        });
        linkWidget.addActionHandler("Close", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                removePerspectiveLinkRequestHandler.handleRemovePerspectiveLinkRequest(perspectiveId);
            }
        });
        tabBar.addTab(linkWidget.asWidget());
    }

    @Override
    public void removePerspectiveLink(PerspectiveId perspectiveId) {
        int index = displayedPerspectives.indexOf(perspectiveId);
        if(index == -1) {
            return;
        }
        displayedPerspectives.remove(perspectiveId);
        tabBar.removeTab(index);
    }

    private void removeAllDisplayedPerspectives() {
        while(tabBar.getTabCount() > 0) {
            tabBar.removeTab(0);
        }
        this.displayedPerspectives.clear();
    }

    public List<PerspectiveId> getPerspectiveLinks() {
        return Lists.newArrayList(displayedPerspectives);
    }

    public void setPerspectiveLinkActivatedHandler(PerspectiveLinkActivatedHandler handler) {
        linkActivatedHandler = checkNotNull(handler);
    }

    public void setAddPerspectiveLinkRequestHandler(AddPerspectiveLinkRequestHandler handler) {
        addPerspectiveLinkRequestHandler = checkNotNull(handler);
    }

    public void setRemovePerspectiveLinkHandler(RemovePerspectiveLinkRequestHandler handler) {
        removePerspectiveLinkRequestHandler = checkNotNull(handler);
    }

    public void setHighlightedPerspective(PerspectiveId perspectiveId) {
        checkNotNull(perspectiveId);
        highlightedPerspective = Optional.of(perspectiveId);
        ensureHighlightedPerspectiveLinkIsSelected();
    }

    public Optional<PerspectiveId> getSelectedPerspective() {
        return highlightedPerspective;
    }

    private void ensureHighlightedPerspectiveLinkIsSelected() {
        if(!highlightedPerspective.isPresent()) {
            return;
        }
        for(int i = 0; i < displayedPerspectives.size(); i++) {
            if(displayedPerspectives.get(i).equals(highlightedPerspective.get())) {
                if (tabBar.getSelectedTab() != i) {
                    tabBar.selectTab(i);
                }
                break;
            }
        }
    }

}
