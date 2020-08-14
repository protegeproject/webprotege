package edu.stanford.bmir.protege.web.client.shortform;

import com.google.common.collect.ImmutableList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class DictionaryLanguageSelectorViewImpl extends Composite implements DictionaryLanguageSelectorView {

    interface DictionaryLanguageViewImplUiBinder extends UiBinder<HTMLPanel, DictionaryLanguageSelectorViewImpl> {
    }

    private static DictionaryLanguageViewImplUiBinder ourUiBinder = GWT.create(DictionaryLanguageViewImplUiBinder.class);

    @UiField
    ListBox languageTypeNamesComboBox;

    @UiField
    SimplePanel container;

    @Nonnull
    private TypeNameChangedHandler typeNameChangedHandler = () -> {};

    @Inject
    public DictionaryLanguageSelectorViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        languageTypeNamesComboBox.addItem("");
        languageTypeNamesComboBox.addChangeHandler(event -> typeNameChangedHandler.handleTypeNameChanged());
        languageTypeNamesComboBox.addClickHandler(event -> typeNameChangedHandler.handleTypeNameChanged());
    }

    @Override
    public void setTypeNames(@Nonnull ImmutableList<String> typeNames) {
        languageTypeNamesComboBox.clear();
        languageTypeNamesComboBox.addItem("");
        typeNames.forEach(typeName -> languageTypeNamesComboBox.addItem(typeName));
    }

    @Nonnull
    @Override
    public Optional<String> getSelectedTypeName() {
        return Optional.ofNullable(languageTypeNamesComboBox.getSelectedValue())
                .filter(v -> !v.isEmpty());
    }

    @Override
    public void setSelectedTypeName(@Nonnull String typeName) {
        languageTypeNamesComboBox.setSelectedIndex(0);
        for(int i = 0; i < languageTypeNamesComboBox.getItemCount(); i++) {
            String tn = languageTypeNamesComboBox.getValue(i);
            if(typeName.equals(tn)) {
                languageTypeNamesComboBox.setSelectedIndex(i);
                break;
            }
        }
    }

    @Override
    public void setTypeNameChangedHandler(@Nonnull TypeNameChangedHandler handler) {
        this.typeNameChangedHandler = checkNotNull(handler);
    }

    @Nonnull
    @Override
    public AcceptsOneWidget getTypeDetailsContainer() {
        return container;
    }

    @Override
    public void clear() {
        languageTypeNamesComboBox.setSelectedIndex(0);
        container.clear();
    }

    @Override
    public void requestFocus() {
        languageTypeNamesComboBox.setFocus(true);
    }
}