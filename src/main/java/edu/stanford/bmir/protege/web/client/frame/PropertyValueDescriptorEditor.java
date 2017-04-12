package edu.stanford.bmir.protege.web.client.frame;

import com.google.common.base.Optional;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasEnabled;
import edu.stanford.bmir.protege.web.client.editor.ValueEditor;
import edu.stanford.bmir.protege.web.client.primitive.FreshEntitiesHandler;
import edu.stanford.bmir.protege.web.client.primitive.FreshEntitySuggestStrategy;
import edu.stanford.bmir.protege.web.shared.HasDeleteable;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;
import edu.stanford.bmir.protege.web.shared.frame.PropertyValueDescriptor;
import edu.stanford.bmir.protege.web.shared.frame.State;
import org.semanticweb.owlapi.model.EntityType;

import java.util.Collection;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 04/12/2012
 */
public interface PropertyValueDescriptorEditor extends ValueEditor<PropertyValueDescriptor>, HasEnabled, HasDeleteable {

    HandlerRegistration addPropertyValueChangedHandler(PropertyValuePropertyChangedHandler handler);

    HandlerRegistration addPropertyValueChangedHandler(PropertyValueValueChangedHandler handler);

    State getState();

    Optional<OWLPropertyData> getPropertyFieldValue();

    String getPropertyFieldLexicalValue();

    Optional<OWLPrimitiveData> getValueFieldValue();

    void setState(State state);

    void setAllowedPropertyTypes(Collection<PrimitiveType> types);

    void setAllowedValueTypes(Collection<PrimitiveType> types);

    void setValuePlaceholder(String placeholder);


    void setPropertyFieldFreshEntitiesHandler(FreshEntitiesHandler freshEntitiesHandler);

    void setValueFieldFreshEntitiesHandler(FreshEntitiesHandler freshEntitiesHandler);

    void setPropertyFieldSuggestStrategy(FreshEntitySuggestStrategy suggestStrategy);

    void setValueFieldSuggestStrategy(FreshEntitySuggestStrategy suggestStrategy);


    void coercePropertyFieldToType(EntityType<?> primitiveType);

}
