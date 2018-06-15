package edu.stanford.bmir.protege.web.client.match;

import com.google.gwt.core.client.GWT;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesResult;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.shared.match.GetMatchingEntitiesAction.getMatchingEntities;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
@Portlet(id = "portlet.match", title = "Match", tooltip = "Specifies match criteria")
public class MatchPortletPresenter extends AbstractWebProtegePortletPresenter {

    @Nonnull
    private final EntityCriteriaPresenter presenter;

    @Nonnull
    private final DispatchServiceManager dispatchServiceManager;

    @Nonnull
    private final MatchPortletView view;

    @Inject
    public MatchPortletPresenter(@Nonnull SelectionModel selectionModel,
                                 @Nonnull ProjectId projectId,
                                 @Nonnull EntityCriteriaPresenter presenter,
                                 @Nonnull DispatchServiceManager dispatchServiceManager,
                                 @Nonnull MatchPortletView view) {
        super(selectionModel, projectId);
        this.presenter = checkNotNull(presenter);
        this.dispatchServiceManager = checkNotNull(dispatchServiceManager);
        this.view = checkNotNull(view);
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        view.setExecuteHandler(this::handleExecute);
        portletUi.setWidget(view);
        presenter.start(view.getCriteriaContainer());
    }

    private void displayResult(GetMatchingEntitiesResult result) {
        view.setResults(result.getEntities());
    }

    private void handleExecute() {
        Optional<? extends Criteria> criteria = presenter.getCriteria();
        String s = criteria.map(Object::toString).orElse("Empty");
        GWT.log(s);
        criteria.ifPresent(c -> {
            dispatchServiceManager.execute(getMatchingEntities(getProjectId(), c),
                                           this::displayResult);
        });
    }
}
