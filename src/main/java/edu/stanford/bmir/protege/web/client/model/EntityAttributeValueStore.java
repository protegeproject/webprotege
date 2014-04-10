package edu.stanford.bmir.protege.web.client.model;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 15/08/2012
 */
public interface EntityAttributeValueStore<E, A> {


    /**
     * Gets the String values for a particular entity and attribute.
     * @param entity The entity
     * @param attribute The attribute
     * @param defaultValues The values that will be returned if the specified entity does not have any values
     * for the specified attribute.  May be null.
     * @return The string values for the specified entity and attribute or the values specified by the defaultValues
     * parameter if the specified entity does not have any values for the specified attribute.
     */
    List<String> getStringValues(E entity, A attribute, List<String> defaultValues);

    /**
     * A convenience method that obtains the first String value for the specified entity and specified attribute.  The behaviour
     * of this method is equivalent to calling the {@link #getStringValues(Object, Object, java.util.List)} method with
     * a list of size one.
     * @param entity The entity
     * @param attribute The attribute
     * @param defaultValue The value that will be returned if the specified entity does not have any values for the
     * specified attribute.
     * @return The first value for the specified attribute, or the value specified by the defaultValue parameter if
     * the specified entity does not have any values for the specified attribute.
     */
    String getStringValue(E entity, A attribute, String defaultValue);

    /**
     * Tests whether the specified entity has a specified String value for a given attribute.
     * @param entity The entity
     * @param attribute The attribute
     * @param value The string value to test for.
     * @return <code>true</code> if the specified entity has a String attribute value equal to the <code>value</code>
     * parameter, <code>false</code> if the specified entity does not have any attributes, <code>false</code> if the
     * specified entity does not have any String values for the specified attribute equal to the value specified by the
     * value parameter.
     */
    boolean hasStringValue(E entity, A attribute, String value);


    
    List<Integer> getIntegerValues(E entity, A attribute, List<Integer> defaultValues);

    Integer getIntegerValue(E entity, A attribute, Integer defaultValue);

    boolean hasIntegerValue(E entity, A attribute, Integer value);


    

    List<Long> getLongValues(E entity, A attribute, List<Long> defaultValues);

    Long getLongValue(E entity, A attribute, Long defaultValue);

    boolean hasLongValue(E entity, A attribute, Long value);



    List<Double> getDoubleValues(E entity, A attribute, List<Double> defaultValues);
    
    Double getDoubleValue(E entity, A attribute, Double defaultValue);

    boolean hasDoubleValue(E entity, A attribute, Double value);




    List<Float> getFloatValues(E entity, A attribute, List<Float> defaultValues);

    Float getFloatValue(E entity, A attribute, Float defaultValue);

    boolean hasFloatValue(E entity, A attribute, Float value);


    
    List<Boolean> getBooleanValues(E entity, A attribute, List<Boolean> defaultValues);
    
    Boolean getBooleanValue(E entity, A attribute, Boolean defaultValue);

    boolean hasBooleanValue(E entity, A attribute, Boolean value);

}
