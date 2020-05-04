package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.resources.WebProtegeClientBundle;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-05-04
 */
public class GridHeaderCellContainerImpl implements GridHeaderCellContainer {

    public static final String FLEX_BASIS = "flexBasis";

    private final SimplePanel delegate = new SimplePanel();

    public GridHeaderCellContainerImpl() {
        delegate.addStyleName(WebProtegeClientBundle.BUNDLE.style().formGridColumn());
    }

    @Override
    public void setWidget(IsWidget w) {
        delegate.setWidget(w);
    }

    @Override
    public boolean isVisible() {
        return delegate.isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        Style style = delegate.getElement()
                              .getStyle();
        if(visible) {
            style.clearDisplay();
        }
        else {
            style.setDisplay(Style.Display.NONE);
        }
    }

    @Override
    public Widget asWidget() {
        return delegate;
    }

    @Override
    public void setWeight(double weight) {
        Style style = delegate.getElement().getStyle();
        style.setProperty(FLEX_BASIS, weight * 100, Style.Unit.PCT);
    }
}
