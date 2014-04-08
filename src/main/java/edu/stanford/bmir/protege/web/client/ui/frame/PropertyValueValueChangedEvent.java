package edu.stanford.bmir.protege.web.client.ui.frame;

import com.google.common.base.Optional;
import com.google.gwt.event.shared.GwtEvent;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 27/02/2014
 */
public class PropertyValueValueChangedEvent extends GwtEvent<PropertyValueValueChangedHandler> {

    private static final Type<PropertyValueValueChangedHandler> TYPE = new Type<PropertyValueValueChangedHandler>();

    private Optional<OWLPrimitiveData> value;

    public PropertyValueValueChangedEvent(Optional<OWLPrimitiveData> value) {
        this.value = value;
    }

    public static Type<PropertyValueValueChangedHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<PropertyValueValueChangedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PropertyValueValueChangedHandler handler) {
        handler.handlePropertyValueChanged(this);
    }
}
