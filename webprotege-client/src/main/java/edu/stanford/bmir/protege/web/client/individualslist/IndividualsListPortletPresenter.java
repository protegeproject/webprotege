package edu.stanford.bmir.protege.web.client.individualslist;

import edu.stanford.bmir.protege.web.client.filter.FilterView;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameSettingsManager;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.tag.TagVisibilityPresenter;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Portlet(id = "portlets.IndividualsList", title = "Individuals by Class")
public class IndividualsListPortletPresenter extends AbstractWebProtegePortletPresenter {

    private final IndividualsListPresenter presenter;

    private final DisplayNameSettingsManager displayNameSettingsManager;

    @Nonnull
    private final FilterView filterView;

    @Nonnull
    private final TagVisibilityPresenter tagVisibilityPresenter;

    @Inject
    public IndividualsListPortletPresenter(@Nonnull IndividualsListPresenter presenter,
                                           @Nonnull SelectionModel selectionModel,
                                           @Nonnull ProjectId projectId,
                                           @Nonnull DisplayNameRenderer displayNameRenderer,
                                           @Nonnull DisplayNameSettingsManager displayNameSettingsManager,
                                           @Nonnull FilterView filterView,
                                           @Nonnull TagVisibilityPresenter tagVisibilityPresenter) {
        super(selectionModel, projectId, displayNameRenderer);
        this.presenter = checkNotNull(presenter);
        this.displayNameSettingsManager = checkNotNull(displayNameSettingsManager);
        this.filterView = checkNotNull(filterView);
        this.tagVisibilityPresenter = checkNotNull(tagVisibilityPresenter);
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setFilterView(filterView);
        tagVisibilityPresenter.start(filterView, portletUi);
        presenter.installActions(portletUi);
        presenter.start(portletUi, eventBus);
        presenter.setDisplayLanguage(displayNameSettingsManager.getLocalDisplayNameSettings());
        handleAfterSetEntity(getSelectedEntity());
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entity) {
        Optional<OWLNamedIndividual> sel = entity.filter(OWLEntity::isOWLNamedIndividual)
                .map(e -> (OWLNamedIndividual) e);
        sel.ifPresent(presenter::setDisplayedIndividual);
    }
}
