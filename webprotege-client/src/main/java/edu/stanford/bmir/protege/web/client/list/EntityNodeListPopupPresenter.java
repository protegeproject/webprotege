package edu.stanford.bmir.protege.web.client.list;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import edu.stanford.bmir.protege.web.client.individualslist.EntityNodeListCellRenderer;
import edu.stanford.bmir.protege.web.client.pagination.HasPagination;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Sep 2018
 */
public class EntityNodeListPopupPresenter {

    public interface PopupClosedHandler {
        void handlePopupClosed(@Nonnull OWLEntityData sel);
    }

    @Nonnull
    private final EntityNodeListCellRenderer renderer;

    @Nonnull
    private final EntityNodeListPopupView view;

    @Inject
    public EntityNodeListPopupPresenter(@Nonnull EntityNodeListCellRenderer renderer, @Nonnull EntityNodeListPopupView view) {
        this.renderer = checkNotNull(renderer);
        this.view = view;
        this.view.setPageNumberChangedHandler(pageNumber -> {

        });
    }

    public void showRelativeTo(@Nonnull UIObject uiObject, @Nonnull PopupClosedHandler handler) {
        PopupPanel popupPanel = new PopupPanel(true, true);
        popupPanel.setAutoHideOnHistoryEventsEnabled(true);
        popupPanel.setWidget(view);
        view.setAcceptSelectionHandler((sel) -> {
            popupPanel.setVisible(false);
            handler.handlePopupClosed(sel);
        });
        popupPanel.showRelativeTo(uiObject);
    }

    public void setListData(Page<EntityNode> data) {
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
