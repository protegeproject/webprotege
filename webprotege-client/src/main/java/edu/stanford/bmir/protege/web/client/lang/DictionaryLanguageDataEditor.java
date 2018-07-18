package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.entity.OWLAnnotationPropertyData;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
public class DictionaryLanguageDataEditor extends Composite implements ValueEditor<DictionaryLanguageData> {

    @Nonnull
    private final DictionaryLanguageDataView view;

    @Inject
    public DictionaryLanguageDataEditor(@Nonnull DictionaryLanguageDataView view) {
        this.view = view;
        initWidget(view.asWidget());
    }

    @Override
    public void clearValue() {
        view.clear();
    }

    @Override
    public Optional<DictionaryLanguageData> getValue() {
        Optional<OWLAnnotationPropertyData> property = view.getAnnotationProperty();
        String lang = view.getLang();
        return property.map(prop -> Optional.of(DictionaryLanguageData.get(prop,
                                                                           lang)))
                       .orElse(Optional.of(DictionaryLanguageData.get(lang)));
    }

    @Override
    public void setValue(DictionaryLanguageData object) {
        object.getAnnotationPropertyData().ifPresent(view::setAnnotationProperty);
        view.setLang(object.getLanguage());
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<DictionaryLanguageData>> handler) {
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
