package ch.unifr.digits.webprotege.attestation.client.module;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class AttestationModule implements EntryPoint {

    /**
     * The entry point method, called automatically by loading a module that
     * declares an implementing class as an entry point.
     */
    @Override
    public void onModuleLoad() {
        ScriptLoader loader = GWT.create(ScriptLoader.class);
        loader.load("detect-provider.min.js");
        loader.load("jszip.min.js");
        loader.load("contracts.js");
    }
}
