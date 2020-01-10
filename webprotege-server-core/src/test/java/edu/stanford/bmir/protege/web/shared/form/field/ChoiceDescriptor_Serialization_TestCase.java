package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlData;
import edu.stanford.bmir.protege.web.shared.form.data.PrimitiveFormControlData;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import uk.ac.manchester.cs.owl.owlapi.OWLClassImpl;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-09
 */
public class ChoiceDescriptor_Serialization_TestCase {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapperProvider().get();
    }

    @Test
    public void shouldSerializeAndDeserializeChoiceDescriptor() throws IOException {
        var label = LanguageMap.of("en", "Hello World");
        var value = PrimitiveFormControlData.get(new OWLClassImpl(IRI.create("http://example.org/A")));
        var choiceDescriptor = ChoiceDescriptor.choice(label, value);
        var serialized = objectMapper.writeValueAsString(choiceDescriptor);
        System.out.println(serialized);
        var deserialized = objectMapper.readerFor(ChoiceDescriptor.class).readValue(serialized);
        assertThat(deserialized, Matchers.is(choiceDescriptor));
    }
}
