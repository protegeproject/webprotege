package edu.stanford.bmir.protege.web.server.api;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 May 2018
 */
public class ResponseUtil<T> {

    private final Map<String, String> links;

    @Nonnull
    private final T value;

    public ResponseUtil(@Nonnull T value) {
        this.links = new LinkedHashMap<>();
        this.value = checkNotNull(value);
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void addLink(@Nonnull String rel, @Nonnull URI uri) {
        links.put(checkNotNull(rel), checkNotNull(uri).toString());
    }

    public void addLink(@Nonnull String rel, @Nonnull String path) {
        links.put(checkNotNull(rel), checkNotNull(path));
    }

    @JsonUnwrapped
    @Nonnull
    public T getValue() {
        return value;
    }

    public Response toResponse(Response.Status status) {
        Response.ResponseBuilder builder = Response.status(status)
                                                .entity(this)
                                                .type(MediaType.APPLICATION_JSON_TYPE);
        links.forEach((rel, uri) -> builder.link(uri, rel));
        return builder.build();
    }

    public Response ok() {
        return toResponse(Response.Status.OK);
    }

    public static Response badRequest(@Nonnull String message) {
        Response.Status badRequest = Response.Status.BAD_REQUEST;
        ErrorInfo errorInfo = new ErrorInfo(badRequest, message);
        return new ResponseUtil<>(errorInfo).toResponse(badRequest);
    }

    public static Response forbidden(@Nonnull String message) {
        return new ResponseUtil<>(new ErrorInfo(Response.Status.FORBIDDEN, message)).toResponse(Response.Status.FORBIDDEN);
    }
}
