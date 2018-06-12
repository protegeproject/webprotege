package edu.stanford.bmir.protege.web.shared.match;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 8 Jun 2018
 */
public enum AnnotationPresence {

    @JsonProperty("present")
    PRESENT,

    @JsonProperty("absent")
    ABSENT
}
