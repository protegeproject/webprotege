package edu.stanford.bmir.protege.web.server.mansyntax;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.frame.HasFreshEntities;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;

import java.util.Map;
import java.util.Set;

/**
* @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
*/
public class WebProtegeOWLEntityChecker implements OWLEntityChecker {

    private BidirectionalShortFormProvider shortFormProvider;

    private Map<String, OWLEntityData> freshEntitiesMap = Maps.newHashMap();

    @Inject
    public WebProtegeOWLEntityChecker(BidirectionalShortFormProvider shortFormProvider, HasFreshEntities freshEntities) {
        this.shortFormProvider = shortFormProvider;
        for(OWLEntityData data : freshEntities.getFreshEntities()) {
            freshEntitiesMap.put(stripQuotes(data.getBrowserText()), data);
        }
    }

    @SuppressWarnings("unchecked")
    private <E extends OWLEntity> E getEntity(String s, EntityType<E> entityType) {
        for(String freshEntity : freshEntitiesMap.keySet()) {
            OWLEntity entity = freshEntitiesMap.get(freshEntity).getEntity();
            if(freshEntity.equals(s) && entity.getEntityType().equals(entityType)) {
                return (E) entity;
            }
        }
        for(OWLEntity entity : shortFormProvider.getEntities(stripQuotes(s))) {
            if(entity.getEntityType() == entityType) {
                return (E) entity;
            }
        }
        return null;
    }

    @Override
    public OWLClass getOWLClass(String s) {
        return getEntity(stripQuotes(s), EntityType.CLASS);
    }

    private String stripQuotes(String s) {
        if(s.startsWith("'") && s.endsWith("'")) {
            return s.substring(1, s.length() - 1);
        }
        else {
            return s;
        }
    }

    @Override
    public OWLObjectProperty getOWLObjectProperty(String s) {
        return getEntity(stripQuotes(s), EntityType.OBJECT_PROPERTY);
    }

    @Override
    public OWLDataProperty getOWLDataProperty(String s) {
        return getEntity(stripQuotes(s), EntityType.DATA_PROPERTY);
    }

    @Override
    public OWLNamedIndividual getOWLIndividual(String s) {
        return getEntity(stripQuotes(s), EntityType.NAMED_INDIVIDUAL);
    }

    @Override
    public OWLDatatype getOWLDatatype(String s) {
        return getEntity(stripQuotes(s), EntityType.DATATYPE);
    }

    @Override
    public OWLAnnotationProperty getOWLAnnotationProperty(String s) {
        return getEntity(stripQuotes(s), EntityType.ANNOTATION_PROPERTY);
    }
}
