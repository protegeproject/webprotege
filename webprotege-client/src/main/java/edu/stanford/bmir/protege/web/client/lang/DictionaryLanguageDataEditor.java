package edu.stanford.bmir.protege.web.client.lang;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.library.dlg.HasRequestFocus;
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
public class DictionaryLanguageDataEditor extends Composite implements ValueEditor<DictionaryLanguageData>, HasRequestFocus {

    @Nonnull
    private final DictionaryLanguageDataView view;

    @Inject
    public DictionaryLanguageDataEditor(@Nonnull DictionaryLanguageDataView view) {
        this.view = view;
        initWidget(view.asWidget());
    }

    @Override
    public void requestFocus() {
        view.requestFocus();
    }

    @Override
    public void clearValue() {
        view.clear();
    }

    @Override
    public Optional<DictionaryLanguageData> getValue() {
        Optional<OWLAnnotationPropertyData> property = view.getAnnotationProperty();
        String lang = view.getLang().toLowerCase();
        return property.map(prop -> DictionaryLanguageData.get(prop.getEntity().getIRI(), prop.getBrowserText(), lang));
    }

    @Override
    public void setValue(DictionaryLanguageData object) {
        object.getAnnotationPropertyData().ifPresent(view::setAnnotationProperty);
        view.setLang(object.getLanguageTag());
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
