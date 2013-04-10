package edu.stanford.bmir.protege.web.shared.openid;

import org.semanticweb.owlapi.model.IRI;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 05/04/2013
 */
public class OpenIdAccountDetails implements Serializable {

    private String openIdProviderName;

    private IRI accountURL;

    private OpenId openId;

    private OpenIdAccountDetails() {
    }

    public OpenIdAccountDetails(OpenId openId, IRI accountURL, String openIdProviderName) {
        this.openId = checkNotNull(openId);
        this.accountURL = checkNotNull(accountURL);
        this.openIdProviderName = checkNotNull(openIdProviderName);
    }

    public OpenId getOpenId() {
        return openId;
    }

    public String getOpenIdProviderName() {
        return openIdProviderName;
    }

    public IRI getAccountURL() {
        return accountURL;
    }




    @Override
    public int hashCode() {
        return "OpenIdAccountDetails".hashCode() + openIdProviderName.hashCode() + accountURL.hashCode() + openId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof OpenIdAccountDetails)) {
            return false;
        }
        OpenIdAccountDetails other = (OpenIdAccountDetails) obj;
        return this.openIdProviderName.equals(other.openIdProviderName) && this.accountURL.equals(other.accountURL) && this.openId.equals(other.openId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OpenIdAccountDetails");
        sb.append("(");
        sb.append(openId);
        sb.append(" Provider(");
        sb.append(openIdProviderName);
        sb.append(") AccountURL(");
        sb.append(accountURL);
        sb.append(")");
        return sb.toString();
    }
}
