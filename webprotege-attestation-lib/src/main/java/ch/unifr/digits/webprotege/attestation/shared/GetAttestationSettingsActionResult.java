package ch.unifr.digits.webprotege.attestation.shared;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

public class GetAttestationSettingsActionResult implements Result {

    private String addressChangeContract;
    private String addressOntologyContract;

    public GetAttestationSettingsActionResult() {}

    public GetAttestationSettingsActionResult(String addressChangeContract, String addressOntologyContract) {
        this.addressChangeContract = addressChangeContract;
        this.addressOntologyContract = addressOntologyContract;
    }

    public String getAddressChangeContract() {
        return addressChangeContract;
    }

    public String getAddressOntologyContract() {
        return addressOntologyContract;
    }
}
