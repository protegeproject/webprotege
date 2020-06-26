package edu.stanford.bmir.protege.web.client.form.input;

import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import edu.stanford.bmir.protege.web.client.HasReadOnly;

public interface CheckBoxView extends IsWidget, HasEnabled, HasReadOnly, HasValue<Boolean>, HasText, HasKeyUpHandlers, HasMouseDownHandlers, HasMouseUpHandlers {

    void setFocus(boolean focus);
}
