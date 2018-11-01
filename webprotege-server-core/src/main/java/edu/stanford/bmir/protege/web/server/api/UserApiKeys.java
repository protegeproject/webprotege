package edu.stanford.bmir.protege.web.server.api;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.mongodb.morphia.annotations.*;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.api.UserApiKeys.API_KEYS__API_KEY;
import static edu.stanford.bmir.protege.web.server.api.UserApiKeys.API_KEYS__API_KEY_ID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Apr 2018
 */
@Entity(noClassnameStored = true)
@Indexes(
        {
                @Index(fields = @Field(API_KEYS__API_KEY_ID), options = @IndexOptions(unique = true)),
                @Index(fields = @Field(API_KEYS__API_KEY), options = @IndexOptions(unique = true))
        }
)
public class UserApiKeys {

    public static final String USER_ID = "_id";

    public static final String API_KEYS = "apiKeys";

    public static final String API_KEYS__API_KEY_ID = API_KEYS + ".apiKeyId";

    public static final String API_KEYS__API_KEY = API_KEYS + ".apiKey";

    @Id
    private UserId userId;

    private List<ApiKeyRecord> apiKeys;

    // For Morphia
    private UserApiKeys() {
    }

    public UserApiKeys(@Nonnull UserId userId, @Nonnull List<ApiKeyRecord> apiKeys) {
        this.userId = checkNotNull(userId);
        this.apiKeys = ImmutableList.copyOf(apiKeys);
    }

    @Nonnull
    public UserId getUserId() {
        return userId;
    }

    @Nonnull
    public List<ApiKeyRecord> getApiKeys() {
        return ImmutableList.copyOf(apiKeys);
    }
}
