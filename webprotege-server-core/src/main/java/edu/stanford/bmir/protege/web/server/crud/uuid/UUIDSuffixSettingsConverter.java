package edu.stanford.bmir.protege.web.server.crud.uuid;

import edu.stanford.bmir.protege.web.server.persistence.DocumentConverter;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidSuffixSettings;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Oct 2016
 */
public class UUIDSuffixSettingsConverter implements DocumentConverter<UuidSuffixSettings> {

    private static final UuidSuffixSettings UUID_SUFFIX_SETTINGS = UuidSuffixSettings.get();

    @Inject
    public UUIDSuffixSettingsConverter() {
    }

    @Override
    public Document toDocument(@Nonnull UuidSuffixSettings object) {
        return new Document();
    }

    @Override
    public UuidSuffixSettings fromDocument(@Nonnull Document document) {
        return UUID_SUFFIX_SETTINGS;
    }
}
