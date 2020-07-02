package edu.stanford.bmir.protege.web.server.form;

import com.mongodb.client.MongoDatabase;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.form.ExpansionState;
import edu.stanford.bmir.protege.web.shared.form.FormDescriptor;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.floatThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class EntityFormRepositoryImpl_IT {

    private EntityFormRepositoryImpl impl;

    private ProjectId projectId;

    private MongoDatabase database;

    @Before
    public void setUp() {
        projectId = ProjectId.get(UUID.randomUUID().toString());
        var mongoClient = MongoTestUtils.createMongoClient();
        database = mongoClient.getDatabase(MongoTestUtils.getTestDbName());

        var objectMapperProvider = new ObjectMapperProvider();
        var objectMapper = objectMapperProvider.get();
        impl = new EntityFormRepositoryImpl(objectMapper, database);
    }

    @After
    public void tearDown() throws Exception {
        database.drop();
    }

    @Test
    public void shouldSaveFormDescriptor() {

        var languageMap = LanguageMap.builder()
                                     .put("en", "Water")
                                     .put("de", "Wasser")
                                     .put("en-US", "Water")
                                     .build();
        var formId = FormId.generate();
        var formDescriptor = FormDescriptor.builder(formId)
                      .addDescriptor(FormFieldDescriptor.get(
                              FormFieldId.get(UUID.randomUUID().toString()),
                              OwlPropertyBinding.get(new OWLObjectPropertyImpl(OWLRDFVocabulary.RDFS_COMMENT.getIRI()),
                                                     null),
                              languageMap,
                              FieldRun.START,
                              new TextControlDescriptor(
                                      LanguageMap.of("en", "Enter brand name"),
                                      StringType.SIMPLE_STRING,
                                      LineMode.SINGLE_LINE,
                                      "Pattern",
                                      LanguageMap.of("en", "There's an error")
                              ),
                              Repeatability.NON_REPEATABLE,
                              Optionality.OPTIONAL,
                              true,
                              ExpansionState.COLLAPSED,
                              LanguageMap.of("en", "Help text")
                      ))
                      .build();

        impl.saveFormDescriptor(projectId,
                                formDescriptor);
        var deserializedFormDescriptor = impl.findFormDescriptor(projectId, formId);
        assertThat(deserializedFormDescriptor.isPresent(), is(true));
        assertThat(deserializedFormDescriptor.get(), is(formDescriptor));
    }

    @Test
    public void shouldDeleteFormDescriptor() {
        var formId = FormId.generate();
        var formIdOther = FormId.generate();

        impl.saveFormDescriptor(projectId, FormDescriptor.builder(formId).build());
        impl.saveFormDescriptor(projectId, FormDescriptor.builder(formIdOther).build());

        var formCount = impl.findFormDescriptors(projectId).count();
        assertThat(formCount, is(2L));

        assertThat(impl.findFormDescriptor(projectId, formId).isPresent(), is(true));

        impl.deleteFormDescriptor(projectId, formId);

        var formCountAfterDelete = impl.findFormDescriptors(projectId).count();
        assertThat(formCountAfterDelete, is(1L));
        assertThat(impl.findFormDescriptor(projectId, formId).isPresent(), is(false));
    }
}
