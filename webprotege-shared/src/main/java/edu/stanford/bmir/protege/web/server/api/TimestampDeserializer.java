package edu.stanford.bmir.protege.web.server.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.google.gwt.core.shared.GwtIncompatible;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jul 2018
 *
 * Deserializes ISO timestamps into longs (UTC Zone).  This also deserializes
 * mongodb timestamps which look like this  ISODate("2018-09-01T01:02:03Z")
 */
@GwtIncompatible
public class TimestampDeserializer extends StdDeserializer<Long> {

    private static final String UTC = "UTC";

    private static final ZoneId ZONE_ID = ZoneId.of(UTC);

    private static final DateTimeFormatter FORMATTER = createFormatter();

    // Legacy MongoDB serialization
    private static final String ISODATE_PREFIX = "ISODate(\"";

    private static final String ISODATE_SUFFIX = "\")";

    public TimestampDeserializer() {
        super(String.class);
    }

    @GwtIncompatible
    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String ts = extractDateTime(jsonParser.getValueAsString());
        ZonedDateTime dateTime = ZonedDateTime.parse(ts, FORMATTER);
        Instant instant = dateTime.toInstant();
        return instant.toEpochMilli();
    }

    @GwtIncompatible
    private static String extractDateTime(@Nonnull String dateTime) {
        if(dateTime.startsWith(ISODATE_PREFIX)) {
            return dateTime.substring(ISODATE_PREFIX.length(), dateTime.length() - ISODATE_SUFFIX.length());
        }
        else {
            return dateTime;
        }
    }

    @GwtIncompatible
    private static DateTimeFormatter createFormatter() {
        // This stuff is due to a JDK bug in parsing date times with basic zone
        // offsets (i.e. zone offsets that do not contain colons)
        // See https://stackoverflow.com/questions/46487403, where this was taken from
        return new DateTimeFormatterBuilder()
                // date/time
                .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                // offset (hh:mm - "+00:00" when it's zero)
                .optionalStart().appendOffset("+HH:MM", "+00:00").optionalEnd()
                // offset (hhmm - "+0000" when it's zero)
                .optionalStart().appendOffset("+HHMM", "+0000").optionalEnd()
                // offset (hh - "Z" when it's zero)
                .optionalStart().appendOffset("+HH", "Z").optionalEnd()
                // create formatter
                .toFormatter();

    }
}
