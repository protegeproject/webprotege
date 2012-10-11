package edu.stanford.bmir.protege.web.server;

import java.util.List;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 23/08/2012
 */
public interface AttributesStore {
    

    String getStringValue(String propertyName, String defaultValue);

    List<String> getStringValues(String propertyName, List<String> defaultValues);

    void setStringValue(String propertyName, String value);
    
    void setStringValues(String propertyName, List<String> values);
    
    

    Integer getIntegerValue(String propertyName, Integer defaultValue) throws NumberFormatException;

    List<Integer> getIntegerValues(String propertyName, List<Integer> defaultValues) throws NumberFormatException;

    void setIntegerValue(String propertyName, Integer value);

    void setIntegerValues(String propertyName, List<Integer> values);




    Long getLongValue(String propertyName, Long defaultValue) throws NumberFormatException;

    List<Long> getLongValues(String propertyName, List<Long> defaultValues) throws NumberFormatException;

    void setLongValue(String propertyName, Long value);

    void setLongValues(String propertyName, List<Long> values);



    Double getDoubleValue(String propertyName, Double defaultValue) throws NumberFormatException;

    List<Double> getDoubleValues(String propertyName, List<Double> defaultValues) throws NumberFormatException;

    void setDoubleValue(String propertyName, Double value);

    void setDoubleValues(String propertyName, List<Double> values);



    Boolean getBooleanValue(String propertyName, Boolean defaultValue);
    
    void setBooleanValue(String propertyName, Boolean value);

    

}
