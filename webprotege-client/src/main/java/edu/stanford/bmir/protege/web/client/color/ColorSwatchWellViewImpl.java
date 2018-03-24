package edu.stanford.bmir.protege.web.client.color;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.shared.color.Color;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.gwt.user.client.Event.ONCLICK;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 24 Mar 2018
 */
public class ColorSwatchWellViewImpl extends Composite implements ColorSwatchWellView {

    interface ColorSwatchWellViewImplUiBinder extends UiBinder<HTMLPanel, ColorSwatchWellViewImpl> {

    }

    private static ColorSwatchWellViewImplUiBinder ourUiBinder = GWT.create(ColorSwatchWellViewImplUiBinder.class);

    private Color color = Color.getRGB(0, 0, 0);

    @Inject
    public ColorSwatchWellViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        getElement().getStyle().setBackgroundColor(color.getHex());
        sinkEvents(ONCLICK);
    }

    @Override
    public void setColor(@Nonnull Color color) {
        this.color = checkNotNull(color);
        if (isAttached()) {
            getElement().getStyle().setBackgroundColor(color.getHex());
        }
    }

    @Nonnull
    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addHandler(handler, ClickEvent.getType());
    }
}