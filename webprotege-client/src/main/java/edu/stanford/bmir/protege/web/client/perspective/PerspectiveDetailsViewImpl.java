package edu.stanford.bmir.protege.web.client.perspective;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.form.LanguageMapEditor;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class PerspectiveDetailsViewImpl extends Composite implements PerspectiveDetailsView {

    private LabelChangedHandler labelChangedHandler = () -> {};

    interface PerspectiveDetailsViewImplUiBinder extends UiBinder<HTMLPanel, PerspectiveDetailsViewImpl> {
    }

    private static PerspectiveDetailsViewImplUiBinder ourUiBinder = GWT.create(PerspectiveDetailsViewImplUiBinder.class);

    @UiField(provided = true)
    LanguageMapEditor labelEditor;

    @UiField
    CheckBox favoriteCheckBox;

    @Inject
    public PerspectiveDetailsViewImpl(@Nonnull LanguageMapEditor labelEditor) {
        this.labelEditor = checkNotNull(labelEditor);
        initWidget(ourUiBinder.createAndBindUi(this));
        labelEditor.addValueChangeHandler(event -> labelChangedHandler.handleLabelChanged());
    }

    @Override
    public void setLabel(@Nonnull LanguageMap label) {
        labelEditor.setValue(label);
    }

    @Nonnull
    @Override
    public LanguageMap getLabel() {
        return labelEditor.getValue().orElse(LanguageMap.empty());
    }

    @Override
    public void setFavorite(boolean favorite) {
        favoriteCheckBox.setValue(favorite);
    }

    @Override
    public boolean isFavorite() {
        return favoriteCheckBox.getValue();
    }

    @Override
    public void setLabelChangedHandler(LabelChangedHandler handler) {
        this.labelChangedHandler = checkNotNull(handler);
    }
}