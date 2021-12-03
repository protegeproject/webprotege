package ch.unifr.digits.webprotege.attestation.client.jszip;

import com.google.gwt.core.client.JsDate;
import elemental2.promise.Promise;
import jsinterop.annotations.*;

import javax.annotation.Nullable;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class ZipObject {
    @JsProperty
    public String name;
    @JsProperty
    public boolean dir;
    @JsProperty
    public JsDate date;
    @JsProperty
    public String comment;
    @JsProperty
    public short unixPermissions;
    @JsProperty
    public short dosPermissions;
    @JsProperty
    public Object options;

    @JsMethod
    public native Promise<?> async(String type, @Nullable OnUpdateCallback callback);

    @FunctionalInterface
    @JsFunction
    public interface OnUpdateCallback {

        void update(Metadata metadata);

        @JsType(isNative = true, namespace = JsPackage.GLOBAL)
        class Metadata {
            @JsProperty
            public double percent;
        }
    }
}
