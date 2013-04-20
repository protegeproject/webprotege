package edu.stanford.bmir.protege.web.server;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.bmir.protege.web.shared.openid.OpenIdAccountDetails;

import javax.servlet.http.HttpSession;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Class to store the keys that we use to store data in the session.
 */
public class SessionConstants {

    public static final SessionKey<UserId> USER_ID = new SessionKey<UserId>("user.name");

//    String userOpenId = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_URL);
//    String openIdAccName = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_ID);
//    String openIdProvider = (String) session.getAttribute(OpenIdConstants.HTTPSESSION_OPENID_PROVIDER);
//    String salt = (String) session.getAttribute(AuthenticationConstants.NEW_SALT);

    //Related to values stored in Http Session
//    public static final String HTTPSESSION_OPENID_URL = "userOpenId";
//
//    public static final String HTTPSESSION_OPENID_ID = "openId.id";// Now email associated with Openid
//
//    public static final String HTTPSESSION_OPENID_PROVIDER = "openId.provider";// Open id provider name

//

    public static final SessionKey<OpenIdAccountDetails> OPEN_ID_ACCOUNT = new SessionKey<OpenIdAccountDetails>("openId.account");

    @SuppressWarnings("unchecked")
    public static <T> Optional<T> getAttribute(SessionKey<T> key, HttpSession session) {
        Object object = session.getAttribute(key.keyName);
        if(object == null) {
            return Optional.absent();
        }
        else {
            return Optional.of((T) object);
        }
    }

    public static void removeAttribute(SessionKey<?> key, HttpSession session) {
        session.removeAttribute(key.keyName);
    }


    public static <T> void setAttribute(SessionKey<T> key, T value, HttpSession session) {
        checkNotNull(session);
        session.setAttribute(checkNotNull(key.keyName), checkNotNull(value));
    }

    /**
     * Gets the {@link UserId} of the user in the specified {@link HttpSession}.
     * @param session The session.  Not {@code null}.
     * @return The {@link UserId} of the user in the specified user session.  Not {@code null}.  If the user in the
     * session is not logged in then the id of the guest user will be returned i.e. {@link edu.stanford.bmir.protege.web.shared.user.UserId#getGuest()}.
     */
    public static UserId getUserId(HttpSession session) {
        Optional<UserId> userId = getAttribute(USER_ID, session);
        if(userId.isPresent()) {
            return userId.get();
        }
        else {
            return UserId.getGuest();
        }
    }


    public static class SessionKey<C> {

        private String keyName;

        private SessionKey(String keyName) {
            this.keyName = keyName;
        }

        @Override
        public int hashCode() {
            return "SessionConstants$SessionKey".hashCode() + keyName.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == this) {
                return true;
            }
            if(!(obj instanceof SessionKey)) {
                return false;
            }
            SessionKey other = (SessionKey) obj;
            return this.keyName.equals(other.keyName);
        }
    }




}
