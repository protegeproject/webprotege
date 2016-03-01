package edu.stanford.bmir.protege.web.client.portlet;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceCallback;
import edu.stanford.bmir.protege.web.client.dispatch.DispatchServiceManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingAction;
import edu.stanford.bmir.protege.web.shared.renderer.GetEntityRenderingResult;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 01/03/16
 */
public class TitleHelper {

    private final String prefix;

    private final DispatchServiceManager manager;

    private final ProjectId projectId;

    public TitleHelper(String prefix, DispatchServiceManager manager, ProjectId projectId) {
        this.prefix = prefix;
        this.manager = manager;
        this.projectId = projectId;
    }

    public void updateTitle(final Optional<OWLEntity> entity, final EntityPortlet portlet) {
        if(entity.isPresent()) {
            manager.execute(new GetEntityRenderingAction(projectId, entity.get()), new DispatchServiceCallback<GetEntityRenderingResult>() {
                @Override
                public void handleSuccess(GetEntityRenderingResult result) {
                    portlet.setTitle(prefix + ": " + result.getRendering());
                }
            });
        }
        else {
            portlet.setTitle(prefix + ": Nothing selected");
        }
    }
}
