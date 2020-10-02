package edu.stanford.bmir.protege.web.shared.form;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-01
 */
public enum ValidationStatus {

    VALID,

    INVALID;

    public boolean isInvalid() {
        return this.equals(INVALID);
    }

    public boolean isValid() {
        return this.equals(VALID);
    }
}
