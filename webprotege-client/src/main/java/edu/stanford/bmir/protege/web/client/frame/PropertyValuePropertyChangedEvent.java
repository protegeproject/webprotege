package edu.stanford.bmir.protege.web.client.frame;

import com.google.gwt.event.shared.GwtEvent;
import edu.stanford.bmir.protege.web.shared.entity.OWLPropertyData;

import java.util.Optional;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 27/02/2014
 */
public class PropertyValuePropertyChangedEvent extends GwtEvent<PropertyValuePropertyChangedHandler> {

    private static final Type<PropertyValuePropertyChangedHandler> TYPE = new Type<PropertyValuePropertyChangedHandler>();

    private Optional<OWLPropertyData> value;

    public PropertyValuePropertyChangedEvent(Optional<OWLPropertyData> value) {
        this.value = value;
    }

    @Override
    public Type<PropertyValuePropertyChangedHandler> getAssociatedType() {
        return TYPE;
    }

    public static Type<PropertyValuePropertyChangedHandler> getType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PropertyValuePropertyChangedHandler handler) {
        handler.handlePropertyChanged(this);
    }
}
