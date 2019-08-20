package edu.stanford.bmir.protege.web.server.change.matcher;

import org.semanticweb.owlapi.change.AddAxiomData;
import org.semanticweb.owlapi.change.OWLOntologyChangeData;
import org.semanticweb.owlapi.change.RemoveAxiomData;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import java.util.List;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14 Mar 2017
 */
public class SubClassOfWithFreshEntitiesMatcher {
//
//    private final OWLObjectStringFormatter formatter;
//
//    @Inject
//    public SubClassOfWithFreshEntitiesMatcher(OWLObjectStringFormatter formatter) {
//        this.formatter = formatter;
//    }
//
//    @Override
//    public Optional<ChangeSummary> getDescription(List<OWLOntologyChangeData> changeData) {
//        Set<PropertyFiller> added = addedPropertyFillers(changeData)
//                .collect(Collectors.toSet());
//        if (added.size() != 1) {
//            return Optional.empty();
//        }
//        PropertyFiller addedPropertyValue = added.iterator().next();
//        Set<PropertyFiller> removed = removePropertyFillers(changeData)
//                .collect(Collectors.toSet());
//        Optional<OWLEntity> declaredFiller = addedDeclarationAxioms(changeData)
//                .filter(ax -> ax.getEntity().equals(addedPropertyValue.getFiller().get()))
//                .map(OWLDeclarationAxiom::getEntity)
//                .findFirst();
//        Optional<OWLEntity> declaredProperty = addedDeclarationAxioms(changeData)
//                .map(OWLDeclarationAxiom::getEntity)
//                .filter(entity -> entity.equals(addedPropertyValue.getProperty().get()))
//                .findFirst();
//        if (!declaredFiller.isPresent() && !declaredProperty.isPresent()) {
//            return Optional.empty();
//        }
//        String created = "";
//        if (declaredProperty.isPresent()) {
//            created += formatter.formatString("Created %s inline.  " , declaredProperty.get());
//        }
//        if (declaredFiller.isPresent()) {
//            created += formatter.formatString("Created %s inline.  " , declaredFiller.get());
//        }
//        if (removed.isEmpty()) {
//            var msg = formatter.formatString("Added relationship (%s  %s) on %s.  %s" ,
//                                    addedPropertyValue.getProperty().get(),
//                                    addedPropertyValue.getFiller().get(),
//                                    addedPropertyValue.getSubject(),
//                                    created);
//            return Optional.of(ChangeSummary.get(msg));
//        }
//        else {
//            var msg = formatter.formatString("Edited relationship (%s  %s) on %s.  %s" ,
//                                    addedPropertyValue.getProperty().get(),
//                                    addedPropertyValue.getFiller().get(),
//                                    addedPropertyValue.getSubject(),
//                                    created);
//            return Optional.of(ChangeSummary.get(msg));
//        }
//
//    }

    private static Stream<OWLAxiom> addedAxioms(List<OWLOntologyChangeData> change) {
        return change.stream()
                     .filter(data -> data instanceof AddAxiomData)
                     .map(data -> (OWLAxiom) data.getItem());
    }

    private static Stream<OWLAxiom> removedAxiom(List<OWLOntologyChangeData> changes) {
        return changes.stream()
                      .filter(data -> data instanceof RemoveAxiomData)
                      .map(data -> (OWLAxiom) data.getItem());
    }

    private static Stream<OWLDeclarationAxiom> addedDeclarationAxioms(List<OWLOntologyChangeData> change) {
        return addedAxioms(change)
                .filter(ax -> ax instanceof OWLDeclarationAxiom)
                .map(ax -> ((OWLDeclarationAxiom) ax));
    }

    private static Stream<PropertyFiller> addedPropertyFillers(List<OWLOntologyChangeData> change) {
        return toPropertyFillerExtractors(addedAxioms(change));
    }

    private static Stream<PropertyFiller> removePropertyFillers(List<OWLOntologyChangeData> change) {
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
