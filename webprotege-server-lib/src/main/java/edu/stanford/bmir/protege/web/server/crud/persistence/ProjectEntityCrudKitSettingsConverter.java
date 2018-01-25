package edu.stanford.bmir.protege.web.server.crud.persistence;

import edu.stanford.bmir.protege.web.server.persistence.DocumentConverter;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30 Sep 2016
 */
public class ProjectEntityCrudKitSettingsConverter implements DocumentConverter<ProjectEntityCrudKitSettings> {

    private static final String PROJECT_ID = "_id";

    private static final String SETTINGS = "settings";

    private EntityCrudKitSettingsConverter converter;

    @Inject
    public ProjectEntityCrudKitSettingsConverter(EntityCrudKitSettingsConverter converter) {
        this.converter = converter;
    }

    @Override
    public Document toDocument(@Nonnull ProjectEntityCrudKitSettings object) {
        Document document = new Document();
        document.append(PROJECT_ID, object.getProjectId().getId());
        EntityCrudKitSettings<? extends EntityCrudKitSuffixSettings> settings = object.getSettings();
        Document settingsDocument = converter.toDocument(settings);
        document.append(SETTINGS, settingsDocument);
        return document;
    }

    @Override
    public ProjectEntityCrudKitSettings fromDocument(@Nonnull Document document) {
        ProjectId projectId = ProjectId.get(document.getString(PROJECT_ID));
        Document settingsDocument = (Document) document.get(SETTINGS);
        EntityCrudKitSettings<? extends EntityCrudKitSuffixSettings> settings = converter.fromDocument(settingsDocument);
        return new ProjectEntityCrudKitSettings(projectId, settings);
    }

    @Nonnull
    public static Document withProjectId(ProjectId projectId) {
        return new Document(PROJECT_ID, projectId.getId());
    }
}
