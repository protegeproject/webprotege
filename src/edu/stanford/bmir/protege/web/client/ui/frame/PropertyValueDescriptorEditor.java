package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import com.google.gwt.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.client.primitive.FreshEntitiesHandler;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditorSuggestOracleMode;
import edu.stanford.bmir.protege.web.client.ui.editor.ValueEditor;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueState;
import org.semanticweb.owlapi.model.EntityType;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/12/2012
 */
public interface PropertyValueDescriptorEditor extends ValueEditor<PropertyValueDescriptor> {

    HandlerRegistration addPropertyValueChangedHandler(PropertyValuePropertyChangedHandler handler);

    HandlerRegistration addPropertyValueChangedHandler(PropertyValueValueChangedHandler handler);

    PropertyValueState getPropertyValueState();

    Optional<OWLPropertyData> getPropertyFieldValue();

    String getPropertyFieldLexicalValue();

    Optional<OWLPrimitiveData> getValueFieldValue();

    void setPropertyValueState(PropertyValueState propertyValueState);

    void setAllowedPropertyTypes(Collection<PrimitiveType> types);

    void setAllowedValueTypes(Collection<PrimitiveType> types);

    void setValuePlaceholder(String placeholder);


    void setPropertyFieldFreshEntitiesHandler(FreshEntitiesHandler freshEntitiesHandler);

    void setValueFieldFreshEntitiesHandler(FreshEntitiesHandler freshEntitiesHandler);

    void setPropertyFieldSuggestMode(PrimitiveDataEditorSuggestOracleMode suggestMode);

    void setValueFieldSuggestMode(PrimitiveDataEditorSuggestOracleMode suggestMode);


    void coercePropertyFieldToType(EntityType<?> primitiveType);

}
