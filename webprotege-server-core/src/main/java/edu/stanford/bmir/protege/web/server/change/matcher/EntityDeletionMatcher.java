package edu.stanford.bmir.protege.web.server.change.matcher;

import edu.stanford.bmir.protege.web.server.change.description.DeletedEntities;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.change.RemoveAxiomData;
import org.semanticweb.owlapi.change.RemoveOntologyAnnotationData;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableSet.toImmutableSet;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2018-12-04
 */
public class EntityDeletionMatcher implements ChangeMatcher {

    @Nonnull
    private final OWLObjectStringFormatter formatter;

    @Inject
    public EntityDeletionMatcher(@Nonnull OWLObjectStringFormatter formatter) {
        this.formatter = checkNotNull(formatter);
    }

    @Override
    public Optional<ChangeSummary> getDescription(List<OWLOntologyChangeData> changeData) {
        // All changes must be removes
        var nonRemovalChange = changeData.stream()
                .anyMatch(EntityDeletionMatcher::isNonRemovalChange);
        if(nonRemovalChange) {
            return Optional.empty();
        }
        // Must be entity declarations that are removed
        var removedEntities = changeData
                .stream()
                .filter(EntityDeletionMatcher::isRemoveAxiomData)
                .map(data -> ((RemoveAxiomData) data).getAxiom())
                .filter(ax -> ax instanceof OWLDeclarationAxiom)
                .map(ax -> ((OWLDeclarationAxiom) ax).getEntity())
                .collect(toImmutableSet());
        if(removedEntities.isEmpty()) {
            return Optional.empty();
        }
        var msg = formatter.formatString("Deleted %s", removedEntities);
        return Optional.of(ChangeSummary.get(DeletedEntities.get(removedEntities)));
    }

    private static boolean isRemoveAxiomData(OWLOntologyChangeData data) {
        return data instanceof RemoveAxiomData;
    }

    private static boolean isNonRemovalChange(OWLOntologyChangeData data) {
        return !(isRemoveAxiomData(data) || data instanceof RemoveOntologyAnnotationData);
    }
}
