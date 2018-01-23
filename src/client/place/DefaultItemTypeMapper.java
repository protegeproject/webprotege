package edu.stanford.bmir.protege.web.client.place;

import com.google.common.collect.Maps;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import java.util.Map;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 19/05/2014
 */
public class DefaultItemTypeMapper implements ItemTokenParser.ItemTypeMapper {

//    private OWLDataFactory dataFactory;
//
//    private DefaultPrefixManager defaultPrefixManager;

    private Map<String, ItemTokenParser.ItemParser> typeMapper = Maps.newHashMap();

    public DefaultItemTypeMapper(OWLDataFactory dataFactory, DefaultPrefixManager defaultPrefixManager) {
        setTypeParser(OWLClassItem.getType(), new OWLEntityItemParser<OWLClass>(
                EntityType.CLASS,
                dataFactory, defaultPrefixManager
        ) {
            @Override
            protected Item<OWLClass> createItem(OWLClass entity) {
                return new OWLClassItem(entity);
            }
        });

        setTypeParser(OWLObjectPropertyItem.getType(), new OWLEntityItemParser<OWLObjectProperty>(
                EntityType.OBJECT_PROPERTY,
                dataFactory, defaultPrefixManager
        ) {
            @Override
            protected Item<OWLObjectProperty> createItem(OWLObjectProperty entity) {
                return new OWLObjectPropertyItem(entity);
            }
        });

        setTypeParser(OWLDataPropertyItem.getType(), new OWLEntityItemParser<OWLDataProperty>(
                EntityType.DATA_PROPERTY,
                dataFactory, defaultPrefixManager
        ) {
            @Override
            protected Item<OWLDataProperty> createItem(OWLDataProperty entity) {
                return new OWLDataPropertyItem(entity);
            }
        });

        setTypeParser(OWLAnnotationPropertyItem.getType(), new OWLEntityItemParser<OWLAnnotationProperty>(
                EntityType.ANNOTATION_PROPERTY,
                dataFactory, defaultPrefixManager
        ) {
            @Override
            protected Item<OWLAnnotationProperty> createItem(OWLAnnotationProperty entity) {
                return new OWLAnnotationPropertyItem(entity);
            }
        });

        setTypeParser(OWLNamedIndividualItem.getType(), new OWLEntityItemParser<OWLNamedIndividual>(
                EntityType.NAMED_INDIVIDUAL,
                dataFactory, defaultPrefixManager
        ) {
            @Override
            protected Item<OWLNamedIndividual> createItem(OWLNamedIndividual entity) {
                return new OWLNamedIndividualItem(entity);
            }
        });


    }


    public ItemTokenParser.ItemParser getParser(String type) {
        ItemTokenParser.ItemParser parser = typeMapper.get(type);
        return parser;
    }


    public void setTypeParser(Item.Type type, ItemTokenParser.ItemParser parser) {
        typeMapper.put(type.getName(), parser);
    }
}
