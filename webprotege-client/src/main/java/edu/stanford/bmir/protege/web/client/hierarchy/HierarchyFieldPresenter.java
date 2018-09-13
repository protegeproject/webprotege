package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Sep 2018
 */
public class HierarchyFieldPresenter {

    @Nonnull
    private final HierarchyFieldView view;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Inject
    public HierarchyFieldPresenter(@Nonnull HierarchyFieldView view,
                                   @Nonnull DispatchServiceManager dispatchServiceManager) {
        this.view = checkNotNull(view);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }
    @Nonnull
    public HierarchyFieldView getView() {
        return view;
    }

    public void start(@Nonnull AcceptsOneWidget viewContainer) {
        viewContainer.setWidget(view);
        view.setEntityChangedHandler(this::handleEntityChanged);
        view.setMoveToParentHandler(this::handleMoveToParent);
        view.setMoveToSiblingHandler(this::handleMoveToSibling);
        view.setMoveToChildHandler(this::handleMoveToChild);
        updateButtonState();
    }

    private void handleEntityChanged() {
        updateButtonState();
    }

    private void updateButtonState() {
        view.setMoveToParentButtonEnabled(false);
        view.setMoveToChildButtonEnabled(false);
        view.setMoveToSiblingButtonEnabled(false);
        view.getEntity().ifPresent(entity -> {
            OWLEntity e = entity.getEntity();
            view.setMoveToParentButtonEnabled(!e.isTopEntity());
            view.setMoveToChildButtonEnabled(!e.isBottomEntity());
            view.setMoveToSiblingButtonEnabled(!e.isTopEntity() && !e.isBottomEntity());
        });
    }


    private void handleMoveToParent() {

    }

    private void handleMoveToSibling() {

    }

    private void handleMoveToChild() {

    }
}
