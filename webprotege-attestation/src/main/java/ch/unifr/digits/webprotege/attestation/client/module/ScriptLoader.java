package ch.unifr.digits.webprotege.attestation.client.module;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;

public class ScriptLoader {

    public void load(String script) {}

    public static class InjectingLoader extends ScriptLoader {
        @Override
        public void load(String script) {
            ScriptInjector
                    .fromUrl(GWT.getModuleBaseForStaticFiles() + "attestation/" + script)
                    .setCallback(
                        new Callback<Void, Exception>() {
                            public void onFailure(Exception reason) {
                                GWT.log("[attestation] Could not load script " + script);
                            }
                            public void onSuccess(Void result) {
                                GWT.log("[attestation] Script " + script + " loaded successfully.");
                            }
                        })
                    .setWindow(ScriptInjector.TOP_WINDOW)
                    .setRemoveTag(false)
                    .inject();
        }
    }

}
