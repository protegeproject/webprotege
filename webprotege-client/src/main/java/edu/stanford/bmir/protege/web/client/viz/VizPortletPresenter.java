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
import edu.stanford.bmir.protege.web.shared.viz.GetEntityDotRenderingResult;
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
                             this::handleRendering);
        });
    }

    private void handleRendering(@Nonnull GetEntityDotRenderingResult result) {
        Viz viz = new Viz();
        String rendering = replaceVariables(result.getRendering());
        viz.render(rendering, view::setRendering);
    }

    private String replaceVariables(@Nonnull String rendering) {
        return rendering
                .replace("${title}", "Entity Graph")
                .replace("${layout}", "dot")
                .replace("${rankdir}", "BT")
                .replace("${concentrate}", "true")
                .replace("${splines}", "true")
                .replace("${ranksep}", "0.75")
                .replace("${nodesep}", "0.3")
                .replace("${node.style}", "rounded")
                .replace("${node.shape}", "box")
                .replace("${node.color}", "#d0d0d0")
                .replace("${node.margin}", "0.03")
                .replace("${node.fontcolor}", "#505050")
                .replace("${edge.rel.color}", "#4784d1")
                .replace("${edge.isa.color}", "#c0c0c0")
                .replace("${edge.arrowsize}", "0.8");
    }
}
