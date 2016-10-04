package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasEnabled;
import edu.stanford.bmir.protege.web.client.primitive.*;
import edu.stanford.bmir.protege.web.client.ui.library.suggest.EntitySuggestion;
import edu.stanford.bmir.protege.web.shared.DirtyChangedEvent;
import edu.stanford.bmir.protege.web.shared.DirtyChangedHandler;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueDescriptor;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueState;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLAxiom;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 27/02/2014
 */
public class PropertyValueDescriptorEditorImpl extends Composite implements PropertyValueDescriptorEditor, HasEnabled {

    public static final String DERIVED_PROPERTY_VALUE = "derived-property-value";

    interface PropertyValueDescriptorEditorImplUiBinder extends UiBinder<HTMLPanel, PropertyValueDescriptorEditorImpl> {

    }

    private static PropertyValueDescriptorEditorImplUiBinder ourUiBinder = GWT.create(PropertyValueDescriptorEditorImplUiBinder.class);



    @UiField(provided = true)
    protected PrimitiveDataEditorImpl propertyField;

    @UiField(provided = true)
    protected PrimitiveDataEditorImpl valueField;

    @UiField(provided = true)
    protected DefaultLanguageEditor languageField;

    private Optional<PropertyValueDescriptor> currentValue = Optional.absent();

    private boolean dirty = false;

    private boolean enabled = true;

    private PropertyValueState propertyValueState = PropertyValueState.ASSERTED;

    @Inject
    public PropertyValueDescriptorEditorImpl(PrimitiveDataEditorImpl propertyField, PrimitiveDataEditorImpl valueField) {
        this.propertyField = propertyField;
        this.valueField = valueField;
        languageField = (DefaultLanguageEditor) valueField.getLanguageEditor();
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
    }

    @UiHandler("propertyField")
    protected void handlePropertyChanged(ValueChangeEvent<Optional<OWLPrimitiveData>> event) {
        setDirty();
        fireEvent(new PropertyValuePropertyChangedEvent(getPropertyFieldValue()));
        handlePossibleValueChange();
    }

    @UiHandler("valueField")
    protected void handleValueChanged(ValueChangeEvent<Optional<OWLPrimitiveData>> event) {
        // Edit made.  Now we are in asserted mode.
        setPropertyValueState(PropertyValueState.ASSERTED);
        setDirty();
        fireEvent(new PropertyValueValueChangedEvent(getValueFieldValue()));
        handlePossibleValueChange();
    }

    @UiHandler("languageField")
    protected void handleLanguageChanged(ValueChangeEvent<Optional<String>> event) {
        setPropertyValueState(PropertyValueState.ASSERTED);
        setDirty();
        fireEvent(new PropertyValueValueChangedEvent(getValueFieldValue()));
        handlePossibleValueChange();
    }


    public PropertyValueState getPropertyValueState() {
        return propertyValueState;
    }

    private void updatePropertyValueStateStyle() {
        if(propertyValueState == PropertyValueState.DERIVED) {
            addStyleName(DERIVED_PROPERTY_VALUE);
        }
        else {
            removeStyleName(DERIVED_PROPERTY_VALUE);
        }
    }

    public void setPropertyValueState(PropertyValueState propertyValueState) {
        this.propertyValueState = propertyValueState;
        updatePropertyValueStateStyle();
    }

    public void setAllowedPropertyTypes(Collection<PrimitiveType> types) {
        propertyField.setAllowedTypes(types);
    }

    public void setAllowedValueTypes(Collection<PrimitiveType> types) {
        valueField.setAllowedTypes(types);
        setLanguageEditorVisible(types.contains(PrimitiveType.LITERAL));
    }

    public void setValuePlaceholder(String placeholder) {
        valueField.setPlaceholder(placeholder);
    }

    @Override
    public HandlerRegistration addPropertyValueChangedHandler(PropertyValuePropertyChangedHandler handler) {
        return addHandler(handler, PropertyValuePropertyChangedEvent.getType());
    }

    @Override
    public HandlerRegistration addPropertyValueChangedHandler(PropertyValueValueChangedHandler handler) {
        return addHandler(handler, PropertyValueValueChangedEvent.getType());
    }

    @Override
    public void setPropertyFieldFreshEntitiesHandler(FreshEntitiesHandler freshEntitiesHandler) {
        propertyField.setFreshEntitiesHandler(freshEntitiesHandler);
    }

    @Override
    public void setValueFieldFreshEntitiesHandler(FreshEntitiesHandler freshEntitiesHandler) {
        valueField.setFreshEntitiesHandler(freshEntitiesHandler);
    }

    @Override
    public void setPropertyFieldSuggestStrategy(FreshEntitySuggestStrategy suggestStrategy) {
        propertyField.setFreshEntitiesSuggestStrategy(suggestStrategy);
    }

    @Override
    public void setValueFieldSuggestStrategy(FreshEntitySuggestStrategy suggestStrategy) {
        valueField.setFreshEntitiesSuggestStrategy(suggestStrategy);
    }

    @Override
    public Optional<OWLPropertyData> getPropertyFieldValue() {
        return propertyField.getValue().transform(new Function<OWLPrimitiveData, OWLPropertyData>() {
            @Nullable
            @Override
            public OWLPropertyData apply(@Nullable OWLPrimitiveData input) {
                if (input instanceof OWLPropertyData) {
                    return (OWLPropertyData) input;
                }
                else {
                    return null;
                }
            }
        });
    }

    @Override
    public Optional<OWLPrimitiveData> getValueFieldValue() {
        return valueField.getValue();
    }

    @Override
    public void setValue(PropertyValueDescriptor propertyValue) {
        this.currentValue = Optional.of(propertyValue);
        valueField.setPrimitiveDataPlaceholder(Optional.<OWLPrimitiveData>absent());
        if(propertyValue.getState() == PropertyValueState.ASSERTED) {
            propertyField.setValue(propertyValue.getProperty());
            valueField.setValue(propertyValue.getValue());
            valueField.setFreshEntitiesSuggestStrategy(new SimpleFreshEntitySuggestStrategy());
        }
        else {
            propertyField.setValue(propertyValue.getProperty());
            if (!propertyValue.isMostSpecific()) {
                valueField.clearValue();
                valueField.setPrimitiveDataPlaceholder(Optional.of(propertyValue.getValue()));
                // TODO: Finish this
//                if(propertyValue.getValue() instanceof OWLEntityData) {
//                    valueField.setFreshEntitiesSuggestStrategy(new AugmentedFreshEntitiesSuggestStrategy(Collections
//                            .singleton((OWLEntityData) propertyValue.getValue())
//                    ));
//                }
//                else {
                    valueField.setFreshEntitiesSuggestStrategy(new SimpleFreshEntitySuggestStrategy());
//                }

            }
            else {
                valueField.setValue(propertyValue.getValue());
            }
        }
        updateEnabled();
        setPropertyValueState(propertyValue.getState());
        setLanguageEditorVisible(propertyValue.getValue().isOWLLiteral());
    }

    private void setLanguageEditorVisible(boolean langEditorVisible) {
        languageField.getElement().getStyle().setVisibility(langEditorVisible ? Style.Visibility.VISIBLE : Style.Visibility.HIDDEN);
    }

    @Override
    public void clearValue() {
        propertyField.clearValue();
        valueField.clearValue();
        languageField.clearValue();
        handlePossibleValueChange();
    }

    @Override
    public boolean isDirty() {
        return propertyField.isDirty() || valueField.isDirty() || dirty;
    }


    private void setDirty() {
        dirty = true;
        fireDirtyChangedEvent();
    }


    private void fireDirtyChangedEvent() {
        fireEvent(new DirtyChangedEvent());
    }

    @Override
    public Optional<PropertyValueDescriptor> getValue() {
        if(propertyValueState == PropertyValueState.DERIVED) {
            return currentValue;
        }
        else {
            if(propertyField.getValue().isPresent() && valueField.getValue().isPresent()) {
                Set<OWLAxiom> augmentingAxioms = Sets.newHashSet();
                Optional<EntitySuggestion> selectedSuggestion = valueField.getSelectedSuggestion();
                if(selectedSuggestion.isPresent()) {
                    if(selectedSuggestion.get() instanceof FreshEntitySuggestion) {
                        augmentingAxioms.addAll(((FreshEntitySuggestion) selectedSuggestion.get()).getAugmentingAxioms());
                    }
                }
                return Optional.of(new PropertyValueDescriptor((OWLPropertyData) propertyField.getValue().get(),
                        valueField.getValue().get(), propertyValueState, false, augmentingAxioms));
            }
            else {
                return Optional.absent();
            }
        }
    }

    @Override
    public HandlerRegistration addDirtyChangedHandler(DirtyChangedHandler handler) {
        return addHandler(handler, DirtyChangedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Optional<PropertyValueDescriptor>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public boolean isWellFormed() {
        boolean propertyPresent = propertyField.getValue().isPresent();
        boolean valuePresent = valueField.getValue().isPresent();
        return (propertyPresent && valuePresent) || (!propertyPresent && !valuePresent);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        updateEnabled();
    }

    private void updateEnabled() {
        propertyField.setEnabled(enabled);
        valueField.setEnabled(enabled);
        languageField.setEnabled(enabled);
    }

    @Override
    public String getPropertyFieldLexicalValue() {
        return propertyField.getText().trim();
    }

    @Override
    public void coercePropertyFieldToType(EntityType<?> primitiveType) {
        propertyField.coerceToEntityType(primitiveType);
    }

    private void handlePossibleValueChange() {
        GWT.log("Handling possible value change (from " + currentValue + " to " + getValue() + ")");
        if(currentValue.equals(getValue())) {
            GWT.log("    The values are the same.  Not firing event.");
            return;
        }
        currentValue = getValue();
        if(isWellFormed()) {
            ValueChangeEvent.fire(this, getValue());
        }
    }
}
