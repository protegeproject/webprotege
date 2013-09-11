package edu.stanford.bmir.protege.web.server.crud.persistence;

import edu.stanford.bmir.protege.web.server.app.WebProtegeApplicationContext;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 21/08/2013
 */
public class ProjectEntityCrudKitSettingsRepositoryManager {

    /**
     * Gets the {@link ProjectEntityCrudKitSettingsRepository} that is responsible for storing {@link ProjectEntityCrudKitSettings}.
     * @return The repository. Not {@code null}.
     */
    public static ProjectEntityCrudKitSettingsRepository getRepository() {
        return WebProtegeApplicationContext.getContext().getBean(ProjectEntityCrudKitSettingsRepository.class);
    }
}
