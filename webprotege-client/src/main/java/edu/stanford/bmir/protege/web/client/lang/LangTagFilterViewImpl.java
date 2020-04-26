package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-26
 */
public class LangTagFilterViewImpl extends Composite implements LangTagFilterView {

    interface LangTagFilterViewImplUiBinder extends UiBinder<HTMLPanel, LangTagFilterViewImpl> {

    }

    private static LangTagFilterViewImplUiBinder ourUiBinder = GWT.create(LangTagFilterViewImplUiBinder.class);

    @UiField
    SimplePanel container;

    @Inject
    public LangTagFilterViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getLangTagsContainer() {
        return container;
    }
}
