package edu.stanford.bmir.protege.web.client.primitive;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import edu.stanford.bmir.protege.web.client.library.suggest.EntitySuggestion;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/03/2014
 *
 * Suggest new entities are created as instances, subclasses or subproperties of a list of possible types.
 */
public class AugmentedFreshEntitiesSuggestStrategy implements FreshEntitySuggestStrategy {

    private List<Optional<OWLEntityData>> auxiliaryTypes;

    public AugmentedFreshEntitiesSuggestStrategy(Collection<OWLEntityData> auxiliaryTypes) {
        this.auxiliaryTypes = Lists.newArrayList();
        for(OWLEntityData auxiliaryType : auxiliaryTypes) {
            this.auxiliaryTypes.add(Optional.of(auxiliaryType));
        }
        this.auxiliaryTypes.add(Optional.absent());
    }

    @Override
    public FreshEntitySuggestMode getMode() {
        return FreshEntitySuggestMode.SUGGEST_CREATE_FRESH_ENTITIES;
    }

    @Override
    public List<EntitySuggestion> getSuggestions(String query, List<EntityType<?>> suggestedTypes) {
        List<EntitySuggestion> result = Lists.newArrayList();
        for(EntityType<?> type : suggestedTypes) {
            for (Optional<OWLEntityData> auxiliaryType : auxiliaryTypes) {
                Optional<FreshEntitySuggestion> suggestion = getSuggestion(query, type, auxiliaryType);
                if (suggestion.isPresent()) {
                    result.add(suggestion.get());
                }
            }
            OWLEntity entity = DataFactory.getFreshOWLEntity(type, query);
            OWLEntityData entityData = DataFactory.getOWLEntityData(entity, query);
            result.add(new EntitySuggestion(entityData, formatSuggestText(query, type)));
        }
        return result;
    }

    private String formatSuggestText(String query, EntityType<?> allowedType) {
        return "<span class=\"new-keyword\">New</span> <span style=\"font-weight: bold;\">" + allowedType.getPrintName() + "</span> named " + query;
    }

    private Optional<FreshEntitySuggestion> getSuggestion(String query, EntityType<?> type, Optional<OWLEntityData> auxiliaryType) {
        // TODO: If query starts with a lowercase letter, suggest individual first?
        OWLEntity entity = DataFactory.getFreshOWLEntity(type, query);
        OWLEntityData entityData = DataFactory.getOWLEntityData(entity, query);
        if(auxiliaryType.isPresent()) {
            AuxiliaryTypeHandler auxiliaryTypeHandler = AuxiliaryTypeHandler.get(auxiliaryType.get());
            if(auxiliaryTypeHandler.isApplicableTo(type)) {
                Set<OWLAxiom> augmentingAxioms = auxiliaryTypeHandler.getAdditionalAxioms(entity);
                return Optional.of(new FreshEntitySuggestion(entityData, auxiliaryTypeHandler.getSuggestionText(entityData), augmentingAxioms));
            }
        }
        return Optional.absent();
    }
}
