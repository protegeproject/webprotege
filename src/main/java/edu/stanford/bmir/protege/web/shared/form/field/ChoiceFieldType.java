package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public enum ChoiceFieldType {

    @JsonProperty("CheckBox")
    CHECK_BOX,

    @JsonProperty("RadioButton")
    RADIO_BUTTON,

    @JsonProperty("ComboBox")
    COMBO_BOX,

    @JsonProperty("SegmentedButton")
    SEGMENTED_BUTTON
}
