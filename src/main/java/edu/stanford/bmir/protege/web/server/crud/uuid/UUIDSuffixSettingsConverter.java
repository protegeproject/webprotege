package edu.stanford.bmir.protege.web.server.crud.uuid;

import edu.stanford.bmir.protege.web.server.persistence.DocumentConverter;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UUIDSuffixSettings;
import org.bson.Document;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Oct 2016
 */
public class UUIDSuffixSettingsConverter implements DocumentConverter<UUIDSuffixSettings> {

    private static final UUIDSuffixSettings UUID_SUFFIX_SETTINGS = new UUIDSuffixSettings();

    @Override
    public Document toDocument(@Nonnull UUIDSuffixSettings object) {
        return new Document();
    }

    @Override
    public UUIDSuffixSettings fromDocument(@Nonnull Document document) {
        return UUID_SUFFIX_SETTINGS;
    }
}
