package edu.stanford.bmir.protege.web.shared.app;

import com.google.common.base.Objects;
import org.mongodb.morphia.annotations.CappedAt;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import javax.annotation.Nonnull;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Mar 2017
 *
 * An object that holds application settings.
 */
@Entity(cap = @CappedAt(count = 1L))
public class ApplicationSettings {

    public static final String ID = "ApplicationSettings";

    @Id
    @SuppressWarnings("unused")
    private final String id = ID;

    @Nonnull
    private final String applicationName;

    @Nonnull
    private final String customLogoUrl;

    @Nonnull
    private final String adminEmailAddress;

    @Nonnull
    private final ApplicationLocation applicationLocation;

    public ApplicationSettings(@Nonnull String applicationName,
                               @Nonnull String customLogoUrl,
                               @Nonnull String adminEmailAddress,
                               @Nonnull ApplicationLocation applicationLocation) {
        this.applicationName = checkNotNull(applicationName);
        this.customLogoUrl = checkNotNull(customLogoUrl);
        this.adminEmailAddress = checkNotNull(adminEmailAddress);
        this.applicationLocation = checkNotNull(applicationLocation);
    }

    /**
     * Gets the application name.
     * @return A string representing the application name.
     */
    @Nonnull
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Gets a URL that points to an image to be used for the application logo.
     * @return A string representing the URL.  May be empty, in which case, the default
     * WebProtege logo will be used.
     */
    @Nonnull
    public String getCustomLogoUrl() {
        return customLogoUrl;
    }

    /**
     * Gets the administrator's email address.
     * @return A string representing the admins email address.  May be empty.
     */
    @Nonnull
    public String getAdminEmailAddress() {
        return adminEmailAddress;
    }

    /**
     * Get the application location.
     * @return The location of the application.  This is used in links in notification emails.
     */
    @Nonnull
    public ApplicationLocation getApplicationLocation() {
        return applicationLocation;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(applicationName,
                                customLogoUrl,
                                adminEmailAddress,
                                applicationLocation);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ApplicationSettings)) {
            return false;
        }
        ApplicationSettings other = (ApplicationSettings) obj;
        return this.applicationName.equals(other.applicationName)
                && this.customLogoUrl.equals(other.customLogoUrl)
                && this.adminEmailAddress.equals(other.adminEmailAddress)
                && this.applicationLocation.equals(other.applicationLocation);
    }


    @Override
    public String toString() {
        return toStringHelper("ApplicationSettings" )
                .addValue(applicationName)
                .add("logo", customLogoUrl)
                .add("adminEmail", adminEmailAddress)
                .addValue(applicationLocation)
                .toString();
    }
}
