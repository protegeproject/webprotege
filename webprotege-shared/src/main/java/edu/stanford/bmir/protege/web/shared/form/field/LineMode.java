package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public enum LineMode {

    @JsonProperty("SingleLine")
    SINGLE_LINE,

    @JsonProperty("MultiLine")
    MULTI_LINE
}
