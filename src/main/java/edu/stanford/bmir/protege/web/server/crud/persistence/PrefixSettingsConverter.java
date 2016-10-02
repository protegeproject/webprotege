package edu.stanford.bmir.protege.web.server.crud.persistence;

import edu.stanford.bmir.protege.web.server.persistence.DocumentConverter;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30 Sep 2016
 */
public class PrefixSettingsConverter implements DocumentConverter<EntityCrudKitPrefixSettings> {

    private static final String IRI_PREFIX = "iriPrefix";

    @Inject
    public PrefixSettingsConverter() {
    }

    @Override
    public Document toDocument(@Nonnull EntityCrudKitPrefixSettings object) {
        Document document = new Document();
        document.append(IRI_PREFIX, object.getIRIPrefix());
        return document;
    }

    @Override
    public EntityCrudKitPrefixSettings fromDocument(@Nonnull Document document) {
        String iriPrefix = document.getString(IRI_PREFIX);
        return new EntityCrudKitPrefixSettings(iriPrefix);
    }
}
