package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

import java.io.IOException;
import java.util.Collections;

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
                FormFieldId.get("TheFormFieldId"),
                null,
                LanguageMap.empty(),
                FieldRun.START,
                new TextControlDescriptor(LanguageMap.empty(), StringType.SIMPLE_STRING, LineMode.SINGLE_LINE, "", LanguageMap.empty()),
                Repeatability.NON_REPEATABLE,
                Optionality.REQUIRED,
                true,
                LanguageMap.empty()
        );
        var serialized = objectMapper.writeValueAsString(formElementDescriptor);
        var deserialized = objectMapper.readerFor(FormFieldDescriptor.class).readValue(serialized);
        assertThat(formElementDescriptor, is(deserialized));
    }

    @Test
    public void shouldSerializeElementWithOwlPropertyBinding() throws IOException {
        var formElementDescriptor = FormFieldDescriptor.get(
                FormFieldId.get("TheFormFieldId"),
                OwlPropertyBinding.get(new OWLObjectPropertyImpl(IRI.create("http://example.org/prop")), null),
                LanguageMap.empty(),
                FieldRun.START,
                new TextControlDescriptor(LanguageMap.empty(), StringType.SIMPLE_STRING, LineMode.SINGLE_LINE, "", LanguageMap.empty()),
                Repeatability.NON_REPEATABLE,
                Optionality.REQUIRED,
                true,
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
                FormFieldId.get("TheFormFieldId"),
                OwlClassBinding.get(),
                LanguageMap.empty(),
                FieldRun.START,
                new TextControlDescriptor(LanguageMap.empty(), StringType.SIMPLE_STRING, LineMode.SINGLE_LINE, "", LanguageMap.empty()),
                Repeatability.NON_REPEATABLE,
                Optionality.REQUIRED,
                true,
                LanguageMap.empty()
        );
        var serialized = objectMapper.writeValueAsString(formElementDescriptor);
        System.out.println(serialized);
        var deserialized = objectMapper.readerFor(FormFieldDescriptor.class).readValue(serialized);
        assertThat(deserialized, is(formElementDescriptor));
    }

    @Test
    public void shouldParseWithNoOwlBinding() throws IOException {
        var serializedForm = "{\"id\":\"TheFormFieldId\",\"label\":{},\"elementRun\":\"START\",\"formControlDescriptor\":{\"type\":\"TEXT\",\"placeholder\":{},\"stringType\":\"SIMPLE_STRING\",\"lineMode\":\"SINGLE_LINE\",\"patternViolationErrorMessage\":{}},\"repeatability\":\"NON_REPEATABLE\",\"optionality\":\"REQUIRED\",\"help\":{}}";
        FormFieldDescriptor deserializedForm = objectMapper.readerFor(FormFieldDescriptor.class)
                                                           .readValue(serializedForm);
        assertThat(deserializedForm.getOwlBinding().isEmpty(), is(true));

    }
}
