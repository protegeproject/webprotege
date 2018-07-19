package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.bson.Document;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jul 2018
 */
public class ProjectDetails_Serialization_TestCase {

    private ProjectDetails projectDetails;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        projectDetails = ProjectDetails.get(ProjectId.get("12345678-1234-1234-1234-123456789abc"),
                                            "The display name",
                                            "The description",
                                            UserId.getUserId("The Owner"),
                                            true,
                                            DictionaryLanguage.rdfsLabel("en-GB"),
                                            2L,
                                            UserId.getUserId("The creator"),
                                            3L,
                                            UserId.getUserId("The modifier"));
        ObjectMapperProvider objectMapperProvider = new ObjectMapperProvider();
        objectMapper = objectMapperProvider.get();
    }

    @Test
    public void shouldSerializeToJson() throws IOException {
        String val = objectMapper.writeValueAsString(projectDetails);
        System.out.println(val);
        ProjectDetails readProjectDetails = objectMapper.readValue(val, ProjectDetails.class);
        assertThat(readProjectDetails, is(projectDetails));
    }
}
