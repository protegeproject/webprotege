package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12 Jun 2018
 */
@Portlet(id = "portlet.match", title = "Match", tooltip = "Specifies match criteria")
public class MatchPortletPresenter extends AbstractWebProtegePortletPresenter {

    @Nonnull
    private final RootCriteriaPresenter presenter;

    @Inject
    public MatchPortletPresenter(@Nonnull SelectionModel selectionModel,
                                 @Nonnull ProjectId projectId,
                                 @Nonnull RootCriteriaPresenter presenter) {
        super(selectionModel, projectId);
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        presenter.start(portletUi);
    }
}
