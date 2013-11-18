package edu.stanford.bmir.protege.web.client.ui.frame;

import edu.stanford.bmir.protege.web.shared.PrimitiveType;

import java.util.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/12/2012
 */
public class PropertyValueGridGrammar {

    private SortedSet<PrimitiveType> fillerTypes = new TreeSet<PrimitiveType>();

    private SortedMap<PrimitiveType, SortedSet<PrimitiveType>> productionMap = new TreeMap<PrimitiveType, SortedSet<PrimitiveType>>();

    public void addProduction(PrimitiveType propertyType, PrimitiveType fillerType) {
        SortedSet<PrimitiveType> values = productionMap.get(propertyType);
        if(values == null) {
            values = new TreeSet<PrimitiveType>();
            productionMap.put(propertyType, values);
        }
        values.add(fillerType);
        fillerTypes.add(fillerType);
    }

    public SortedSet<PrimitiveType> getPropertyTypes() {
        return new TreeSet<PrimitiveType>(productionMap.keySet());
    }

    public SortedSet<PrimitiveType> getFillerTypes() {
        return fillerTypes;
    }

    public Set<PrimitiveType> getValueTypesForPropertyType(PrimitiveType propertyType) {
        Set<PrimitiveType> values =  productionMap.get(propertyType);
        if(values == null) {
            return Collections.emptySet();
        }
        return values;
    }

    public static PropertyValueGridGrammar getAnnotationsGrammar() {
        PropertyValueGridGrammar grammar = new PropertyValueGridGrammar();
        grammar.addProduction(PrimitiveType.ANNOTATION_PROPERTY, PrimitiveType.LITERAL);
        grammar.addProduction(PrimitiveType.ANNOTATION_PROPERTY, PrimitiveType.IRI);
        return grammar;
    }

    public static PropertyValueGridGrammar getLogicalPropertiesGrammar() {
        PropertyValueGridGrammar grammar = new PropertyValueGridGrammar();
        grammar.addProduction(PrimitiveType.OBJECT_PROPERTY, PrimitiveType.CLASS);
        grammar.addProduction(PrimitiveType.OBJECT_PROPERTY, PrimitiveType.NAMED_INDIVIDUAL);
        grammar.addProduction(PrimitiveType.DATA_PROPERTY, PrimitiveType.DATA_TYPE);
        grammar.addProduction(PrimitiveType.DATA_PROPERTY, PrimitiveType.LITERAL);
        return grammar;
    }

//    public Set<PrimitiveType> getPropertyTypesForValueType(PrimitiveType valueType) {
//
//    }
}
