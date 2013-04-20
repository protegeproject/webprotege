/**
 *
 */
package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.data.UserData;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.smi.protege.server.metaproject.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class contains methods to authenticate user securely with Hashing using MD5
 *
 * @author z.khan
 *
 */
public class AuthenticationUtil {

    public boolean verifyChallengedHash(String storedHashedPswd, String response, String challenge) {
        if (storedHashedPswd == null) {
            return false;
        }

        AuthenticationUtil authenticatinUtil = new AuthenticationUtil();
        String challengedStoredPass = authenticatinUtil.makeDigestAddChallenge(storedHashedPswd, challenge);
        return response.equals(challengedStoredPass);
    }

    public String makeDigestAddChallenge(String hashedSaltedPassword, String challenge) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException("Did not have MD5 algorithm");
        }
        messageDigest.update(challenge.getBytes());
        messageDigest.update(hashedSaltedPassword.getBytes());
        String digest = encodeBytes(messageDigest.digest());
        return digest;
    }

    private String encodeBytes(byte[] bytes) {
        int stringLength = 2 * bytes.length;
        BigInteger bi = new BigInteger(1, bytes);
        String encoded = bi.toString(16);
        while (encoded.length() < stringLength) {
            encoded = "0" + encoded;
        }
        return encoded;
    }

    public static UserData createUserData(UserId userId) {
        UserData userData = new UserData(userId);
//        fillInGoups(userData, userId);
        fillInEmail(userData);
        return userData;
    }

//    public static void fillInGoups(UserData userData, UserId userId) {
//        User user = MetaProjectManager.getManager().getMetaProject().getUser(userId.getUserName());
//        if (user == null) {
//            return;
//        }
////        fillInGoups(userData, user);
//    }

//    public static void fillInGoups(UserData userData, User user) {
//        Set<Group> groups = user.getGroups();
//        Collection<String> groupNames = new HashSet<String>();
//        for (Group group : groups) {
//            groupNames.add(group.getName());
//        }
//        userData.setGroups(groupNames);
//    }

    public static void fillInEmail(UserData userData) {
        final UserId userId = userData.getUserId();
        if (userId.isGuest()) {
            return;
        }
        User user = MetaProjectManager.getManager().getMetaProject().getUser(userId.getUserName());
        if (user == null) {
            return;
        }
        userData.setEmail(user.getEmail());
    }


}
