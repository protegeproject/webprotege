package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;

public interface FormControlStackView extends IsWidget, HasRequestFocus, HasEnabled {

    void setPaginatorVisible(boolean visible);

    void setVerticalLayout();

    void setHorizontalLayout();
}
