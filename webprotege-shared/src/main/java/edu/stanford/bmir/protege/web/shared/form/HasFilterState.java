package edu.stanford.bmir.protege.web.shared.form;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-25
 */
public interface HasFilterState {

    @Nonnull
    FilterState getFilterState();
}
