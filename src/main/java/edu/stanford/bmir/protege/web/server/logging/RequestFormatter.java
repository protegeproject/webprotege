package edu.stanford.bmir.protege.web.server.logging;

import javax.servlet.http.HttpServletRequest;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Apr 2017
 */
public class RequestFormatter {

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";

    public static String formatAddr(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader(X_FORWARDED_FOR);
        if(xForwardedForHeader == null) {
            return request.getRemoteAddr();
        }
        else {
            return xForwardedForHeader;
        }
    }
}
