package edu.stanford.bmir.protege.web.server.user;

import com.google.common.base.Objects;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 09/03/16
 *
 * Represents the information about a user that is stored in a DB.  This is not a data structure for
 * general purpose use.
 */
@Document(collection = "UserRecords")
@TypeAlias("UserRecord")
public class UserRecord {

    @Id
    @Indexed(unique = true)
    private final UserId userId;

    private final String realName;

    private final String emailAddress;

    private final String avatarUrl;

    private final Salt salt;

    private final SaltedPasswordDigest saltedPasswordDigest;

    /**
     * @param userId The userId that identifies this record in the DB
     * @param realName The real name of the user.  May be empty, but must not be {@code null}.
     * @param emailAddress The email address of the user.   May be empty, but must not be {@code null}.
     * @param avatarUrl The Avatar URL of the user.    May be empty, but must not be {@code null}.
     * @param salt The salt for the user.  Not {@code null}.
     * @param saltedPasswordDigest The salted password digest for the user. Not {@code null}.
     */
    public UserRecord(UserId userId,
                      String realName,
                      String emailAddress,
                      String avatarUrl,
                      Salt salt, SaltedPasswordDigest saltedPasswordDigest) {
        this.userId = checkNotNull(userId);
        this.realName = checkNotNull(realName);
        this.emailAddress = checkNotNull(emailAddress);
        this.avatarUrl = checkNotNull(avatarUrl);
        this.salt = checkNotNull(salt);
        this.saltedPasswordDigest = checkNotNull(saltedPasswordDigest);
    }

    /**
     * Gets the user Id.
     * @return The user Id.  Not {@code null}.
     */
    public UserId getUserId() {
        return userId;
    }

    public String getRealName() {
        return realName;
    }

    /**
     * Gets the email address.
     * @return The email address.  May be empty to indicate no specified address.  Not {@code null}.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Gets the avatar Url.
     * @return The avatar Url.  May be empty to indicate no specified Url.  Not {@code null}.
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * Gets the salt for this user.
     * @return The salt.  Not {@code null}.
     */
    public Salt getSalt() {
        return salt;
    }

    /**
     * Gets the salted password digest.
     * @return The salted password digest.  Not {@code null}.
     */
    public SaltedPasswordDigest getSaltedPasswordDigest() {
        return saltedPasswordDigest;
    }


    @Override
    public String toString() {
        return toStringHelper("UserRecord")
                .addValue(userId)
                .addValue(realName)
                .addValue(emailAddress)
                .addValue(avatarUrl)
                .addValue(salt)
                .addValue(saltedPasswordDigest)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
                userId,
                realName,
                emailAddress,
                avatarUrl,
                salt,
                saltedPasswordDigest
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UserRecord)) {
            return false;
        }
        UserRecord other = (UserRecord) obj;
        return this.userId.equals(other.userId)
                && this.realName.equals(other.realName)
                && this.emailAddress.equals(other.emailAddress)
                && this.avatarUrl.equals(other.avatarUrl)
                && this.salt.equals(other.salt)
                && this.saltedPasswordDigest.equals(other.saltedPasswordDigest);
    }
}
