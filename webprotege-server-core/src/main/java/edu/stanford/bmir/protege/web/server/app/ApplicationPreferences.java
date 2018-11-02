package edu.stanford.bmir.protege.web.server.app;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.app.ApplicationLocation;
import org.mongodb.morphia.annotations.AlsoLoad;
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
 * An object that holds application preferences that are stored in the database.
 */
@Entity(cap = @CappedAt(count = 1L), noClassnameStored = true)
public class ApplicationPreferences {

    public static final String ID = "Preferences";

    @Id
    @SuppressWarnings("unused")
    private String id = ID;

    private String applicationName;

    @AlsoLoad("adminEmailAddress")
    private String systemNotificationEmailAddress;

    private ApplicationLocation applicationLocation;

    private long maxUploadSize;

    // For Morphia
    private ApplicationPreferences() {
    }

    public ApplicationPreferences(@Nonnull String applicationName,
                                  @Nonnull String systemNotificationEmailAddress,
                                  @Nonnull ApplicationLocation applicationLocation,
                                  long maxUploadSize) {
        this.applicationName = checkNotNull(applicationName);
        this.systemNotificationEmailAddress = checkNotNull(systemNotificationEmailAddress);
        this.applicationLocation = checkNotNull(applicationLocation);
        this.maxUploadSize = maxUploadSize;
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
     * Gets the administrator's email address.
     * @return A string representing the admins email address.  May be empty.
     */
    @Nonnull
    public String getSystemNotificationEmailAddress() {
        return systemNotificationEmailAddress;
    }

    /**
     * Get the application location.
     * @return The location of the application.  This is used in links in notification emails.
     */
    @Nonnull
    public ApplicationLocation getApplicationLocation() {
        return applicationLocation;
    }

    /**
     * Gets the maximum file upload size.
     * @return The maximum file upload size.
     */
    public long getMaxUploadSize() {
        return maxUploadSize;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(applicationName,
                systemNotificationEmailAddress,
                                applicationLocation,
                                maxUploadSize);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ApplicationPreferences)) {
            return false;
        }
        ApplicationPreferences other = (ApplicationPreferences) obj;
        return this.applicationName.equals(other.applicationName)
                && this.systemNotificationEmailAddress.equals(other.systemNotificationEmailAddress)
                && this.applicationLocation.equals(other.applicationLocation)
                && this.maxUploadSize == other.maxUploadSize;
    }


    @Override
    public String toString() {
        return toStringHelper("ApplicationPreferences" )
                .addValue(applicationName)
                .add("systemNotificationEmail", systemNotificationEmailAddress)
                .addValue(applicationLocation)
                .toString();
    }
}
