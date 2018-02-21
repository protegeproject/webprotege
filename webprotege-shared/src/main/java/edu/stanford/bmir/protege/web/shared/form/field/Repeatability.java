package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public enum Repeatability {

    @JsonProperty("NonRepeatable")
    NON_REPEATABLE,

    @JsonProperty("RepeatableVertically")
    REPEATABLE_VERTICALLY,

    @JsonProperty("RepeatableHorizontally")
    REPEATABLE_HORIZONTALLY,

}
