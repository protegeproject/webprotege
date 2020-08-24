package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.project.PrefixDeclaration;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.project.WithProjectId;
import edu.stanford.bmir.protege.web.shared.tag.Tag;

import javax.annotation.Nonnull;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-24
 */
@AutoValue
public abstract class AllProjectSettings implements WithProjectId<AllProjectSettings> {

    public static final String PROJECT_SETTINGS = "projectSettings";

    public static final String ENTITY_CREATION_SETTINGS = "entityCreationSettings";

    public static final String PREFIX_DECLARATIONS = "prefixDeclarations";

    public static final String PROJECT_TAGS = "projectTags";

    @JsonCreator
    @Nonnull
    public static AllProjectSettings get(@JsonProperty(PROJECT_SETTINGS) @Nonnull ProjectSettings projectSettings,
                                         @JsonProperty(ENTITY_CREATION_SETTINGS) @Nonnull EntityCrudKitSettings entityCrudKitSettings,
                                         @JsonProperty(PREFIX_DECLARATIONS) @Nonnull ImmutableList<PrefixDeclaration> prefixDeclarations,
                                         @JsonProperty(PROJECT_TAGS) @Nonnull ImmutableList<Tag> tags) {
        return new AutoValue_AllProjectSettings(projectSettings, entityCrudKitSettings, prefixDeclarations,
                                                tags);
    }

    @JsonProperty(PROJECT_SETTINGS)
    @Nonnull
    public abstract ProjectSettings getProjectSettings();

    @JsonProperty(ENTITY_CREATION_SETTINGS)
    @Nonnull
    public abstract EntityCrudKitSettings getEntityCreationSettings();

    @JsonProperty(PREFIX_DECLARATIONS)
    @Nonnull
    public abstract ImmutableList<PrefixDeclaration> getPrefixDeclarations();

    @JsonProperty(PROJECT_TAGS)
    @Nonnull
    public abstract ImmutableList<Tag> getProjectTags();

    @Override
    public AllProjectSettings withProjectId(@Nonnull ProjectId projectId) {
        return AllProjectSettings.get(
                getProjectSettings().withProjectId(projectId),
                getEntityCreationSettings(),
                getPrefixDeclarations(),
                getProjectTags().stream().map(t -> t.withProjectId(projectId)).collect(toImmutableList())
        );
    }
}
