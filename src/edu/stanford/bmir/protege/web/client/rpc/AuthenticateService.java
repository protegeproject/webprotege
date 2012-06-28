package edu.stanford.bmir.protege.web.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import edu.stanford.bmir.protege.web.client.rpc.data.UserData;

/**
 *  
 *
 * @author z.khan
 */
 @RemoteServiceRelativePath("authenticate/authen")

public interface AuthenticateService extends RemoteService {
    
    //related to webprotege account login
    UserData validateUserAndAddInSession(String name, String password);
    
    UserData validateUser(String name, String password);
    
    void changePassword(String userName, String password);
    
    UserData registerUserToAssociateOpenId(String name, String password, String emailId);
    
    UserData validateUserToAssociateOpenId(String name, String password);
    
    void sendPasswordReminder(String userName);
    
    UserData registerUser(String name, String password);
}
