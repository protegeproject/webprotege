package edu.stanford.bmir.protege.web.client.color;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.color.Color;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Mar 2018
 *
 * A small square of color in the user interface.
 */
public interface ColorSwatchWellView extends IsWidget, HasClickHandlers {

    /**
     * Sets the color displayed by the color well.
     */
    void setColor(@Nonnull Color color);

    /**
     * Gets the color displayed by the color well.
     */
    @Nonnull
    Color getColor();
}
