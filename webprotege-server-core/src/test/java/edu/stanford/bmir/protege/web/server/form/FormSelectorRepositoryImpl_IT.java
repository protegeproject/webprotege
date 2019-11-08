package edu.stanford.bmir.protege.web.server.form;

import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.form.FormId;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityIsDeprecatedCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-08
 */
public class FormSelectorRepositoryImpl_IT {

    private FormTriggerRepositoryImpl repository;

    private MongoClient client;

    @Before
    public void setUp() {
        client = MongoTestUtils.createMongoClient();
        var database = client.getDatabase(MongoTestUtils.getTestDbName());
        var objectMapper = new ObjectMapperProvider().get();
        repository = new FormTriggerRepositoryImpl(database, objectMapper);
    }

    @Test
    public void shouldSaveFormTrigger() {
        var projectId = ProjectId.get("12345678-1234-1234-1234-123456789abc");
        var criteria = EntityIsDeprecatedCriteria.get();
        var theFormId = FormId.get("TheFormId");
        var formTrigger = FormSelector.get(projectId,
                                           criteria,
                                           theFormId);
        repository.save(formTrigger);
        var deserializedFormTrigger = repository.findFormTriggers(projectId).findFirst().orElseThrow();
        assertThat(deserializedFormTrigger, is(formTrigger));
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }
}
