package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.stanford.bmir.protege.web.client.rpc.data.OpenIdData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;

/**
 * A service for creating , associating and maintaining Open Id
 *
 * @author z.khan
 */
@RemoteServiceRelativePath("openid")
public interface OpenIdService extends RemoteService {

    /**
     * It associated Protege Account with the User's OpenId
     *
     * @param name
     * @param password
     * @return
     */
    UserData validateUserToAssocOpenIdWithEncrypt(String name, String password);

    UserData registerUserToAssocOpenIdWithEncrption(String name, String hashedPassword, String emailId);

    OpenIdData getUsersOpenId(String name);

    OpenIdData removeAssocToOpenId(String name, String openId);

    OpenIdData assocNewOpenIdToUser(String name);

    UserData isOpenIdInSessForAddNewOpenId();

    UserData checkIfOpenIdInSessionForLogin();

    void clearCreateUserToAssocOpenIdSessData();

    UserData checkIfUserCreatedToAssocOpenId();

    void clearAuthUserToAssocOpenIdSessData();

    UserData checkIfUserAuthenticatedToAssocOpenId();
}
