package edu.stanford.bmir.protege.web.client.shortform;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

import javax.inject.Inject;

public class DictionaryLanguageBlankViewImpl extends Composite implements DictionaryLanguageBlankView {

    interface DictionaryLanguageBlankViewImplUiBinder extends UiBinder<HTMLPanel, DictionaryLanguageBlankViewImpl> {
    }

    private static DictionaryLanguageBlankViewImplUiBinder ourUiBinder = GWT.create(
            DictionaryLanguageBlankViewImplUiBinder.class);

    @Inject
    public DictionaryLanguageBlankViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }


}