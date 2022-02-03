package org.artisoft.domain.ModToolbox.project;

import org.artisoft.domain.ValueLabel;

public class SignerInfo {
    private ValueLabel<Long, String> signer;
    private String signature;

    public ValueLabel<Long, String> getSigner() {
        return signer;
    }

    public void setSigner(ValueLabel<Long, String> signer) {
        this.signer = signer;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
