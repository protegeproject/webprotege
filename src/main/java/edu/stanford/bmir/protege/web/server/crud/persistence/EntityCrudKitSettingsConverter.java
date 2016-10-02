package edu.stanford.bmir.protege.web.server.crud.persistence;

import edu.stanford.bmir.protege.web.server.persistence.DocumentConverter;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30 Sep 2016
 */
public class EntityCrudKitSettingsConverter implements DocumentConverter<EntityCrudKitSettings> {

    private static final String PREFIX_SETTINGS = "prefixSettings";

    private static final String SUFFIX_SETTINGS = "suffixSettings";

    @Nonnull
    private PrefixSettingsConverter prefixSettingsConverter;

    @Nonnull
    private SuffixSettingsConverter suffixSettingsConverter;

    @Inject
    public EntityCrudKitSettingsConverter(@Nonnull PrefixSettingsConverter prefixSettingsConverter,
                                          @Nonnull SuffixSettingsConverter suffixSettingsConverter) {
        this.prefixSettingsConverter = checkNotNull(prefixSettingsConverter);
        this.suffixSettingsConverter = checkNotNull(suffixSettingsConverter);
    }

    @Override
    public Document toDocument(@Nonnull EntityCrudKitSettings object) {
        Document document = new Document();
        Document prefixSettings = prefixSettingsConverter.toDocument(object.getPrefixSettings());
        document.append(PREFIX_SETTINGS, prefixSettings);
        Document suffixSettings = suffixSettingsConverter.toDocument(object.getSuffixSettings());
        document.append(SUFFIX_SETTINGS, suffixSettings);
        return document;
    }

    @Override
    public EntityCrudKitSettings fromDocument(@Nonnull Document document) {
        return null;
    }
}
