package edu.stanford.bmir.protege.web.client.perspective;


import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import edu.stanford.bmir.protege.web.client.Messages;
import edu.stanford.bmir.protege.web.client.form.LanguageMapCurrentLocaleMapper;
import edu.stanford.bmir.protege.web.client.library.popupmenu.PopupMenu;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveDescriptor;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle.BUNDLE;


/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 23/06/2014
 */
public class PerspectiveSwitcherViewImpl extends Composite implements PerspectiveSwitcherView {

    private Set<PerspectiveId> resettablePerspectives = new HashSet<>();

    interface PerspectiveSwitcherViewImplUiBinder extends UiBinder<HTMLPanel, PerspectiveSwitcherViewImpl> {

    }

    private static PerspectiveSwitcherViewImplUiBinder ourUiBinder = GWT.create(PerspectiveSwitcherViewImplUiBinder.class);

    @UiField
    protected VerticalPanel sideBar;

    @UiField
    protected Button settingButton;

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

    private boolean addPerspectiveAllowed = false;

    private boolean closePerspectiveAllowed = false;

    private boolean addViewAllowed = false;

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

//    @UiHandler("tabBar")
//    protected void handlePerspectiveLinkClicked(BeforeSelectionEvent<Integer> event) {
//        /*
//          Veto the selection if it does not correspond to the highlighted link
//         */
//        PerspectiveId link = displayedPerspectives.get(event.getItem());
//        if (!highlightedPerspective.equals(Optional.of(link))) {
//            event.cancel();
//        }
//    }

    @UiHandler("settingButton")
    protected void handleNewPerspectiveButtonClicked(ClickEvent clickEvent) {
        PopupMenu popupMenu = new PopupMenu();
        popupMenu.addItem("Display",
                () -> addBlankPerspectiveHandler.handleAddBlankPerspective());
        popupMenu.addItem("Project",
                () -> addBlankPerspectiveHandler.handleAddBlankPerspective());
        popupMenu.showRelativeTo(settingButton);

    }

    public void setFavourites(List<PerspectiveDescriptor> perspectives) {
        removeAllDisplayedPerspectives();
        String additionalStyles = "";
        for (final PerspectiveDescriptor perspectiveDescriptor : perspectives) {
            String label = localeMapper.getValueForCurrentLocale(perspectiveDescriptor.getLabel());
            switch (label) {
                case "Classes":
                    additionalStyles = BUNDLE.style().sideBarClassesIcon();
                    break;
                case "Individuals":
                    additionalStyles = BUNDLE.style().sideBarIndividualIcon();
                    break;
                case "Properties":
                    additionalStyles = BUNDLE.style().sideBarPropertiesIcon();
                    break;
            }
            addFavorite(sideBar, perspectiveDescriptor, additionalStyles);
        ensureHighlightedPerspectiveLinkIsSelected();
        }
    }

    @Override
    public void addFavorite(Panel panel, final PerspectiveDescriptor perspectiveDescriptor, @Nonnull String additionalStyles) {
        PerspectiveId perspectiveId = perspectiveDescriptor.getPerspectiveId();
        String label = localeMapper.getValueForCurrentLocale(perspectiveDescriptor.getLabel());
        this.displayedPerspectives.add(perspectiveDescriptor.getPerspectiveId());
        PerspectiveLink linkWidget = linkFactory.createPerspectiveLink(perspectiveId);
        linkWidget.setLabel(label);
        linkWidget.setMenuButtonVisible(false);
        linkWidget.setLabelVisible(false);
        linkWidget.addClickHandler(event -> {
            GWT.log("[PerspectiveSwitcherViewImpl] link clicked");
            highlightedPerspective = Optional.of(perspectiveId);
            linkActivatedHandler.handlePerspectiveActivated(perspectiveId);
        });
        if (!additionalStyles.isEmpty()) {
            linkWidget.setStyle(additionalStyles);
        }
        if (addViewAllowed) {
            linkWidget.addActionHandler(messages.perspective_addView(), () -> {
                if (addViewAllowed) {
                    addViewHandler.handleAddViewToPerspective(perspectiveId);
                }
            });
        }
        if (resettablePerspectives.contains(perspectiveId)) {
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
        panel.add(linkWidget.asWidget());
    }

    @Override
    public void removeFavorite(PerspectiveDescriptor perspectiveDescriptor) {
        PerspectiveId perspectiveId = perspectiveDescriptor.getPerspectiveId();
        int index = displayedPerspectives.indexOf(perspectiveId);
        if (index == -1) {
            return;
        }
        displayedPerspectives.remove(perspectiveId);
//        tabBar.removeTab(index);
    }

    private void removeAllDisplayedPerspectives() {
        while (sideBar.getWidgetCount() > 0) {
            sideBar.remove(0);
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
//                if (tabBar.getSelectedTab() != i) {
//                    tabBar.selectTab(i);
//                }
                break;
            }
        }
    }

    @Override
    public void setAddPerspectiveAllowed(boolean addPerspectiveAllowed) {
//        newTabButton.setVisible(false);
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
