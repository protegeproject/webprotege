package edu.stanford.bmir.protege.web.client.entity;

import com.google.common.collect.Sets;
import com.google.gwt.user.client.Timer;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.entity.GetDeprecatedEntitiesAction;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.event.EntityDeprecatedChangedEvent;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16 Jun 2017
 */
@Portlet(
        id = "portlets.deprecatedEntities",
        title = "Deprecated Entities",
        tooltip = "Displays a list of entities that are marked as deprecated"
)
public class DeprecatedEntitiesPresenter extends AbstractWebProtegePortletPresenter {

    private static final int PAGE_SIZE = 50;

    @Nonnull
    private final DeprecatedEntitiesView view;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    private final Timer requestTimer = new Timer() {
        @Override
        public void run() {
            refillView();
        }
    };

    @Inject
    public DeprecatedEntitiesPresenter(@Nonnull SelectionModel selectionModel,
                                       @Nonnull ProjectId projectId,
                                       @Nonnull DeprecatedEntitiesView view,
                                       @Nonnull DispatchServiceManager dispatchServiceManager, DisplayNameRenderer displayNameRenderer) {
        super(selectionModel, projectId, displayNameRenderer);
        this.view = checkNotNull(view);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(view);
        eventBus.addApplicationEventHandler(EntityDeprecatedChangedEvent.ON_ENTITY_DEPRECATED,
                                            this::handleEntityDeprecatedChanged);
        view.setPageNumberChangedHandler(pageNumber -> {
            refillWithDelay();
        });
        view.setSelectionChangedHandler(event -> {
            getSelectionModel().setSelection(event.getSelectedItem().getEntity());
        });
        refillView();
    }

    void refillWithDelay() {
        requestTimer.cancel();
        requestTimer.schedule(500);
    }

    private void refillView() {
        view.setEntities(Collections.emptyList());
        int pageNumber = view.getPageNumber();
        dispatchServiceManager.execute(new GetDeprecatedEntitiesAction(
                                               getProjectId(),
                                               PageRequest.requestPageWithSize(pageNumber, PAGE_SIZE),
                                               Sets.newHashSet(EntityType.values())
                                       ),
                                       this,
                                       result -> {
                                           Page<OWLEntityData> page = result.getDeprecatedEntities();
                                           view.setEntities(page.getPageElements());
                                           view.setPageNumber(page.getPageNumber());
                                           view.setPageCount(page.getPageCount());
                                       });
    }

    private void handleEntityDeprecatedChanged(EntityDeprecatedChangedEvent evt) {
        refillWithDelay();
    }
}
