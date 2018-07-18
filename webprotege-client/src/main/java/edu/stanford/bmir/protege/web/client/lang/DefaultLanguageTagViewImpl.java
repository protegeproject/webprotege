package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.primitive.DefaultLanguageEditor;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jul 2018
 */
public class DefaultLanguageTagViewImpl extends Composite implements DefaultLanguageTagView {

    interface DefaultLanguageTagViewImplUiBinder extends UiBinder<HTMLPanel, DefaultLanguageTagViewImpl> {

    }

    private static DefaultLanguageTagViewImplUiBinder ourUiBinder = GWT.create(DefaultLanguageTagViewImplUiBinder.class);

    @UiField(provided = true)
    DefaultLanguageEditor languageTagEditor;

    @Inject
    public DefaultLanguageTagViewImpl(@Nonnull DefaultLanguageEditor languageTagEditor) {
        this.languageTagEditor = checkNotNull(languageTagEditor);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setLanguageTag(@Nonnull String languageTag) {
        languageTagEditor.setValue(languageTag);
    }

    @Nonnull
    @Override
    public String getLanguageTag() {
        return languageTagEditor.getValue().orElse("");
    }
}