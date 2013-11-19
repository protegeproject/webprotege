package edu.stanford.bmir.protege.web.client.ui.util;

import com.gwtext.client.widgets.Panel;


public abstract class AbstractValidatableTab extends Panel implements ValidatableTab {

    public abstract boolean isValid();

    public abstract void onSave();

}
