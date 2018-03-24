package edu.stanford.bmir.protege.web.client.color;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.shared.color.Color;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Mar 2018
 */
public interface ColorSwatchView extends IsWidget {


    /**
     * Set the color at the specified index.
     * @param index The index in the range of 0-35.
     * @param color The color to be set.
     */
    void setColorAt(int index, @Nonnull Color color);

    /**
     * Sets a handler that is notified when a color is selected.
     * @param handler The handler.
     */
    void setColorSelectedHandler(@Nonnull ColorSelectedHandler handler);
}
