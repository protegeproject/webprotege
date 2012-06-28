/**
 *
 */
package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.stanford.bmir.protege.web.client.rpc.data.OpenIdData;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;

/**
 * @author z.khan
 *
 */
public interface OpenIdServiceAsync {

    /**
     *
     * @see edu.stanford.bmir.protege.web.client.rpc.OpenIdService#validateUserToAssocOpenIdWithEncrypt(java.lang.String,
     *      java.lang.String)
     */
    void validateUserToAssocOpenIdWithEncrypt(String name, String password, AsyncCallback<UserData> callback);

    void getUsersOpenId(String name, AsyncCallback<OpenIdData> callback);

    void removeAssocToOpenId(String name, String openId, AsyncCallback<OpenIdData> callback);

    void assocNewOpenIdToUser(String name, AsyncCallback<OpenIdData> callback);

    void isOpenIdInSessForAddNewOpenId(AsyncCallback<UserData> callback);

    void checkIfOpenIdInSessionForLogin(AsyncCallback<UserData> callback);

    void clearCreateUserToAssocOpenIdSessData(AsyncCallback<Void> callback);

    void checkIfUserCreatedToAssocOpenId(AsyncCallback<UserData> callback);

    void registerUserToAssocOpenIdWithEncrption(String name, String hashedPassword, String emailId,
            AsyncCallback<UserData> callback);

    void clearAuthUserToAssocOpenIdSessData(AsyncCallback<Void> callback);

    void checkIfUserAuthenticatedToAssocOpenId(AsyncCallback<UserData> callback);
}
