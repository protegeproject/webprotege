package edu.stanford.bmir.protege.web.server.api.exception;

import edu.stanford.bmir.protege.web.server.api.ResponseUtil;
import edu.stanford.bmir.protege.web.shared.permissions.PermissionDeniedException;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 May 2018
 */
@Provider
public class PermissionDeniedExceptionMapper implements ExceptionMapper<PermissionDeniedException> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(PermissionDeniedException exception) {
        return ResponseUtil.forbidden(exception.getMessage());
    }
}
