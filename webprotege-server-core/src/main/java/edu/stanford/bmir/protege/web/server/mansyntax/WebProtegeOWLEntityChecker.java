package edu.stanford.bmir.protege.web.server.mansyntax;

import com.google.common.collect.Maps;
import edu.stanford.bmir.protege.web.server.shortform.DictionaryManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.frame.HasFreshEntities;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Map;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 18/03/2014
 */
public class WebProtegeOWLEntityChecker implements OWLEntityChecker {

    private DictionaryManager dictionaryManager;

    private Map<String, OWLEntityData> freshEntitiesMap = Maps.newHashMap();

    @Inject
    public WebProtegeOWLEntityChecker(HasFreshEntities freshEntities,
                                      DictionaryManager dictionaryManager) {
        this.dictionaryManager = dictionaryManager;
        for (OWLEntityData data : freshEntities.getFreshEntities()) {
            freshEntitiesMap.put(stripQuotes(data.getBrowserText()), data);
        }
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private <E extends OWLEntity> E getEntity(String s, EntityType<E> entityType) {
        for (String freshEntity : freshEntitiesMap.keySet()) {
            OWLEntity entity = freshEntitiesMap.get(freshEntity).getEntity();
            if (freshEntity.equals(s) && entity.getEntityType().equals(entityType)) {
                return (E) entity;
            }
        }
        return dictionaryManager.getEntities(stripQuotes(s))
                                .filter(entity -> entity.getEntityType().equals(entityType))
                                .map(entity -> (E) entity)
                                .findFirst()
                                .orElse(null);
    }

    @Override
    public OWLClass getOWLClass(@Nonnull String s) {
        return getEntity(stripQuotes(s), EntityType.CLASS);
    }

    private String stripQuotes(String s) {
        if (s.startsWith("'") && s.endsWith("'")) {
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
