package edu.stanford.bmir.protege.web.server.projectsettings;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidSuffixSettings;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.project.PrefixDeclaration;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.AllProjectSettings;
import edu.stanford.bmir.protege.web.shared.projectsettings.ProjectSettings;
import edu.stanford.bmir.protege.web.shared.projectsettings.SlackIntegrationSettings;
import edu.stanford.bmir.protege.web.shared.projectsettings.WebhookSettings;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-24
 */
public class AllProjectSettings_Serialization_TestCase {

    @Test
    public void shouldRoundTripSettings() throws IOException {
        var projectSettings = ProjectSettings.get(ProjectId.get(UUID.randomUUID().toString()),
                                                              "My project",
                                                              "My project description",
                                                              DictionaryLanguage.rdfsLabel("fr"),
                                                              DisplayNameSettings.get(ImmutableList.of(),
                                                                                      ImmutableList.of()
                                                              ),
                                                              SlackIntegrationSettings.get("http://payloadurl"),
                                                              WebhookSettings.get(ImmutableList.of()));
        var creationSettings = EntityCrudKitSettings.get(
                EntityCrudKitPrefixSettings.get(), UuidSuffixSettings.get()
        );
        var prefixDeclarations = ImmutableList.of(PrefixDeclaration.get("ex:", "http://example.org/hello/"));
        var tags = ImmutableList.<Tag>of();
        var settings = AllProjectSettings.get(
                projectSettings,
                creationSettings,
                prefixDeclarations,
                tags
        );
        JsonSerializationTestUtil.testSerialization(settings, AllProjectSettings.class);
    }
}
