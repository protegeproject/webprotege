package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
public class DisplayLanguagesViewImpl extends Composite implements DisplayLanguagesView {

    interface DisplayLanguagesViewImplUiBinder extends UiBinder<HTMLPanel, DisplayLanguagesViewImpl> {

    }

    private static DisplayLanguagesViewImplUiBinder ourUiBinder = GWT.create(DisplayLanguagesViewImplUiBinder.class);

    @Inject
    public DisplayLanguagesViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}