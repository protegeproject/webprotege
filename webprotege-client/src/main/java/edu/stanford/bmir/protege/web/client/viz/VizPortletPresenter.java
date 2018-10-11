package edu.stanford.bmir.protege.web.client.viz;

import com.google.gwt.user.client.ui.FlowPanel;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.client.lang.DisplayNameRenderer;
import edu.stanford.bmir.protege.web.client.portlet.AbstractWebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.viz.GetEntityDotRenderingAction;
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
@Portlet(id = "portlets.viz", title = "Viz", tooltip = "Provides a visualisation")
public class VizPortletPresenter extends AbstractWebProtegePortletPresenter {

    private final VizView view = new VizViewImpl();

    @Nonnull
    private final DispatchServiceManager dispatch;

    @Nonnull
    private Optional<PortletUi> portletUi = Optional.empty();

    @Inject
    public VizPortletPresenter(@Nonnull SelectionModel selectionModel,
                               @Nonnull ProjectId projectId,
                               @Nonnull DisplayNameRenderer displayNameRenderer,
                               @Nonnull DispatchServiceManager dispatch) {
        super(selectionModel, projectId, displayNameRenderer);
        this.dispatch = checkNotNull(dispatch);
    }

    @Override
    public void startPortlet(PortletUi portletUi, WebProtegeEventBus eventBus) {
        portletUi.setWidget(view);
        this.portletUi = Optional.of(portletUi);
    }

    @Override
    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
        entityData.ifPresent(entity -> {
            dispatch.execute(new GetEntityDotRenderingAction(getProjectId(), entity),
                             busy -> portletUi.ifPresent(ui -> ui.setBusy(busy)),
                             result -> {
                Viz viz = new Viz();
                viz.render(result.getRendering(), view::setRendering);
            });
        });
    }
}
