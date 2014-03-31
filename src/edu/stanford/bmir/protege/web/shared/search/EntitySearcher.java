package edu.stanford.bmir.protege.web.shared.search;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;

import java.util.List;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 22/03/2014
 */
public class EntitySearcher {

    private String searchString;

    private ImmutableSet<EntityType<?>> entityTypes;

    private BidirectionalShortFormProvider shortFormProvider;

    public EntitySearcher(BidirectionalShortFormProvider shortFormProvider, String searchString, Set<EntityType<?>> entityTypes) {
        this.searchString = searchString;
        this.entityTypes = ImmutableSet.copyOf(entityTypes);
        this.shortFormProvider = shortFormProvider;
    }

    public List<EntitySearchResult> getMatches(String searchIn, int limit) {
        List<EntitySearchResult> result = Lists.newArrayList();
        for(String shortForm : shortFormProvider.getShortForms()) {
            EntityNameMatcher entityNameMatcher = new EntityNameMatcher(searchString);
            Optional<EntityNameMatchResult> match = entityNameMatcher.findIn(shortForm);
            if(match.isPresent()) {
                Set<OWLEntity> entities = shortFormProvider.getEntities(shortForm);
                for(OWLEntity entity : entities) {
                    if(entityTypes.contains(entity.getEntityType())) {
                        result.add(new EntitySearchResult(match.get(), entity, shortForm));
                        if(result.size() == limit) {
                            return result;
                        }
                    }
                }
            }
        }
        return result;
    }
}
