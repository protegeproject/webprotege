package edu.stanford.bmir.protege.web.server.api;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 23 Apr 2018
 */
public class TimestampSerializer extends StdSerializer<Long> {

    private static final String UTC = "UTC";

    private static final ZoneId ZONE_ID = ZoneId.of(UTC);

    public TimestampSerializer() {
        super(Long.class);
    }

    @Override
    public void serialize(Long l, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Instant instant = Instant.ofEpochMilli(l);
        ZonedDateTime dateTime = ZonedDateTime.ofInstant(instant, ZONE_ID);
        String formatted = dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        jsonGenerator.writeString(formatted);
    }
}
