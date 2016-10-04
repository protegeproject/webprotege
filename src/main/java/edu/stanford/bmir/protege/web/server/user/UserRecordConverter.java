package edu.stanford.bmir.protege.web.server.user;

import com.mongodb.client.model.Filters;
import edu.stanford.bmir.protege.web.server.persistence.*;
import edu.stanford.bmir.protege.web.shared.auth.Salt;
import edu.stanford.bmir.protege.web.shared.auth.SaltedPasswordDigest;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.bson.Document;
import org.bson.types.Binary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.mongodb.client.model.Filters.regex;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30 Sep 2016
 */
public class UserRecordConverter implements DocumentConverter<UserRecord> {

    private static final String USER_ID = "_id";

    private static final String REAL_NAME = "realName";

    private static final String EMAIL_ADDRESS = "emailAddress";

    private static final String AVATAR_URL = "avatar";

    private static final String SALT = "salt";

    private static final String SALTED_PASSWORD_DIGEST = "saltedPasswordDigest";

    @Inject
    public UserRecordConverter() {
    }

    @Override
    public Document toDocument(@Nonnull UserRecord object) {
        Document document = new Document();
        document.append(USER_ID, object.getUserId().getUserName());
        document.append(REAL_NAME, object.getRealName());
        document.append(EMAIL_ADDRESS, object.getEmailAddress());
        if (!object.getAvatarUrl().isEmpty()) {
            document.append(AVATAR_URL, object.getAvatarUrl());
        }
        document.append(SALT, new SaltWriteConverter().convert(object.getSalt()));
        document.append(SALTED_PASSWORD_DIGEST, new SaltedPasswordDigestWriteConverter().convert(object.getSaltedPasswordDigest()));
        return document;
    }

    @Override
    public UserRecord fromDocument(@Nonnull Document document) {
        String userId = document.getString(USER_ID);
        String realName = document.getString(REAL_NAME);
        String email = orEmptyString(document.getString(EMAIL_ADDRESS));
        String avatar = orEmptyString(document.getString(AVATAR_URL));
        Salt salt = new SaltReadConverter().convert(document.getString(SALT));
        SaltedPasswordDigest password = new SaltedPasswordDigestReadConverter().convert(document.getString(SALTED_PASSWORD_DIGEST));
        return new UserRecord(
                UserId.getUserId(userId),
                realName,
                email,
                avatar,
                salt,
                password);
    }

    public UserId getUserId(Document document) {
        return UserId.getUserId(document.getString(USER_ID));
    }

    @Nonnull
    private static String orEmptyString(@Nullable String s) {
        if (s == null) {
            return "";
        }
        else {
            return s;
        }
    }

    public static Document byUserId(@Nonnull UserId userId) {
        return new Document(USER_ID, userId.getUserName());
    }

    public static Document byEmailAddress(@Nonnull String emailAddress) {
        return new Document(EMAIL_ADDRESS, emailAddress);
    }

    public static Document byUserIdContainsIgnoreCase(@Nonnull String regex) {
        return new Document(USER_ID, new Document("$regex" , regex).append("$options" , "i" ));
    }

    public static Document withUserId() {
        return new Document(USER_ID, 1);
    }

}
