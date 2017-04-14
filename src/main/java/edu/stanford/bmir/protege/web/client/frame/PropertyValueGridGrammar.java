package edu.stanford.bmir.protege.web.client.frame;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import edu.stanford.bmir.protege.web.shared.PrimitiveType;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static edu.stanford.bmir.protege.web.shared.PrimitiveType.*;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 03/12/2012
 */
public class PropertyValueGridGrammar {

    private Set<PrimitiveType> fillerTypes = Sets.newLinkedHashSet();

    private Multimap<PrimitiveType, PrimitiveType> productionMap = ArrayListMultimap.create();

    public void addProduction(PrimitiveType propertyType, PrimitiveType fillerType) {
        productionMap.put(propertyType, fillerType);
        fillerTypes.add(fillerType);
    }

    public Collection<PrimitiveType> getPropertyTypes() {
        return new LinkedHashSet<>(productionMap.keySet());
    }

    public Collection<PrimitiveType> getValueTypes() {
        return fillerTypes;
    }

    public Collection<PrimitiveType> getValueTypesForPropertyType(PrimitiveType propertyType) {
        Collection<PrimitiveType> values =  productionMap.get(propertyType);
        if(values == null) {
            return Collections.emptyList();
        }
        return new LinkedHashSet<>(values);
    }

    public static PropertyValueGridGrammar getAnnotationsGrammar() {
        PropertyValueGridGrammar grammar = new PropertyValueGridGrammar();
        grammar.addProduction(ANNOTATION_PROPERTY, LITERAL);
        grammar.addProduction(ANNOTATION_PROPERTY, IRI);
//        grammar.addProduction(ANNOTATION_PROPERTY, CLASS);
//        grammar.addProduction(ANNOTATION_PROPERTY, OBJECT_PROPERTY);
//        grammar.addProduction(ANNOTATION_PROPERTY, DATA_PROPERTY);
//        grammar.addProduction(ANNOTATION_PROPERTY, ANNOTATION_PROPERTY);
//        grammar.addProduction(ANNOTATION_PROPERTY, NAMED_INDIVIDUAL);
//        grammar.addProduction(ANNOTATION_PROPERTY, DATA_TYPE);
        return grammar;
    }

    public static PropertyValueGridGrammar getClassGrammar() {
        PropertyValueGridGrammar grammar = new PropertyValueGridGrammar();
        grammar.addProduction(OBJECT_PROPERTY, CLASS);
        grammar.addProduction(OBJECT_PROPERTY, NAMED_INDIVIDUAL);
        grammar.addProduction(DATA_PROPERTY, DATA_TYPE);
        grammar.addProduction(DATA_PROPERTY, LITERAL);
        return grammar;
    }

    public static PropertyValueGridGrammar getNamedIndividualGrammar() {
        PropertyValueGridGrammar grammar = new PropertyValueGridGrammar();
        grammar.addProduction(ANNOTATION_PROPERTY, LITERAL);
        grammar.addProduction(ANNOTATION_PROPERTY, IRI);
        grammar.addProduction(OBJECT_PROPERTY, NAMED_INDIVIDUAL);
        grammar.addProduction(OBJECT_PROPERTY, CLASS);
        grammar.addProduction(DATA_PROPERTY, LITERAL);
        grammar.addProduction(DATA_PROPERTY, DATA_TYPE);
        return grammar;
    }
}
