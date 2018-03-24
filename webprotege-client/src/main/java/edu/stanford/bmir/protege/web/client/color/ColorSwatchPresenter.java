package edu.stanford.bmir.protege.web.client.color;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import edu.stanford.bmir.protege.web.shared.color.Color;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Mar 2018
 *
 * Presents a swatch of 36 colors.  The colors are shown in two blocks, each 18 colors.  One with a neutral
 * lightness (of 0.5) and one with a lower brightness (of 0.3).  The 18 colors in each block start
 * with a hue of 0 and end with a hue of 360.
 */
public class ColorSwatchPresenter {

    private static final double DEFAULT_SATURATION = 0.8;

    @Nonnull
    private final ColorSwatchView view;

    @Nonnull
    private ColorSelectedHandler colorSelectedHandler = (color) -> {};

    private final PopupPanel popupPanel;

    private boolean initializedColors = false;

    private double saturation = DEFAULT_SATURATION;

    /**
     * Creates a {@link ColorSwatchPresenter}.
     * @param view The view that the presenter controls.
     * @param popupPanel A popup that is used to display the view.
     */
    @Inject
    public ColorSwatchPresenter(@Nonnull ColorSwatchView view,
                                @Nonnull PopupPanel popupPanel) {
        this.view = checkNotNull(view);
        this.view.setColorSelectedHandler(this::handleColorSelected);
        this.popupPanel = checkNotNull(popupPanel);
        popupPanel.setModal(true);
        popupPanel.setAutoHideEnabled(true);
    }

    /**
     * Sets a handler that is called when a color is selected by the user.
     * @param handler The handler.
     */
    public void setColorSelectedHandler(@Nonnull ColorSelectedHandler handler) {
        this.colorSelectedHandler = checkNotNull(handler);
    }

    /**
     * Sets the saturation for the colors displayed by the color swatch.
     * @param saturation The saturation.  This must be a value between 0.0 and 1.0 inclusive.
     * @throws IllegalArgumentException if the saturation is out of range.
     */
    public void setSaturation(double saturation) {
        checkArgument(0.0 <= saturation && saturation <= 1.0,
                      "Saturation must be between 0.0 and 1.0 inclusive.");
        this.saturation = saturation;
        updateColors();
    }

    private void handleColorSelected(@Nonnull Color color) {
        popupPanel.hide();
        colorSelectedHandler.handleColorSelected(color);
    }

    public void showPopup(UIObject target) {
        if (!initializedColors) {
            updateColors();
        }
        popupPanel.setWidget(view);
        popupPanel.showRelativeTo(target);
    }

    private void updateColors() {
        int index = 0;
        for(int i = 0; i < 360; i = i + 20) {
            Color color = Color.getHSL(i, saturation, 0.5);
            view.setColorAt(index, color);
            index++;
        }
        for(int i = 0; i < 360; i = i + 20) {
            Color color = Color.getHSL(i, saturation, 0.3);
            view.setColorAt(index, color);
            index++;
        }
        initializedColors = true;
    }
}
