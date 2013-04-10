package edu.stanford.bmir.protege.web.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.stanford.bmir.protege.web.client.rpc.EntityIdGeneratorSettingsService;
import edu.stanford.bmir.protege.web.client.rpc.data.*;
import edu.stanford.bmir.protege.web.server.owlapi.EntityIdGeneratorSettingsManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/10/2012
 */
public class EntityIdGeneratorSettingsServiceImpl extends RemoteServiceServlet implements EntityIdGeneratorSettingsService {

    public EntityIdGeneratorSettings getSettings(ProjectId projectId) {
        return EntityIdGeneratorSettingsManager.getManager().getSettingsForProject(projectId);
    }

    public void setSettings(ProjectId projectId, EntityIdGeneratorSettings settings) {
        EntityIdGeneratorSettingsManager.getManager().setSettingsForProject(settings, projectId);
    }
}
