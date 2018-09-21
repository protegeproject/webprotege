package edu.stanford.bmir.protege.web.client.hierarchy;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import edu.stanford.bmir.protege.web.shared.entity.EntityNode;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.hierarchy.HierarchyId;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Sep 2018
 */
public class HierarchyPopupPresenter {


    @Nonnull
    private final HierarchyId hierarchyId;

    @Nonnull
    private final HierarchyPopupView view;

    @Nonnull
    private final EntityHierarchyModel model;

    private PopupPanel popupPanel;

    private Optional<OWLEntity> selectedEntity = Optional.empty();

    @Inject
    @AutoFactory
    public HierarchyPopupPresenter(@Nonnull HierarchyId hierarchyId,
                                   @Provided @Nonnull HierarchyPopupView view,
                                   @Provided @Nonnull EntityHierarchyModel model) {
        this.hierarchyId = checkNotNull(hierarchyId);
        this.view = view;
        this.model = checkNotNull(model);
        popupPanel = new PopupPanel(true, true);
        popupPanel.setAutoHideEnabled(true);
        popupPanel.setAutoHideOnHistoryEventsEnabled(true);
    }

    public void start(@Nonnull WebProtegeEventBus eventBus) {
        model.start(eventBus,
                    hierarchyId);
        view.setModel(model);
    }

    public void show(@Nonnull UIObject target, Consumer<EntityNode> popupClosedHandler) {
        popupPanel.setWidget(view);
        popupPanel.showRelativeTo(target);
        view.setSelectionChangedHandler(sel -> {
            if(!Optional.of(sel.getEntity()).equals(selectedEntity)) {
                popupPanel.hide();
                selectedEntity = Optional.of(sel.getEntity());
                popupClosedHandler.accept(sel);
            }
        });
    }

    public void setSelectedEntity(@Nonnull OWLEntity selectedEntity) {
        this.selectedEntity = Optional.of(selectedEntity);
        view.revealEntity(selectedEntity);
    }

    public void setDisplayNameSettings(@Nonnull DisplayNameSettings settings) {
        view.setDisplayNameSettings(settings);
    }
}
