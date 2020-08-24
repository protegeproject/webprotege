package edu.stanford.bmir.protege.web.shared.projectsettings;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.crud.EntityCrudKitSettings;
import edu.stanford.bmir.protege.web.shared.project.PrefixDeclaration;
import edu.stanford.bmir.protege.web.shared.tag.Tag;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-24
 */
@AutoValue
public abstract class AllProjectSettings {

    @Nonnull
    public static AllProjectSettings get(@Nonnull ProjectSettings projectSettings,
                                         @Nonnull EntityCrudKitSettings entityCrudKitSettings,
                                         @Nonnull ImmutableList<PrefixDeclaration> prefixDeclarations,
                                         @Nonnull ImmutableList<Tag> tags) {
        return new AutoValue_AllProjectSettings(projectSettings, entityCrudKitSettings, prefixDeclarations,
                                                tags);
    }

    @Nonnull
    @JsonUnwrapped
    public abstract ProjectSettings getProjectSettings();

    @Nonnull
    @JsonUnwrapped
    public abstract EntityCrudKitSettings<?> getEntityCreationSettings();

    @Nonnull
    public abstract ImmutableList<PrefixDeclaration> getPrefixDeclarations();

    @Nonnull
    public abstract ImmutableList<Tag> getTags();
}
