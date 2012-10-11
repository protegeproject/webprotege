package edu.stanford.bmir.protege.web.client.model;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/08/2012
 */
public interface MutableEntityAttributeValueStore<E, A> {

    void setStringValue(E entity, A attribute, String value);
    
    void setStringValues(E entity, A attribute, List<String> values);
    
    void clearStringValue(E entity, A attribute, String value);
    
    void clearStringValues(E entity, A attribute);
    
    

    void setIntegerValue(E entity, A attribute, Integer value);

    void setIntegerValues(E entity, A attribute, List<Integer> values);

    void clearIntegerValues(E entity, A attribute);

    void clearIntegerValue(E entity, A attribute, Integer value);


    
    void setLongValue(E entity, A attribute, Long value);
    
    void setLongValues(E entity, A attribute, List<Long> values);

    void clearLongValues(E entity, A attribute);

    void clearLongValue(E entity, A attribute, Long value);



    void setDoubleValue(E entity, A attribute, Double value);

    void setDoubleValues(E entity, A attribute, List<Double> values);

    void clearDoubleValues(E entity, A attribute);

    void clearDoubleValue(E entity, A attribute, Double value);


    
    void setFloatValue(E entity, A attribute, Float value);
    
    void setFloatValues(E entity, A attribute, List<Float> values);

    void clearFloatValues(E entity, A attribute);

    void clearFloatValue(E entity, A attribute, Float value);



    void setBooleanValue(E entity, A attribute, Boolean value);

    void setBooleanValues(E entity, A attribute, List<Boolean> values);

    void clearBooleanValues(E entity, A attribute);

    void clearBooleanValue(E entity, A attribute, Boolean value);
}
