package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-09
 */
public class SubFormFieldDescriptor_IT {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapperProvider().get();
    }

    @Test
    public void shouldSerializeAndDeserialize() throws IOException {
        var formDescriptor = new FormDescriptor(FormId.get("SubFormId"),
                                                LanguageMap.of("en", "The sub form"),
                                                Arrays.asList(
                                                        FormElementDescriptor.get(
                                                                FormElementId.get("Label"),
                                                                new OWLObjectPropertyImpl(OWLRDFVocabulary.RDFS_LABEL.getIRI()),
                                                                LanguageMap.of("en", "The Label"),
                                                                ElementRun.START,
                                                                new TextFieldDescriptor(
                                                                        LanguageMap.empty(),
                                                                        StringType.SIMPLE_STRING,
                                                                        LineMode.SINGLE_LINE,
                                                                        "Pattern",
                                                                        LanguageMap.empty()
                                                                ),
                                                                Repeatability.NON_REPEATABLE,
                                                                Optionality.REQUIRED,
                                                                LanguageMap.empty(),
                                                                Map.of()
                                                        )
                                                ));
        SubFormFieldDescriptor descriptor = new SubFormFieldDescriptor(formDescriptor);
        var serialized = objectMapper.writeValueAsString(descriptor);
        var deserialized = objectMapper.readerFor(SubFormFieldDescriptor.class)
                .readValue(serialized);
        assertThat(deserialized, is(descriptor));
    }
}
