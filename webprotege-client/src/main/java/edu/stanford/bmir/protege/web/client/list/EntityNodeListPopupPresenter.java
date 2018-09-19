package edu.stanford.bmir.protege.web.client.list;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.base.Supplier;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.individualslist.EntityNodeListCellRenderer;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.shared.dispatch.Action;
import edu.stanford.bmir.protege.web.shared.dispatch.Result;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.pagination.PageRequest.requestPageWithSize;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Sep 2018
 */
public class EntityNodeListPopupPresenter {

    private static final int PAGE_SIZE = 200;

    public interface PopupClosedHandler {
        void handlePopupClosed(@Nonnull OWLEntityData sel);
    }

    public interface ListDataSupplier {
        void getListData(@Nonnull PageRequest pageRequest,
                         @Nonnull ListDataConsumer consumer);
    }

    public interface ListDataConsumer {
        void consumeListData(@Nonnull Page<EntityNode> data);
    }

    @Nonnull
    private final EntityNodeListCellRenderer renderer;

    @Nonnull
    private final EntityNodeListPopupView view;

    @Nonnull
    private final ListDataSupplier listDataSupplier;

    @Inject
    @AutoFactory
    public EntityNodeListPopupPresenter(@Nonnull @Provided EntityNodeListCellRenderer renderer,
                                        @Nonnull @Provided  EntityNodeListPopupView view,
                                        @Nonnull ListDataSupplier listDataSupplier) {
        this.renderer = checkNotNull(renderer);
        this.view = checkNotNull(view);
        this.listDataSupplier = checkNotNull(listDataSupplier);
        this.view.setPageNumberChangedHandler(pageNumber -> refill());
    }

    public void showRelativeTo(@Nonnull UIObject uiObject,
                               @Nonnull PopupClosedHandler handler,
                               @Nonnull String title) {
        PopupPanel popupPanel = new PopupPanel(true, true);
        popupPanel.setAutoHideOnHistoryEventsEnabled(true);
        popupPanel.setWidget(view);
        view.setTitleLabel(title);
        view.setAcceptSelectionHandler((sel) -> {
            popupPanel.setVisible(false);
            handler.handlePopupClosed(sel);
        });
        popupPanel.showRelativeTo(uiObject);
        refill();
    }

    private void refill() {
        int pageNumber = view.getPageNumber();
        listDataSupplier.getListData(requestPageWithSize(pageNumber, PAGE_SIZE),
                                     this::setListData);
    }

    private void setListData(Page<EntityNode> data) {
        view.setListData(data.getPageElements());
        view.setPageCount(data.getPageCount());
        view.setPageNumber(data.getPageNumber());
    }

    public void setDisplayLanguage(@Nonnull DisplayNameSettings displayLanguage) {
        renderer.setDisplayLanguage(displayLanguage);
    }

    @Nonnull
    public Optional<EntityNode> getFirstSelectedElement() {
        return view.getFirstSelectedElement();
    }

    @Nonnull
    public List<EntityNode> getSelection() {
        return view.getSelection();
    }
}
