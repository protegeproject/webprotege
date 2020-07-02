package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.form.FormSubjectFactoryDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-15
 */
public class FormDescriptor_Serialization_IT {

    private static final FormId FORM_ID = FormId.get("12345678-1234-1234-1234-123456789abc");

    private static final LanguageMap LABEL = LanguageMap.of("en", "The label");

    private OWLClass parent;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        parent = DataFactory.getOWLThing();
        objectMapper = new ObjectMapperProvider().get();
    }

    @Test
    public void shouldSerializeFormDescriptor() throws IOException {
        var subjectFactoryDescriptor = FormSubjectFactoryDescriptor.get(
                EntityType.CLASS,
                parent,
                Optional.of(IRI.create("http://example.org/target-ontology"))
        );
        var formDescriptor = new FormDescriptor(
                FORM_ID,
                LABEL,
                Collections.emptyList(),
                Optional.of(subjectFactoryDescriptor)
        );
        var serialized = objectMapper.writeValueAsString(formDescriptor);
        System.out.println(serialized);
        var deserialized = objectMapper.readerFor(FormDescriptor.class).readValue(serialized);
        assertThat(deserialized, is(formDescriptor));
    }

    @Test
    public void shouldSerializeFormDescriptorWithoutTargetOntologyIri() throws IOException {
        var subjectFactoryDescriptor = FormSubjectFactoryDescriptor.get(
                EntityType.CLASS,
                parent,
                Optional.empty()
        );
        var formDescriptor = new FormDescriptor(
                FORM_ID,
                LABEL,
                Collections.emptyList(),
                Optional.of(subjectFactoryDescriptor)
        );
        var serialized = objectMapper.writeValueAsString(formDescriptor);
        System.out.println(serialized);
        var deserialized = objectMapper.readerFor(FormDescriptor.class).readValue(serialized);
        assertThat(deserialized, is(formDescriptor));
    }

}
