package edu.stanford.bmir.protege.web.server.bioportal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 09/10/2012
 */
@XmlRootElement(name = "userBean")
public class BioPortalUserInfoBean {

    @XmlElement(name = "id")
    private int id;
    
    @XmlElement(name = "username")
    private String userName;
    
    @XmlElement(name = "firstname")
    private String firstName;
    
    @XmlElement(name = "lastname")
    private String lastName;
    
    @XmlElement(name = "email")
    private String email;

    private BioPortalUserInfoBean() {

    }

    public BioPortalUserInfoBean(int id, String userName, String firstName, String lastName, String email) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return String.format("BioPortalUserInfoBean(id(%s) username(%s) firstname(%s) lastname(%s) email(%s))", id, userName, firstName, lastName, email);
    }
}
