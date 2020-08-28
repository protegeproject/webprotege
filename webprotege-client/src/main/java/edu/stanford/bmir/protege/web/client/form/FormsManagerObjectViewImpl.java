package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.lang.LanguageMapChangedHandler;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class FormsManagerObjectViewImpl extends Composite implements FormsManagerObjectView {

    private ShowFormDetailsHandler showFormDetailsHandler = () -> {};

    interface FormsManagerObjectViewImplUiBinder extends UiBinder<HTMLPanel, FormsManagerObjectViewImpl> {
    }

    private static FormsManagerObjectViewImplUiBinder ourUiBinder = GWT.create(FormsManagerObjectViewImplUiBinder.class);

    @UiField(provided = true)
    LanguageMapEditor languageMapEditor;

    @UiField
    Button formDetailButton;

    private LanguageMapChangedHandler languageMapChangedHandler = () -> {};

    @Inject
    public FormsManagerObjectViewImpl(LanguageMapEditor languageMapEditor) {
        this.languageMapEditor = languageMapEditor;
        initWidget(ourUiBinder.createAndBindUi(this));
        languageMapEditor.addValueChangeHandler(event -> languageMapChangedHandler.handleLanguageMapChanged());
        formDetailButton.addClickHandler(this::handleFormDetailsClicked);
    }

    private void handleFormDetailsClicked(ClickEvent event) {
        showFormDetailsHandler.handleShowFormDetails();
    }

    @Override
    public void setShowFormDetailsHandler(@Nonnull ShowFormDetailsHandler handler) {
        this.showFormDetailsHandler = checkNotNull(handler);
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

    @Override
    public void setLanguageMapChangedHandler(@Nonnull LanguageMapChangedHandler handler) {
        this.languageMapChangedHandler = checkNotNull(handler);
    }
}