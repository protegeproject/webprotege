package edu.stanford.bmir.protege.web.client.form;

import com.google.common.collect.ImmutableSet;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import edu.stanford.bmir.protege.web.client.library.common.HasPlaceholder;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorImpl;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.form.data.*;
import edu.stanford.bmir.protege.web.shared.form.field.EntityNameControlDescriptorDto;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 06/04/16
 */
public class EntityNameControl extends Composite implements FormControl, HasPlaceholder {

    private EntityNameControlDescriptorDto descriptor;

    interface EntityNameControlUiBinder extends UiBinder<HTMLPanel, EntityNameControl> {

    }

    private static EntityNameControlUiBinder ourUiBinder = GWT.create(EntityNameControlUiBinder.class);

    @UiField(provided = true)
    PrimitiveDataEditorImpl editor;


    @Inject
    public EntityNameControl(Provider<PrimitiveDataEditor> primitiveDataEditorProvider) {
        editor = (PrimitiveDataEditorImpl) primitiveDataEditorProvider.get();
        initWidget(ourUiBinder.createAndBindUi(this));
        editor.addValueChangeHandler(this::handleEditorValueChanged);
        editor.setAutoSelectSuggestions(true);
        editor.setClassesAllowed(true);
        editor.setNamedIndividualsAllowed(true);
        editor.setObjectPropertiesAllowed(true);
        editor.setDataPropertiesAllowed(true);
        editor.setAnnotationPropertiesAllowed(true);
        editor.setDatatypesAllowed(true);
    }

    public void setDescriptor(@Nonnull EntityNameControlDescriptorDto descriptor) {
        this.descriptor = checkNotNull(descriptor);
        descriptor.getMatchCriteria().ifPresent(c -> editor.setCriteria(c));
        LocaleInfo localeInfo = LocaleInfo.getCurrentLocale();
        editor.setPlaceholder(descriptor.getPlaceholder()
                                        .get(localeInfo.getLocaleName()));
    }

    private void handleEditorValueChanged(ValueChangeEvent<Optional<OWLPrimitiveData>> event) {
        ValueChangeEvent.fire(EntityNameControl.this, getValue());
    }

    @Override
    public void setValue(@Nonnull FormControlDataDto object) {
        if(object instanceof EntityNameControlDataDto) {
            EntityNameControlDataDto data = (EntityNameControlDataDto) object;
            data.getEntity().ifPresent(editor::setValue);
        }
        else {
            editor.clearValue();
        }

    }

    @Override
    public void requestFocus() {
        editor.requestFocus();
    }

    @Override
    public String getPlaceholder() {
        return editor.getPlaceholder();
    }

    @Override
    public void setPlaceholder(String placeholder) {
        editor.setPlaceholder(placeholder);
    }

    @Override
    public void clearValue() {
        editor.clearValue();
    }

    @Nonnull
    @Override
    public ImmutableSet<FormRegionFilter> getFilters() {
        return ImmutableSet.of();
    }

    @Override
    public Optional<FormControlData> getValue() {
        return editor.getValue()
                .flatMap(OWLPrimitiveData::asEntity)
                .map(entity -> EntityNameControlData.get(descriptor.toFormControlDescriptor(), entity));
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<FormControlData>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public void setEnabled(boolean enabled) {
        editor.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return editor.isEnabled();
    }

    @Override
    public void setFormRegionFilterChangedHandler(@Nonnull FormRegionFilterChangedHandler handler) {

    }
}
