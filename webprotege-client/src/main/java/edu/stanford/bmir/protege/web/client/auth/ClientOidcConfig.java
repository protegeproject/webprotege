package edu.stanford.bmir.protege.web.client.auth;

import com.google.gwt.user.client.Window;

/**
 * Reads OIDC bootstrap values set in {@code WebProtege.jsp} from environment-driven server configuration.
 */
public final class ClientOidcConfig {

    private ClientOidcConfig() {
    }

    public static boolean isOidcSsoEnabled() {
        return readSsoEnabled();
    }

    public static boolean isOidcHideLocalLogin() {
        return readHideLocalLogin();
    }

    private static native boolean readSsoEnabled() /*-{
        return !!$wnd.webprotegeOidcSsoEnabled;
    }-*/;

    private static native boolean readHideLocalLogin() /*-{
        return !!$wnd.webprotegeOidcHideLocalLogin;
    }-*/;

    public static String getOidcLoginUrl() {
        String url = readLoginUrl();
        if (url == null || url.isEmpty()) {
            return fallbackLoginUrl();
        }
        return url;
    }

    private static String fallbackLoginUrl() {
        String path = Window.Location.getPath();
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        if (path.endsWith("/WebProtege.jsp")) {
            path = path.substring(0, path.length() - "/WebProtege.jsp".length());
        }
        return path + "/webprotege/oidc/login";
    }

    private static native String readLoginUrl() /*-{
        var u = $wnd.webprotegeOidcLoginUrl;
        return u == null ? null : String(u);
    }-*/;
}
