package edu.stanford.bmir.protege.web.client.color;

import edu.stanford.bmir.protege.web.shared.color.Color;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Mar 2018
 */
public interface ColorSelectedHandler {

    /**
     * Called when a color is selected.
     *
     * @param color The selected color.
     */
    void handleColorSelected(@Nonnull Color color);
}
