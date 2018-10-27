package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesResult;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettingsChangedEvent;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesAction.getMatchingEntities;
import static edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettingsChangedEvent.ON_PROJECT_SETTINGS_CHANGED;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
@Portlet(id = "portlet.query", title = "Query", tooltip = "Allows asserted information to be queried")
public class QueryPortletPresenter extends AbstractWebProtegePortletPresenter {

    private static final int PAGE_SIZE = 200;

    @Nonnull
    private final EntityCriteriaPresenter presenter;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final QueryPortletView view;

    @Inject
    public QueryPortletPresenter(@Nonnull SelectionModel selectionModel,
                                 @Nonnull ProjectId projectId,
                                 @Nonnull EntityCriteriaPresenter presenter,
                                 @Nonnull DispatchServiceManager dispatchServiceManager,
                                 @Nonnull QueryPortletView view, DisplayNameRenderer displayNameRenderer) {
        super(selectionModel, projectId, displayNameRenderer);
        this.presenter = checkNotNull(presenter);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.view = checkNotNull(view);
        this.view.setPageNumberChangedHandler(pageNumber -> handleExecute());
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        view.setExecuteHandler(this::handleExecute);
        portletUi.setWidget(view);
        presenter.start(view.getCriteriaContainer());
        eventBus.addApplicationEventHandler(ON_PROJECT_SETTINGS_CHANGED, this::handleProjectSettingsChanged);
    }

    private void handleProjectSettingsChanged(@Nonnull ProjectSettingsChangedEvent event) {
        handleExecute();
    }

    private void displayResult(GetMatchingEntitiesResult result) {
        view.setExecuteEnabled(true);
        view.setResult(result.getEntities());
        view.setPageCount(result.getEntities().getPageCount());
        view.setPageNumber(result.getEntities().getPageNumber());
    }

    private void handleExecute() {
        Optional<? extends Criteria> criteria = presenter.getCriteria();
        criteria.ifPresent(c -> {
            PageRequest pageRequest = PageRequest.requestPageWithSize(view.getPageNumber(), PAGE_SIZE);
            view.setExecuteEnabled(false);
            dispatchServiceManager.execute(getMatchingEntities(getProjectId(), c, pageRequest),
                                           this::displayResult);
        });
    }
}
