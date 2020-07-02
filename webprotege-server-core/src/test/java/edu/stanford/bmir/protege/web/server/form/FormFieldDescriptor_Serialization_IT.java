package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.form.ExpansionState;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
public class FormFieldDescriptor_Serialization_IT {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapperProvider().get();
    }

    @Test
    public void shouldSerializeElementWithoutOwlBinding() throws IOException {
        var formElementDescriptor = FormFieldDescriptor.get(
                FormFieldId.get(UUID.randomUUID().toString()),
                null,
                LanguageMap.empty(),
                FieldRun.START,
                new TextControlDescriptor(LanguageMap.empty(), StringType.SIMPLE_STRING, LineMode.SINGLE_LINE, "", LanguageMap.empty()),
                Repeatability.NON_REPEATABLE,
                Optionality.REQUIRED,
                true,
                ExpansionState.COLLAPSED,
                LanguageMap.empty()
        );
        var serialized = objectMapper.writeValueAsString(formElementDescriptor);
        var deserialized = objectMapper.readerFor(FormFieldDescriptor.class).readValue(serialized);
        assertThat(formElementDescriptor, is(deserialized));
    }

    @Test
    public void shouldSerializeElementWithOwlPropertyBinding() throws IOException {
        var formElementDescriptor = FormFieldDescriptor.get(
                FormFieldId.get(UUID.randomUUID().toString()),
                OwlPropertyBinding.get(new OWLObjectPropertyImpl(IRI.create("http://example.org/prop")), null),
                LanguageMap.empty(),
                FieldRun.START,
                new TextControlDescriptor(LanguageMap.empty(), StringType.SIMPLE_STRING, LineMode.SINGLE_LINE, "", LanguageMap.empty()),
                Repeatability.NON_REPEATABLE,
                Optionality.REQUIRED,
                true,
                ExpansionState.COLLAPSED,
                LanguageMap.empty()
        );
        var serialized = objectMapper.writeValueAsString(formElementDescriptor);
        System.out.println(serialized);
        var deserialized = objectMapper.readerFor(FormFieldDescriptor.class).readValue(serialized);
        assertThat(deserialized, is(formElementDescriptor));
    }

    @Test
    public void shouldSerializeElementWithOwlClassBinding() throws IOException {
        var formElementDescriptor = FormFieldDescriptor.get(
                FormFieldId.get(UUID.randomUUID().toString()),
                OwlClassBinding.get(),
                LanguageMap.empty(),
                FieldRun.START,
                new TextControlDescriptor(LanguageMap.empty(), StringType.SIMPLE_STRING, LineMode.SINGLE_LINE, "", LanguageMap.empty()),
                Repeatability.NON_REPEATABLE,
                Optionality.REQUIRED,
                true,
                ExpansionState.COLLAPSED,
                LanguageMap.empty()
        );
        var serialized = objectMapper.writeValueAsString(formElementDescriptor);
        System.out.println(serialized);
        var deserialized = objectMapper.readerFor(FormFieldDescriptor.class).readValue(serialized);
        assertThat(deserialized, is(formElementDescriptor));
    }

    @Test
    public void shouldParseWithNoOwlBinding() throws IOException {
        var serializedForm = "{\"id\":\"12345678-1234-1234-1234-123456789abc\",\"label\":{},\"elementRun\":\"START\",\"formControlDescriptor\":{\"type\":\"TEXT\",\"placeholder\":{},\"stringType\":\"SIMPLE_STRING\",\"lineMode\":\"SINGLE_LINE\",\"patternViolationErrorMessage\":{}},\"repeatability\":\"NON_REPEATABLE\",\"optionality\":\"REQUIRED\",\"help\":{}}";
        FormFieldDescriptor deserializedForm = objectMapper.readerFor(FormFieldDescriptor.class)
                                                           .readValue(serializedForm);
        assertThat(deserializedForm.getOwlBinding().isEmpty(), is(true));

    }
}
