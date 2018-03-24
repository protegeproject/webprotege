package edu.stanford.bmir.protege.web.client.color;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.shared.color.Color;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Mar 2018
 */
public class ColorSwatchViewImpl extends Composite implements ColorSwatchView {

    private static final int COLOR_COUNT = 36;

    interface ColorSwatchViewImplUiBinder extends UiBinder<HTMLPanel, ColorSwatchViewImpl> {
    }

    private static ColorSwatchViewImplUiBinder ourUiBinder = GWT.create(ColorSwatchViewImplUiBinder.class);

    private final List<ColorSwatchWellView> colors = new ArrayList<>();

    @UiField
    HTMLPanel container;

    private ColorSelectedHandler colorSelectedHandler = (color) -> {};

    @Inject
    public ColorSwatchViewImpl(@Nonnull Provider<ColorSwatchWellView> wellViewProvider) {
        Provider<ColorSwatchWellView> wellViewProvider1 = checkNotNull(wellViewProvider);
        initWidget(ourUiBinder.createAndBindUi(this));
        for(int i = 0; i < COLOR_COUNT; i++) {
            ColorSwatchWellView wellView = wellViewProvider.get();
            colors.add(wellView);
            container.add(wellView);
            final int index = i;
            wellView.addClickHandler(event -> handleColorClickedAt(index));
        }
    }

    @Override
    public void setColorSelectedHandler(@Nonnull ColorSelectedHandler handler) {
        this.colorSelectedHandler = checkNotNull(handler);
    }

    private void handleColorClickedAt(int index) {
        Color clickedColor = colors.get(index).getColor();
        colorSelectedHandler.handleColorSelected(clickedColor);
    }

    @Override
    public void setColorAt(int index, @Nonnull Color color) {
        checkElementIndex(index, COLOR_COUNT);
        colors.get(index).setColor(checkNotNull(color));
    }
}