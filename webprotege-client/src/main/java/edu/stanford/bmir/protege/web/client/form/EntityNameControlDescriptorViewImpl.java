package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-20
 */
public class EntityNameControlDescriptorViewImpl extends Composite implements EntityNameControlDescriptorView {

    interface EntityNameControlDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, EntityNameControlDescriptorViewImpl> {

    }

    private static EntityNameControlDescriptorViewImplUiBinder ourUiBinder = GWT.create(
            EntityNameControlDescriptorViewImplUiBinder.class);

    @UiField
    SimplePanel criteriaContainer;

    @UiField(provided = true)
    LanguageMapEditor placeholderEditor;

    @Inject
    public EntityNameControlDescriptorViewImpl(@Nonnull LanguageMapEditor placeholderEditor) {
        this.placeholderEditor = checkNotNull(placeholderEditor);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getCriteriaViewContainer() {
        return criteriaContainer;
    }

    @Override
    public void clear() {
        placeholderEditor.clearValue();
    }

    @Nonnull
    @Override
    public LanguageMap getPlaceholder() {
        return placeholderEditor.getValue().orElse(LanguageMap.empty());
    }



    @Override
    public void setPlaceholder(@Nonnull LanguageMap placeholder) {
        placeholderEditor.setValue(placeholder);
    }
}
