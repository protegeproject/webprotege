package ch.unifr.digits.webprotege.attestation.shared;

import edu.stanford.bmir.protege.web.shared.dispatch.Result;

public class VerifyResult implements Result {
    private boolean valid;
    private String signer;
    private String signerName;
    private int timestamp;

    public VerifyResult() {}

    public VerifyResult(boolean valid, String signer, String signerName, int timestamp) {
        this.valid = valid;
        this.signer = signer;
        this.signerName = signerName;
        this.timestamp = timestamp;
    }

    public boolean isValid() {
        return valid;
    }

    public String getSigner() {
        return signer;
    }

    public String getSignerName() {
        return signerName;
    }

    public int getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "VerifyResult{" +
                "valid=" + valid +
                ", signer='" + signer + '\'' +
                ", signerName='" + signerName + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
