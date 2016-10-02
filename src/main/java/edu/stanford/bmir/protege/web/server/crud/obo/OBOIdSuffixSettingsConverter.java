package edu.stanford.bmir.protege.web.server.crud.obo;

import edu.stanford.bmir.protege.web.server.persistence.DocumentConverter;
import edu.stanford.bmir.protege.web.shared.crud.oboid.OBOIdSuffixSettings;
import edu.stanford.bmir.protege.web.shared.crud.oboid.UserIdRange;
import org.bson.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 1 Oct 2016
 */
public class OBOIdSuffixSettingsConverter implements DocumentConverter<OBOIdSuffixSettings> {

    private static final String TOTAL_DIGITS = "totalDigits";

    private static final String USER_ID_RANGES = "userIdRanges";

    private final UserIdRangeConverter userIdRangeConverter;

    @Inject
    public OBOIdSuffixSettingsConverter(@Nonnull  UserIdRangeConverter userIdRangeConverter) {
        this.userIdRangeConverter = checkNotNull(userIdRangeConverter);
    }

    @Override
    public Document toDocument(@Nonnull OBOIdSuffixSettings object) {
        Document document = new Document();
        document.append(TOTAL_DIGITS, object.getTotalDigits());
        List<Document> userIdRanges = object.getUserIdRanges().stream()
                .map(r -> userIdRangeConverter.toDocument(r))
                .collect(toList());
        document.append(USER_ID_RANGES, userIdRanges);
        return document;
    }

    @SuppressWarnings("unchecked")
    @Override
    public OBOIdSuffixSettings fromDocument(@Nonnull Document document) {
        int totalDigits = document.getInteger(TOTAL_DIGITS);
        List<UserIdRange> userIdRanges = ((List<Document>) document.get(USER_ID_RANGES)).stream()
                .map(d -> userIdRangeConverter.fromDocument(d))
                .collect(toList());
        return new OBOIdSuffixSettings(totalDigits, userIdRanges);
    }
}
