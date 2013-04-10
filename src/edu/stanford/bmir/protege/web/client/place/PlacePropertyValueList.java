package edu.stanford.bmir.protege.web.client.place;

import com.google.gwt.http.client.URL;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 08/04/2013
 */
public class PlacePropertyValueList {

    private static final String PROPERTY_VALUE_SEPARATOR = "=";

    private static final String PROPERTY_VALUE_ITEM_SEPARATOR = ";";


    private final Map<String, String> propertyValues;

    private PlacePropertyValueList(Map<String, String> propertyValues) {
        this.propertyValues = propertyValues;
    }

    public String getPropertyValue(String property, String defaultValue) {
        String value = propertyValues.get(checkNotNull(property));
        if(value == null) {
            return defaultValue;
        }
        else {
            return value;
        }
    }

    public boolean containsPropertyValue(String property) {
        return propertyValues.containsKey(property);
    }

    public String render() {
        StringBuilder sb = new StringBuilder();
        for(String property : propertyValues.keySet()) {
            sb.append(URL.encodeQueryString(property));
            sb.append(PROPERTY_VALUE_SEPARATOR);
            final String value = propertyValues.get(property);
            sb.append(URL.encodeQueryString(value));
            sb.append(PROPERTY_VALUE_ITEM_SEPARATOR);
        }
        return sb.toString();
    }

    public static PlacePropertyValueList parse(String token) {
        final String globalFlag = "g";
        RegExp regExp = RegExp.compile("([^" + PROPERTY_VALUE_SEPARATOR + "]+)" + PROPERTY_VALUE_SEPARATOR + "([^" + PROPERTY_VALUE_ITEM_SEPARATOR + "]+)" + PROPERTY_VALUE_ITEM_SEPARATOR, globalFlag);
        MatchResult matchResult = regExp.exec(token);
        Builder resultBuilder = builder();
        while(matchResult != null) {
            String name = URL.decodeQueryString(matchResult.getGroup(1));
            String value = URL.decodeQueryString(matchResult.getGroup(2));
            resultBuilder.set(name, value);
            final int matchLength = matchResult.getGroup(0).length();
            final int matchStart = matchResult.getIndex();
            final int nextIndex = matchStart + matchLength;
            regExp.setLastIndex(nextIndex);
            matchResult = regExp.exec(token);
        }
        return resultBuilder.build();
    }

    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {

        private Map<String, String> propertyValueMap = new HashMap<String, String>();

        public void set(String property, String value) {
            propertyValueMap.put(checkNotNull(property), checkNotNull(value));
        }



        public PlacePropertyValueList build() {
            return new PlacePropertyValueList(propertyValueMap);
        }
    }
}
