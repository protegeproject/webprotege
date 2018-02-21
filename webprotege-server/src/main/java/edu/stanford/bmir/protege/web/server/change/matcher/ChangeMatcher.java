package edu.stanford.bmir.protege.web.server.change.matcher;

import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public interface ChangeMatcher {

    Optional<String> getDescription(ChangeApplicationResult<?> result);
}
