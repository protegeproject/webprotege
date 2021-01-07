package edu.stanford.bmir.protege.web.client.ontology.attestation;

import ch.unifr.digits.webprotege.attestation.shared.VerifyResult;
import com.google.gwt.core.client.Callback;

public interface AttestationPresenter {
    void fileSign(String iri, String versionIri, Callback<Boolean, Object> callback);
    void fileVerify(String iri, String versionIri, Callback<VerifyResult, Object> callback);
    void owlSign(String iri, String versionIri, Callback<Boolean, Object>  callback);
    void owlVerify(String iri, String versionIri, Callback<VerifyResult, Object> callback);
    void changetrackingSign(String iri, String versionIri, Callback<Boolean, Object>  callback);
    void changetrackingVerify(String iri, String versionIri, Callback<VerifyResult, Object> callback);
}
