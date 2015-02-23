package edu.stanford.bmir.protege.web.server;

import com.google.common.base.Optional;

import javax.servlet.http.HttpSession;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Class to store the keys that we use to store data in the session.
 */
public class SessionConstants {

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
