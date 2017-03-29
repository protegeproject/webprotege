package edu.stanford.bmir.protege.web.server.change.matcher;

import edu.stanford.bmir.protege.web.server.change.ChangeApplicationResult;
import edu.stanford.bmir.protege.web.server.owlapi.OWLObjectStringFormatter;
import org.semanticweb.owlapi.model.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Mar 2017
 */
public class SubClassOfWithFreshEntitiesMatcher implements ChangeMatcher {

    private final OWLObjectStringFormatter formatter;

    @Inject
    public SubClassOfWithFreshEntitiesMatcher(OWLObjectStringFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public Optional<String> getDescription(ChangeApplicationResult<?> result) {
        Set<PropertyFiller> added = addedPropertyFillers(result.getChangeList())
                .collect(Collectors.toSet());
        if (added.size() != 1) {
            return Optional.empty();
        }
        PropertyFiller addedPropertyValue = added.iterator().next();
        Set<PropertyFiller> removed = removePropertyFillers(result.getChangeList())
                .collect(Collectors.toSet());
        Optional<OWLEntity> declaredFiller = addedDeclarationAxioms(result.getChangeList())
                .filter(ax -> ax.getEntity().equals(addedPropertyValue.getFiller().get()))
                .map(OWLDeclarationAxiom::getEntity)
                .findFirst();
        Optional<OWLEntity> declaredProperty = addedDeclarationAxioms(result.getChangeList())
                .map(OWLDeclarationAxiom::getEntity)
                .filter(entity -> entity.equals(addedPropertyValue.getProperty().get()))
                .findFirst();
        if (!declaredFiller.isPresent() && !declaredProperty.isPresent()) {
            return Optional.empty();
        }
        String created = "";
        if (declaredProperty.isPresent()) {
            created += formatter.formatString("Created %s inline.  " , declaredProperty.get());
        }
        if (declaredFiller.isPresent()) {
            created += formatter.formatString("Created %s inline.  " , declaredFiller.get());
        }
        if (removed.isEmpty()) {
            return formatter.format("Added relationship (%s  %s) on %s.  %s" ,
                                    addedPropertyValue.getProperty().get(),
                                    addedPropertyValue.getFiller().get(),
                                    addedPropertyValue.getSubClass(),
                                    created);
        }
        else {
            return formatter.format("Edited relationship (%s  %s) on %s.  %s" ,
                                    addedPropertyValue.getProperty().get(),
                                    addedPropertyValue.getFiller().get(),
                                    addedPropertyValue.getSubClass(),
                                    created);
        }
    }

    private static Stream<OWLAxiom> addedAxioms(List<OWLOntologyChange> change) {
        return change.stream()
                     .filter(OWLOntologyChange::isAddAxiom)
                     .map(OWLOntologyChange::getAxiom);
    }

    private static Stream<OWLAxiom> removedAxiom(List<OWLOntologyChange> changes) {
        return changes.stream()
                      .filter((OWLOntologyChange::isRemoveAxiom))
                      .map(OWLOntologyChange::getAxiom);
    }

    private static Stream<OWLDeclarationAxiom> addedDeclarationAxioms(List<OWLOntologyChange> change) {
        return addedAxioms(change)
                .filter(ax -> ax instanceof OWLDeclarationAxiom)
                .map(ax -> ((OWLDeclarationAxiom) ax));
    }

    private static Stream<PropertyFiller> addedPropertyFillers(List<OWLOntologyChange> change) {
        return toPropertyFillerExtractors(addedAxioms(change));
    }

    private static Stream<PropertyFiller> removePropertyFillers(List<OWLOntologyChange> change) {
        return toPropertyFillerExtractors(removedAxiom(change));
    }

    private static Stream<PropertyFiller> toPropertyFillerExtractors(Stream<OWLAxiom> axiomStream) {
        return axiomStream
                .filter(ax -> ax instanceof OWLSubClassOfAxiom)
                .map(ax -> ((OWLSubClassOfAxiom) ax))
                .filter(ax -> !ax.getSubClass().isAnonymous())
                .map(ax -> new PropertyFiller(ax.getSubClass(), ax.getSuperClass()))
                .filter(ext -> ext.isPropertyAndFillerPresent());
    }
}
