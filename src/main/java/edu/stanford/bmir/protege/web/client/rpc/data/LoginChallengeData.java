package edu.stanford.bmir.protege.web.client.rpc.data;

import java.io.Serializable;

/**
 * @author z.khan
 */
public class LoginChallengeData implements Serializable {
    private static final long serialVersionUID = -5707567593750239138L;
    
    private String salt;
	private String challenge;
	
	public LoginChallengeData() {
	}
	
	public LoginChallengeData(String salt,String challenge) {
	    this.salt = salt;
	    this.challenge = challenge;
	}

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }
	
}
