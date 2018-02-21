package edu.stanford.bmir.protege.web.shared.filter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 21/03/16
 */
public enum  FilterSetting {

    ON,

    OFF;

    public FilterSetting toggle() {
        if(this == ON) {
            return OFF;
        }
        else {
            return ON;
        }
    }

}
