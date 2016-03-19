package edu.stanford.bmir.protege.web.client.change;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 26/02/15
 */
public class ChangeListViewImpl extends Composite implements ChangeListView {

    interface ChangeListViewImplUiBinder extends UiBinder<HTMLPanel, ChangeListViewImpl> {
    }

    private static ChangeListViewImplUiBinder ourUiBinder = GWT.create(ChangeListViewImplUiBinder.class);

    public ChangeListViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiField
    protected HTMLPanel rootElement;

    @Override
    public void addChangeDetailsView(ChangeDetailsView view) {
        rootElement.add(view);
    }

    @Override
    public void addSeparator(String separatorText) {
        ChangeListSeparator sep = new ChangeListSeparator();
        sep.setSeparatorText(separatorText);
        rootElement.add(sep);
    }

    @Override
    public void clear() {
        rootElement.clear();
    }

    @Override
    public void setDetailsVisible(boolean detailsVisible) {
        for(int i = 0; i < rootElement.getWidgetCount(); i++) {
            Widget widget = rootElement.getWidget(i);
            if (widget instanceof ChangeDetailsView) {
                ChangeDetailsView view = (ChangeDetailsView) widget;
                view.setDetailsVisible(detailsVisible);
            }
        }
    }
}