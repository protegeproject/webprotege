package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
import edu.stanford.bmir.protege.web.client.shortform.DictionaryLanguagePresenter;
import edu.stanford.bmir.protege.web.client.shortform.DictionaryLanguageSelectorPresenter;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
public class DictionaryLanguageDataEditor extends Composite implements ValueEditor<DictionaryLanguage>, HasRequestFocus {

    @Nonnull
    private final DictionaryLanguageSelectorPresenter presenter;

    @Inject
    public DictionaryLanguageDataEditor(@Nonnull DictionaryLanguageSelectorPresenter presenter) {
        this.presenter = presenter;
        initWidget(presenter.getView().asWidget());
    }

    @Override
    public void requestFocus() {
        presenter.requestFocus();
    }

    @Override
    public void clearValue() {
        presenter.clear();
    }

    @Override
    public Optional<DictionaryLanguage> getValue() {
        return presenter.getDictionaryLanguage();
    }

    @Override
    public void setValue(DictionaryLanguage object) {
        presenter.setDictionaryLanguage(object);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<DictionaryLanguage>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public boolean isWellFormed() {
        return true;
    }
}
