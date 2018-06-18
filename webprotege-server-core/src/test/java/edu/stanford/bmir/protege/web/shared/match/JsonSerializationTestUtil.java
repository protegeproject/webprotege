package edu.stanford.bmir.protege.web.shared.match;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.stanford.bmir.protege.web.server.api.OWLEntitySerializer;
import edu.stanford.bmir.protege.web.server.jackson.IriMixin;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jun 2018
 */
public class JsonSerializationTestUtil {

    public static  <C, V extends C> void testSerialization(V object, Class<C> cls) throws IOException {
        StringWriter writer = new StringWriter();
        ObjectMapperProvider mapperProvider = new ObjectMapperProvider();
        ObjectMapper mapper = mapperProvider.get();
        mapper.writeValue(writer, object);
        System.out.println(writer.toString());
        C deserializedObject = mapper.readValue(new StringReader(writer.toString()), cls);
        assertThat(deserializedObject, is(object));
    }

}
