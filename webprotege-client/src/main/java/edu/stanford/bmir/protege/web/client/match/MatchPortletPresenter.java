package edu.stanford.bmir.protege.web.client.match;

import edu.stanford.bmir.protege.web.client.library.msgbox.InputBox;
import edu.stanford.bmir.protege.web.client.library.msgbox.InputBoxHandler;
import edu.stanford.bmir.protege.web.client.library.msgbox.MessageBox;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletAction;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.match.criteria.Criteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.RootCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Optional;

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
        portletUi.addAction(new PortletAction("Show criteria", () -> {
            Optional<? extends Criteria> criteria = presenter.getCriteria();
            String s = criteria.map(Object::toString).orElse("Empty");
            InputBox.showDialog("Criteria",
                                true,
                                s,
                                input -> {});
        }));
        presenter.start(portletUi);
    }
}
