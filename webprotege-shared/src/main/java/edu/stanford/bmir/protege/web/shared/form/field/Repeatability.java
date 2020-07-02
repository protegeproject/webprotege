package edu.stanford.bmir.protege.web.shared.form.field;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 30/03/16
 */
public enum Repeatability {

    NON_REPEATABLE,

    REPEATABLE_VERTICALLY,

    REPEATABLE_HORIZONTALLY;

    public boolean isRepeatable() {
        return this != NON_REPEATABLE;
    }
}
