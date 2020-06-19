package edu.stanford.bmir.protege.web.client.form;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-06-19
 */
public interface FormRegionFilterChangedHandler {

    void handleFormRegionFilterChanged(@Nonnull FormRegionFilterChangedEvent event);
}
