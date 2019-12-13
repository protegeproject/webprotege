package edu.stanford.bmir.protege.web.client.viz;

import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.entity.EntityDisplay;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.client.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 11 Oct 2018
 */
@Portlet(id = "portlets.viz", title = "Entity Graph", tooltip = "Provides a visualisation")
public class VizPortletPresenter extends AbstractWebProtegePortletPresenter implements EntityDisplay {

    @Nonnull
    private final VizPresenter vizPresenter;

    @Inject
    public VizPortletPresenter(@Nonnull SelectionModel selectionModel,
                               @Nonnull ProjectId projectId,
                               @Nonnull DisplayNameRenderer displayNameRenderer,
                               @Nonnull VizPresenter vizPresenter) {
        super(selectionModel, projectId, displayNameRenderer);
        this.vizPresenter = vizPresenter;
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        setDisplaySelectedEntityNameAsSubtitle(true);
        vizPresenter.setEntityDisplay(this);
        vizPresenter.start(portletUi);
        vizPresenter.setHasBusy(portletUi);
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        entityData.ifPresent(vizPresenter::displayEntity);
    }
}
