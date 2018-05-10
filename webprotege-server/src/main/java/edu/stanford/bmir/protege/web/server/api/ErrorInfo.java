package edu.stanford.bmir.protege.web.server.api;

import javax.ws.rs.core.Response;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 May 2018
 */
public class ErrorInfo {

    private final int code;

    private final String reason;

    private final String message;

    public ErrorInfo(Response.Status status, String message) {
        this.code = checkNotNull(status).getStatusCode();
        this.reason = status.getReasonPhrase();
        this.message = checkNotNull(message);
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    public String getMessage() {
        return message;
    }
}
