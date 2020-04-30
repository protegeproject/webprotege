package edu.stanford.bmir.protege.web.client.filter;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import edu.stanford.bmir.protege.web.shared.filter.FilterSet;
import edu.stanford.bmir.protege.web.shared.filter.FilterSetting;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-30
 */
public class FilterViewPopup {

    public interface FilterViewPopupClosedHandler {
        void handleFilterViewClosed(FilterSet filterSet);
    }

    public void showFilterView(@Nonnull FilterView filterView,
                               @Nonnull UIObject showRelativeTo,
                               @Nonnull FilterViewPopupClosedHandler handler) {
        PopupPanel popup = new PopupPanel();
        popup.setAutoHideEnabled(true);
        popup.setWidget(filterView);
        popup.showRelativeTo(showRelativeTo);
        popup.addCloseHandler(closeEvent -> {
            FilterSet filterSet = filterView.getFilterSet();
            ValueChangeEvent.fire(filterView, filterSet);
            handler.handleFilterViewClosed(filterSet);
        });
    }
}
