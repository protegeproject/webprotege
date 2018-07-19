package edu.stanford.bmir.protege.web.shared.project;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.gwt.core.shared.GwtIncompatible;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.user.UserId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.time.Instant;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 01/04/2013
 * <p>
 * Instances of this class record the main details about a project.
 * </p>
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class ProjectDetails implements Serializable, Comparable<ProjectDetails>, HasProjectId {


    private static final String PROJECT_ID = "_id";

    private static final String DISPLAY_NAME = "displayName";

    private static final String DESCRIPTION = "description";

    private static final String OWNER = "owner";

    private static final String IN_TRASH = "inTrash";

    private static final String CREATED_AT = "createdAt";

    private static final String CREATED_BY = "createdBy";

    private static final String MODIFIED_AT = "modifiedAt";

    private static final String MODIFIED_BY = "modifiedBy";

    private static final String DEFAULT_DICTIONARY_LANGUAGE = "defaultDictionaryLanguage";

    /**
     * Constructs a {@link ProjectDetails} object.
     *
     * @param projectId      The {@link ProjectId} that identifies the project which these details describe.
     * @param displayName    The human readable name for the project.  Not {@code null}.
     * @param owner          The owner of the project. Not {@code null}.
     * @param description    A description of the project.  Not {@code null}. May be empty.
     * @param inTrash        A flag that specifies whether the project is in the trash.
     * @param createdAt      A timestamp that specifies when the project was created.  A zero value stands for unknown.
     * @param createdBy      A {@link UserId} that identifies the user that created the project.
     * @param lastModifiedAt A timestamp that specifies when the project was last modified.  A zero value indicates
     *                       unknown.
     * @param lastModifiedBy A {@link UserId} that identifies the user that last modified the project.
     * @throws NullPointerException if any parameters are {@code null}.
     */
    public static ProjectDetails get(@Nonnull ProjectId projectId,
                                     @Nonnull String displayName,
                                     @Nonnull String description,
                                     @Nonnull UserId owner,
                                     boolean inTrash,
                                     @Nullable DictionaryLanguage dictionaryLanguage,
                                     long createdAt,
                                     @Nonnull UserId createdBy,
                                     long lastModifiedAt,
                                     @Nonnull UserId lastModifiedBy) {
        Builder builder = builder(projectId, displayName, owner)
                .setInTrash(inTrash)
                .setCreatedAt(createdAt)
                .setCreatedBy(createdBy)
                .setLastModifiedAt(lastModifiedAt)
                .setLastModifiedBy(lastModifiedBy);
        if (dictionaryLanguage != null) {
            builder.setDefaultDictionaryLanguage(dictionaryLanguage);
        }
        return builder.build();
    }

    @JsonCreator
    @GwtIncompatible
    public static ProjectDetails valueOf(@Nonnull @JsonProperty(PROJECT_ID) ProjectId projectId,
                                         @Nonnull @JsonProperty(DISPLAY_NAME) String displayName,
                                         @Nullable @JsonProperty(DESCRIPTION) String description,
                                         @Nonnull @JsonProperty(OWNER) UserId owner,
                                         @JsonProperty(IN_TRASH) boolean inTrash,
                                         @Nullable @JsonProperty(DEFAULT_DICTIONARY_LANGUAGE) DictionaryLanguage dictionaryLanguage,
                                         @JsonProperty(CREATED_AT) Instant createdAt,
                                         @JsonProperty(CREATED_BY) @Nonnull UserId createdBy,
                                         @JsonProperty(MODIFIED_AT) Instant lastModifiedAt,
                                         @JsonProperty(MODIFIED_BY) @Nonnull UserId lastModifiedBy) {
        Builder builder = builder(projectId, displayName, owner)
                .setInTrash(inTrash)
                .setCreatedAt(createdAt.toEpochMilli())
                .setCreatedBy(createdBy)
                .setLastModifiedAt(lastModifiedAt.toEpochMilli())
                .setLastModifiedBy(lastModifiedBy);
        if(description == null) {
            builder.setDescription("");
        }
        else {
            builder.setDescription(description);
        }
        if (dictionaryLanguage != null) {
            builder.setDefaultDictionaryLanguage(dictionaryLanguage);
        }
        return builder.build();
    }

    @Nonnull
    public static Builder builder(@Nonnull ProjectId projectId,
                                  @Nonnull String displayName,
                                  @Nonnull UserId owner) {
        return new AutoValue_ProjectDetails.Builder()
                .setProjectId(projectId)
                .setDisplayName(displayName)
                .setOwner(owner)
                .setInTrash(false)
                .setDefaultDictionaryLanguage(DictionaryLanguage.rdfsLabel(""));
    }

    /**
     * Gets the {@link ProjectId} of the project that these details describe.
     *
     * @return The {@link ProjectId}.  Not {@code null}.
     */
    @JsonProperty(PROJECT_ID)
    @Nonnull
    public abstract ProjectId getProjectId();

    /**
     * Gets the human readable name for the project.
     *
     * @return The human readable name.  Not {@code null}.
     */
    @JsonProperty(DISPLAY_NAME)
    @Nonnull
    public abstract String getDisplayName();

    /**
     * Gets the description of the project described by these details.
     *
     * @return The description as a string.  Not {@code null}.  May be empty.
     */
    @JsonProperty(DESCRIPTION)
    @Nonnull
    public abstract String getDescription();

    /**
     * Gets the {@link UserId} that identifies the owner of the project.
     *
     * @return The {@link UserId} for the project described by these details.  Not {@code null}.
     */
    @JsonProperty(OWNER)
    @Nonnull
    public abstract UserId getOwner();

    /**
     * Determines if this project is in the trash.
     *
     * @return {@code true} if this project is in the trash, otherwise {@code false}.
     */
    @JsonProperty(IN_TRASH)
    public abstract boolean isInTrash();

    /**
     * Gets the default dictionary language for this project
     */
    @JsonProperty(DEFAULT_DICTIONARY_LANGUAGE)
    @Nonnull
    public abstract DictionaryLanguage getDefaultDictionaryLanguage();

    /**
     * Gets the timestamp of when the project was created.
     *
     * @return A timestamp.  A value of zero denotes unknown.
     */
    @JsonIgnore
    public abstract long getCreatedAt();

    @JsonProperty(CREATED_AT)
    @GwtIncompatible
    protected Instant createdAt() {
        return Instant.ofEpochMilli(getCreatedAt());
    }


    /**
     * Gets the user who created the project.
     *
     * @return A {@link UserId} of the user who created the project.
     */
    @JsonProperty(CREATED_BY)
    @Nonnull
    public abstract UserId getCreatedBy();

    /**
     * Gets the timestamp of when the project was modified.
     *
     * @return A timestamp.  A value of zero denotes unknown.
     */
    @JsonIgnore
    public abstract long getLastModifiedAt();

    @JsonProperty(MODIFIED_AT)
    @GwtIncompatible
    protected Instant modifiedAt() {
        return Instant.ofEpochMilli(getLastModifiedAt());
    }

    /**
     * Gets the user who last modified the project.
     *
     * @return A {@link UserId} of the user who last modified the project.
     */
    @Nonnull
    @JsonProperty(MODIFIED_BY)
    public abstract UserId getLastModifiedBy();

    @Override
    public int compareTo(ProjectDetails o) {
        final int dispNameDiff = getDisplayName().compareToIgnoreCase(o.getDisplayName());
        if (dispNameDiff != 0) {
            return dispNameDiff;
        }
        final int caseSensitiveDiff = getDisplayName().compareTo(o.getDisplayName());
        if (caseSensitiveDiff != 0) {
            return caseSensitiveDiff;
        }
        int ownerDiff = getOwner().compareTo(o.getOwner());
        if (ownerDiff != 0) {
            return ownerDiff;
        }
        int descriptionDiff = getDescription().compareTo(o.getDescription());
        if (descriptionDiff != 0) {
            return descriptionDiff;
        }
        return getProjectId().getId().compareTo(o.getProjectId().getId());
    }

    public abstract Builder toBuilder();

    @SuppressWarnings("NullableProblems")
    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setProjectId(@Nonnull ProjectId projectId);

        public abstract Builder setDisplayName(@Nonnull String displayName);

        public abstract Builder setDescription(@Nonnull String description);

        public abstract Builder setOwner(@Nonnull UserId owner);

        public abstract Builder setInTrash(boolean inTrash);

        public abstract Builder setDefaultDictionaryLanguage(@Nonnull DictionaryLanguage defaulDictionaryLanguage);

        public abstract Builder setCreatedAt(long createdAt);

        public abstract Builder setCreatedBy(@Nonnull UserId userId);

        public abstract Builder setLastModifiedAt(long modifiedAt);

        public abstract Builder setLastModifiedBy(@Nonnull UserId userId);

        public abstract ProjectDetails build();

    }
}
