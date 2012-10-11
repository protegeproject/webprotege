package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.EntityIdGeneratorSettings;
import edu.stanford.bmir.protege.web.client.rpc.data.ProjectId;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/10/2012
 */
@RemoteServiceRelativePath("entityidgeneratorsettings")
public interface EntityIdGeneratorSettingsService extends RemoteService {

    EntityIdGeneratorSettings getSettings(ProjectId projectId);
    
    void setSettings(ProjectId projectId, EntityIdGeneratorSettings settings);
}
