package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

import javax.annotation.Nonnull;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-05-04
 */
public class GridCellContainerImpl implements GridCellContainer {

    public static final String FLEX_BASIS = "flexBasis";

    @Nonnull
    private final SimplePanel delegate;

    public GridCellContainerImpl(@Nonnull SimplePanel delegate) {
        this.delegate = checkNotNull(delegate);
    }

    @Override
    public void setWidget(IsWidget w) {
        delegate.setWidget(w);
    }

    @Override
    public boolean isVisible() {
        return !Style.Display.NONE.getCssName().equals(delegate.getElement().getStyle().getDisplay());
    }

    @Override
    public void setVisible(boolean visible) {
        if(visible) {
            delegate.getElement().getStyle().clearDisplay();
        }
        else {
            delegate.getElement().getStyle().setDisplay(Style.Display.NONE);
        }
    }

    @Override
    public Widget asWidget() {
        return delegate;
    }

    @Override
    public void setWeight(double weight) {
        Style style = delegate.getElement()
                              .getStyle();
        style.setProperty(FLEX_BASIS, weight * 100, Style.Unit.PCT);
    }

    @Override
    public void requestFocus() {
        Widget w = delegate.getWidget();
        if(w instanceof HasRequestFocus) {
            ((HasRequestFocus) w).requestFocus();
        }
    }
}
