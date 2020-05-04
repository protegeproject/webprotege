package edu.stanford.bmir.protege.web.client.form;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-05-03
 */
public interface HasGridColumnVisibilityManager {

    /**
     * Override the column visibility manager with the specified one.
     * @param columnVisibilityManager The override value
     */
    void setColumnVisibilityManager(@Nonnull GridColumnVisibilityManager columnVisibilityManager);
}
