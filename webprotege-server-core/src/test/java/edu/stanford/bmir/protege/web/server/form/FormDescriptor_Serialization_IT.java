package edu.stanford.bmir.protege.web.server.form;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.form.EntityFormSubjectFactoryDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.field.FormElementDescriptor;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-15
 */
public class FormDescriptor_Serialization_IT {

    private static final String SUPPLIED_NAME_TEMPLATE = "${type}-${uuid}";

    private static final ImmutableList<String> AXIOM_TEMPLATES = ImmutableList.of("SubClassOf(${subject.iri} <http://example.org/A>)");

    private static final FormId FORM_ID = FormId.get("TheFormId");

    private static final LanguageMap LABEL = LanguageMap.of("en", "The label");

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapperProvider().get();
    }

    @Test
    public void shouldSerializeFormDescriptor() throws IOException {
        var subjectFactoryDescriptor = EntityFormSubjectFactoryDescriptor.get(
                EntityType.CLASS,
                SUPPLIED_NAME_TEMPLATE,
                AXIOM_TEMPLATES,
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
        var subjectFactoryDescriptor = EntityFormSubjectFactoryDescriptor.get(
                EntityType.CLASS,
                SUPPLIED_NAME_TEMPLATE,
                AXIOM_TEMPLATES,
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
