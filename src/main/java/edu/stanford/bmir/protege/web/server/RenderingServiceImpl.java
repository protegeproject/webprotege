package edu.stanford.bmir.protege.web.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.stanford.bmir.protege.web.client.rpc.GetRendering;
import edu.stanford.bmir.protege.web.client.rpc.GetRenderingResponse;
import edu.stanford.bmir.protege.web.client.rpc.RenderingService;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.server.owlapi.RenderingManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Map;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 29/11/2012
 */
public class RenderingServiceImpl extends WebProtegeRemoteServiceServlet implements RenderingService  {

    @Override
    public GetRenderingResponse execute(GetRendering command) {
        ProjectId projectId = command.getProjectId();
        OWLAPIProject project = OWLAPIProjectManager.getProjectManager().getProject(projectId);
        RenderingManager rm = project.getRenderingManager();
        Map<OWLEntity, OWLEntityData> result = rm.getRendering(command.getEntities());
        return new GetRenderingResponse(result);
    }
}
