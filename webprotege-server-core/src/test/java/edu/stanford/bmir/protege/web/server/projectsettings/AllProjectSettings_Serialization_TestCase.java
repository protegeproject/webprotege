package edu.stanford.bmir.protege.web.server.projectsettings;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.color.Color;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitPrefixSettings;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.crud.gen.GeneratedAnnotationsSettings;
import edu.stanford.bmir.protege.web.shared.crud.uuid.UuidSuffixSettings;
import edu.stanford.bmir.protege.web.shared.lang.DisplayNameSettings;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityIsDeprecatedCriteria;
import edu.stanford.bmir.protege.web.shared.project.PrefixDeclaration;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.projectsettings.*;
import edu.stanford.bmir.protege.web.shared.sharing.PersonId;
import edu.stanford.bmir.protege.web.shared.sharing.ProjectSharingSettings;
import edu.stanford.bmir.protege.web.shared.sharing.SharingPermission;
import edu.stanford.bmir.protege.web.shared.sharing.SharingSetting;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.tag.Tag;
import edu.stanford.bmir.protege.web.shared.tag.TagId;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-24
 */
public class AllProjectSettings_Serialization_TestCase {

    @Test
    public void shouldRoundTripSettings() throws IOException {
        var projectId = ProjectId.getNil();
        var projectSettings = ProjectSettings.get(projectId,
                                                  "My project",
                                                  "My project description",
                                                  DictionaryLanguage.rdfsLabel("fr"),
                                                  DisplayNameSettings.get(ImmutableList.of(), ImmutableList.of()),
                                                  SlackIntegrationSettings.get("http://payloadurl"),
                                                  WebhookSettings.get(ImmutableList.of()),
                                                  EntityDeprecationSettings.empty());
        var creationSettings = EntityCrudKitSettings.get(EntityCrudKitPrefixSettings.get(), UuidSuffixSettings.get(),
                                                         GeneratedAnnotationsSettings.empty());
        var prefixDeclarations = ImmutableList.of(PrefixDeclaration.get("ex:", "http://example.org/hello/"));
        var tags = ImmutableList.<Tag>of(Tag.get(TagId.createTagId(),
                                                 projectId,
                                                 "My Tag",
                                                 "My tag description",
                                                 Color.getWhite(),
                                                 Color.getWhite(),
                                                 ImmutableList.of(EntityIsDeprecatedCriteria.get())));
        var sharingSettings = new ProjectSharingSettings(
                projectId,
                Optional.empty(),
                ImmutableList.of(
                        new SharingSetting(PersonId.get("Someone"),
                                           SharingPermission.EDIT)
                )
        );
        var settings = AllProjectSettings.get(projectSettings, creationSettings, prefixDeclarations, tags, sharingSettings);
        JsonSerializationTestUtil.testSerialization(settings, AllProjectSettings.class);
    }
}
