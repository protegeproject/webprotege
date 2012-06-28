package edu.stanford.bmir.protege.web.client.model.event;

public class LoginEvent implements SystemEvent {
    private String user;

    public LoginEvent(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

}
