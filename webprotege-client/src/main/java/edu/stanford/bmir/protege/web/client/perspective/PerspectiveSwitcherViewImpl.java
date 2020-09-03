package edu.stanford.bmir.protege.web.client.perspective;


import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TabBar;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.action.AbstractUiAction;
import edu.stanford.bmir.protege.web.client.form.LanguageMapCurrentLocaleMapper;
import edu.stanford.bmir.protege.web.client.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDescriptor;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 23/06/2014
 */
public class PerspectiveSwitcherViewImpl extends Composite implements PerspectiveSwitcherView {

    private Set<PerspectiveId> resettablePerspectives = new HashSet<>();

    interface PerspectiveSwitcherViewImplUiBinder extends UiBinder<HTMLPanel, PerspectiveSwitcherViewImpl> {

    }

    private static PerspectiveSwitcherViewImplUiBinder ourUiBinder = GWT.create(PerspectiveSwitcherViewImplUiBinder.class);

    @UiField
    protected TabBar tabBar;

    @UiField
    protected Button newTabButton;


    private Optional<PerspectiveId> highlightedPerspective = Optional.empty();

    private final List<PerspectiveId> displayedPerspectives = Lists.newArrayList();

    private final PerspectiveLinkFactory linkFactory;

    private final List<PerspectiveDescriptor> bookmarkedPerspectives = new ArrayList<>();


    private PerspectiveActivatedHandler linkActivatedHandler = perspectiveId -> {};

    private AddBlankPerspectiveHandler addBlankPerspectiveHandler = () -> {};

    private AddToFavoritePerspectivesHandler addBookMarkedPerspectiveLinkHandler = perspectiveId -> {};

    private RemoveFromFavoritePerspectivesHandler removeFromFavoritePerspectivesHandler = perspectiveId -> {};

    private ResetPerspectiveToDefaultStateHandler resetPerspectiveToDefaultStateHandler = perspectiveId -> {};

    private ManagerPerspectivesHandler managePerspectivesHandler = () -> {};

    private AddViewHandler addViewHandler = perspectiveId -> {};

    private boolean addPerspectiveAllowed = true;

    private boolean closePerspectiveAllowed = true;

    private boolean addViewAllowed = true;

    private boolean managePerspectivesAllowed = false;

    private static Messages messages = GWT.create(Messages.class);

    private final LanguageMapCurrentLocaleMapper localeMapper;

    @Inject
    public PerspectiveSwitcherViewImpl(PerspectiveLinkFactory linkFactory, LanguageMapCurrentLocaleMapper localeMapper) {
        this.linkFactory = linkFactory;
        this.localeMapper = localeMapper;
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @UiHandler("tabBar")
    protected void handlePerspectiveLinkClicked(BeforeSelectionEvent<Integer> event) {
        /*
          Veto the selection if it does not correspond to the highlighted link
         */
        PerspectiveId link = displayedPerspectives.get(event.getItem());
        if (!highlightedPerspective.equals(Optional.of(link))) {
            event.cancel();
        }
    }

    @UiHandler("newTabButton")
    protected void handleNewPerspectiveButtonClicked(ClickEvent clickEvent) {
        if(!addPerspectiveAllowed) {
            return;
        }
        PopupMenu popupMenu = new PopupMenu();
        for (final PerspectiveDescriptor perspectiveDescriptor : bookmarkedPerspectives) {
            AbstractUiAction action = new AbstractUiAction(localeMapper.getValueForCurrentLocale(perspectiveDescriptor.getLabel())) {
                @Override
                public void execute() {
                    addBookMarkedPerspectiveLinkHandler.handleAddToFavorites(perspectiveDescriptor);
                }
            };
            action.setEnabled(!displayedPerspectives.contains(perspectiveDescriptor.getPerspectiveId()));
            popupMenu.addItem(action);
        }
        popupMenu.addSeparator();
        popupMenu.addItem(messages.perspective_addBlankTab() + "\u2026",
                          () -> addBlankPerspectiveHandler.handleAddBlankPerspective());
        if(managePerspectivesAllowed) {
            popupMenu.addSeparator();
            popupMenu.addItem(messages.perspective_manage(),
                              () -> managePerspectivesHandler.handleManagePerspectives());
        }

        popupMenu.showRelativeTo(newTabButton);

    }

    public void setFavourites(List<PerspectiveDescriptor> perspectives) {
        removeAllDisplayedPerspectives();
        for (final PerspectiveDescriptor perspectiveDescriptor : perspectives) {
            addFavorite(perspectiveDescriptor);
        }
        ensureHighlightedPerspectiveLinkIsSelected();
    }

    @Override
    public void addFavorite(final PerspectiveDescriptor perspectiveDescriptor) {
        PerspectiveId perspectiveId = perspectiveDescriptor.getPerspectiveId();
        this.displayedPerspectives.add(perspectiveDescriptor.getPerspectiveId());
        PerspectiveLink linkWidget = linkFactory.createPerspectiveLink(perspectiveId);
        linkWidget.setLabel(localeMapper.getValueForCurrentLocale(perspectiveDescriptor.getLabel()));
        linkWidget.addClickHandler(event -> {
            GWT.log("[PerspectiveSwitcherViewImpl] link clicked");
            highlightedPerspective = Optional.of(perspectiveId);
            linkActivatedHandler.handlePerspectiveActivated(perspectiveId);
        });
        if (addViewAllowed) {
            linkWidget.addActionHandler(messages.perspective_addView(), () -> {
                if (addViewAllowed) {
                    addViewHandler.handleAddViewToPerspective(perspectiveId);
                }
            });
        }
        if(resettablePerspectives.contains(perspectiveId)) {
            linkWidget.addActionHandler(messages.perspective_reset(),
                                        () -> resetPerspectiveToDefaultStateHandler.handleResetPerspectiveToDefaultState(perspectiveDescriptor));
        }
        if (closePerspectiveAllowed) {
            linkWidget.addActionHandler(messages.perspective_close(),
                                        () -> {
                                            if (closePerspectiveAllowed) {
                                                removeFromFavoritePerspectivesHandler.handleRemoveFromFavorites(perspectiveId);
                                            }
                                        });
        }
        tabBar.addTab(linkWidget.asWidget());
    }

    @Override
    public void removeFavorite(PerspectiveDescriptor perspectiveDescriptor) {
        PerspectiveId perspectiveId = perspectiveDescriptor.getPerspectiveId();
        int index = displayedPerspectives.indexOf(perspectiveId);
        if (index == -1) {
            return;
        }
        displayedPerspectives.remove(perspectiveId);
        tabBar.removeTab(index);
    }

    private void removeAllDisplayedPerspectives() {
        while (tabBar.getTabCount() > 0) {
            tabBar.removeTab(0);
        }
        this.displayedPerspectives.clear();
    }

    @Override
    public void setAddToFavoritePerspectivesHandler(AddToFavoritePerspectivesHandler handler) {
        this.addBookMarkedPerspectiveLinkHandler = handler;
    }

    @Override
    public void setAvailablePerspectives(List<PerspectiveDescriptor> perspectives) {
        this.bookmarkedPerspectives.clear();
        this.bookmarkedPerspectives.addAll(perspectives);
    }

    public void setPerspectiveActivatedHandler(PerspectiveActivatedHandler handler) {
        linkActivatedHandler = checkNotNull(handler);
    }

    public void setAddBlankPerspectiveHandler(AddBlankPerspectiveHandler handler) {
        addBlankPerspectiveHandler = checkNotNull(handler);
    }

    public void setRemoveFromFavoritePerspectivesHandler(RemoveFromFavoritePerspectivesHandler handler) {
        removeFromFavoritePerspectivesHandler = checkNotNull(handler);
    }

    @Override
    public void setResetPerspectiveToDefaultStateHandler(ResetPerspectiveToDefaultStateHandler handler) {
        resetPerspectiveToDefaultStateHandler = checkNotNull(handler);
    }

    @Override
    public void setManagePerspectivesHandler(
            ManagerPerspectivesHandler handler) {
        this.managePerspectivesHandler = checkNotNull(handler);
    }

    @Override
    public void setAddViewHandler(AddViewHandler handler) {
        addViewHandler = checkNotNull(handler);
    }

    @Override
    public void setResettablePerspectives(Set<PerspectiveId> resettablePerspectives) {
        this.resettablePerspectives.clear();
        this.resettablePerspectives.addAll(resettablePerspectives);
    }

    public void setHighlightedPerspective(PerspectiveId perspectiveId) {
        checkNotNull(perspectiveId);
        highlightedPerspective = Optional.of(perspectiveId);
        ensureHighlightedPerspectiveLinkIsSelected();
    }

    private void ensureHighlightedPerspectiveLinkIsSelected() {
        if (!highlightedPerspective.isPresent()) {
            return;
        }
        for (int i = 0; i < displayedPerspectives.size(); i++) {
            if (displayedPerspectives.get(i).equals(highlightedPerspective.get())) {
                if (tabBar.getSelectedTab() != i) {
                    tabBar.selectTab(i);
                }
                break;
            }
        }
    }

    @Override
    public void setAddPerspectiveAllowed(boolean addPerspectiveAllowed) {
        newTabButton.setVisible(addPerspectiveAllowed);
        this.addPerspectiveAllowed = addPerspectiveAllowed;
    }

    @Override
    public void setClosePerspectiveAllowed(boolean closePerspectiveAllowed) {
        this.closePerspectiveAllowed = closePerspectiveAllowed;
    }

    @Override
    public void setAddViewAllowed(boolean addViewAllowed) {
        this.addViewAllowed = addViewAllowed;
    }

    @Override
    public void setManagePerspectivesAllowed(boolean managePerspectivesAllowed) {
        this.managePerspectivesAllowed = managePerspectivesAllowed;
    }
}
