package edu.stanford.bmir.protege.web.client.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.client.form.LanguageMapEditor;
import edu.stanford.bmir.protege.web.client.form.LanguageMapEntryPresenter;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class EntitySearchFilterViewImpl extends Composite implements EntitySearchFilterView {

    interface EntitySearchFilterViewImplUiBinder extends UiBinder<HTMLPanel, EntitySearchFilterViewImpl> {
    }

    private static EntitySearchFilterViewImplUiBinder ourUiBinder = GWT.create(EntitySearchFilterViewImplUiBinder.class);


    @UiField(provided = true)
    LanguageMapEditor languageMapEditor;

    @UiField
    SimplePanel criteriaContainer;

    @Inject
    public EntitySearchFilterViewImpl(LanguageMapEditor editor) {
        languageMapEditor = checkNotNull(editor);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setLanguageMap(@Nonnull LanguageMap languageMap) {
        languageMapEditor.setValue(languageMap);
    }

    @Nonnull
    @Override
    public LanguageMap getLanguageMap() {
        return languageMapEditor.getValue().orElse(LanguageMap.empty());
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getCriteriaContainer() {
        return criteriaContainer;
    }
}