package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 12/04/16
 */
public enum Required {

    @JsonProperty("Required")
    REQUIRED,

    @JsonProperty("Optional")
    OPTIONAL
}
