package edu.stanford.bmir.protege.web.client.merge_add;

import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasInitialFocusable;

public interface SelectOptionView extends IsWidget, HasInitialFocusable {
    int getSelectedOption();
}
