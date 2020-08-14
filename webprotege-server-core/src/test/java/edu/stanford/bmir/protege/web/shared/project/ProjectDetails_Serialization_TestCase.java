package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Map;

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
                                            DisplayNameSettings.get(ImmutableList.of(DictionaryLanguage.rdfsLabel("en-GB"),
                                                                                     DictionaryLanguage.rdfsLabel("en"),
                                                                                     DictionaryLanguage.rdfsLabel("")),
                                                                    ImmutableList.of(DictionaryLanguage.rdfsLabel("de"))),
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

    @Test
    public void shouldDeserializeFromJsonWithMissingDescription() {
        ProjectDetails readProjectDetails = roundTripWithoutField(ProjectDetails.DESCRIPTION);
        assertThat(readProjectDetails, is(projectDetails.withDescription("")));
    }

    @Test
    public void shouldDeserializeFromJsonWithMissingDefaultLanguage() {
        ProjectDetails readProjectDetails = roundTripWithoutField(ProjectDetails.DEFAULT_LANGUAGE);
        DictionaryLanguage expectedDefaultLanguage = DictionaryLanguage.rdfsLabel("");
        assertThat(readProjectDetails, is(projectDetails.withDefaultLanguage(expectedDefaultLanguage)));
    }

    private ProjectDetails roundTripWithoutField(@Nonnull String fieldName) {
        Map document = objectMapper.convertValue(projectDetails, Map.class);
        document.remove(fieldName);
        return objectMapper.convertValue(document, ProjectDetails.class);
    }
}
