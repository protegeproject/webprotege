
package edu.stanford.bmir.protege.web.server.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimestampDeserializer_TestCase {

    private TimestampDeserializer deserializer;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    private String dateTime = "2018-09-01T10:50:33Z";

    private String dateTimeNonUtc = "2018-09-01T10:50:33-09:00";

    private String dateTimeWithNumericOffsetInBasicFormat = "2018-09-01T10:50:33+0100";

    private String mongoIsoDateTime = "ISODate(\"" + dateTime + "\")";

    @Before
    public void setUp() {
        deserializer = new TimestampDeserializer();
    }

    @Test
    public void shouldDeserializeDateTime() throws IOException {
        when(jsonParser.getValueAsString()).thenReturn(dateTime);
        long ts = deserializer.deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void shouldDeserializeDateTimeWithNonUtcTimeZone() throws IOException {
        when(jsonParser.getValueAsString()).thenReturn(dateTimeNonUtc);
        long ts = deserializer.deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void shouldDeserializeMongoIsoDateTime() throws IOException {
        when(jsonParser.getValueAsString()).thenReturn(mongoIsoDateTime);
        long ts = deserializer.deserialize(jsonParser, deserializationContext);
    }


    @Test
    public void shouldDeserializeMongoIsoDateTimeWithZone() throws IOException {
        when(jsonParser.getValueAsString()).thenReturn(dateTimeWithNumericOffsetInBasicFormat);
        long ts = deserializer.deserialize(jsonParser, deserializationContext);
    }

}
