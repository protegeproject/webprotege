package edu.stanford.bmir.protege.web.server.crud.persistence;

import edu.stanford.bmir.protege.web.server.crud.obo.OBOIdSuffixSettingsConverter;
import edu.stanford.bmir.protege.web.server.crud.supplied.SuppliedNameSuffixSettingsConverter;
import edu.stanford.bmir.protege.web.server.crud.uuid.UUIDSuffixSettingsConverter;
import edu.stanford.bmir.protege.web.server.persistence.DocumentConverter;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.oboid.OBOIdSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UUIDSuffixSettings;
import org.bson.Document;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30 Sep 2016
 */
public class SuffixSettingsConverter implements DocumentConverter<EntityCrudKitSuffixSettings> {

    public static final UUIDSuffixSettings DEFAULT_SETTINGS = new UUIDSuffixSettings();

    public static final String CLASS = "_class";

    @Nonnull
    private final UUIDSuffixSettingsConverter uuidSuffixSettingsConverter;

    @Nonnull
    private final OBOIdSuffixSettingsConverter oboSettingsConverter;

    @Nonnull
    private final SuppliedNameSuffixSettingsConverter suppliedNameSettingsConverter;

    @Inject
    public SuffixSettingsConverter(@Nonnull UUIDSuffixSettingsConverter uuidSuffixSettingsConverter,
                                   @Nonnull OBOIdSuffixSettingsConverter oboSettingsConverter,
                                   @Nonnull SuppliedNameSuffixSettingsConverter suppliedNameSettingsConverter) {
        this.uuidSuffixSettingsConverter = checkNotNull(uuidSuffixSettingsConverter);
        this.oboSettingsConverter = checkNotNull(oboSettingsConverter);
        this.suppliedNameSettingsConverter = checkNotNull(suppliedNameSettingsConverter);
    }

    @Override
    public Document toDocument(@Nonnull EntityCrudKitSuffixSettings object) {
        Document document;
        if(object instanceof UUIDSuffixSettings) {
            document = uuidSuffixSettingsConverter.toDocument((UUIDSuffixSettings) object);
        }
        else if(object instanceof  OBOIdSuffixSettings) {
            document = oboSettingsConverter.toDocument((OBOIdSuffixSettings) object);
        }
        else if(object instanceof SuppliedNameSuffixSettings) {
            document = suppliedNameSettingsConverter.toDocument((SuppliedNameSuffixSettings) object);
        }
        else {
            throw new RuntimeException("Unknown type of suffix settings: " + object);
        }
        document.append(CLASS, object.getClass().getName());
        return document;
    }

    @Override
    public EntityCrudKitSuffixSettings fromDocument(@Nonnull Document document) {
        String type = document.getString(CLASS);
        if(UUIDSuffixSettings.class.getName().equals(type)) {
            return uuidSuffixSettingsConverter.fromDocument(document);
        }
        else if(OBOIdSuffixSettings.class.getName().equals(type)) {
            return oboSettingsConverter.fromDocument(document);
        }
        else if(SuppliedNameSuffixSettings.class.getName().equals(type)) {
            return suppliedNameSettingsConverter.fromDocument(document);
        }
        return DEFAULT_SETTINGS;
    }
}
