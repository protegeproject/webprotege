package edu.stanford.bmir.protege.web.server.crud.supplied;

import edu.stanford.bmir.protege.web.server.persistence.DocumentConverter;
import edu.stanford.bmir.protege.web.shared.crud.supplied.SuppliedNameSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.supplied.WhiteSpaceTreatment;
import org.bson.Document;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Oct 2016
 */
public class SuppliedNameSuffixSettingsConverter implements DocumentConverter<SuppliedNameSuffixSettings> {

    private static final String WHITE_SPACE_TREATMENT = "whiteSpaceTreatment";

    @Override
    public Document toDocument(@Nonnull SuppliedNameSuffixSettings object) {
        return new Document(WHITE_SPACE_TREATMENT, object.getWhiteSpaceTreatment().name());
    }

    @Override
    public SuppliedNameSuffixSettings fromDocument(@Nonnull Document document) {
        WhiteSpaceTreatment whiteSpaceTreatment = WhiteSpaceTreatment.valueOf(document.getString(WHITE_SPACE_TREATMENT));
        return new SuppliedNameSuffixSettings(whiteSpaceTreatment);
    }
}
