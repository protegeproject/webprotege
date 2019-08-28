package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.project.chg.ChangeManager;
import edu.stanford.bmir.protege.web.shared.obo.OBOXRef;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static edu.stanford.bmir.protege.web.server.obo.OboUtil.isXRefProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22 Jun 2017
 */
public class TermXRefsManager {

    @Nonnull
    private final ChangeManager changeManager;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final XRefExtractor xrefExtractor;

    @Nonnull
    private final AnnotationToXRefConverter xrefConverter;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologies;

    @Nonnull
    private final AnnotationAssertionAxiomsBySubjectIndex annotationAssertions;

    @Inject
    public TermXRefsManager(@Nonnull ChangeManager changeManager,
                            @Nonnull OWLDataFactory dataFactory,
                            @Nonnull XRefExtractor xrefExtractor,
                            @Nonnull AnnotationToXRefConverter xrefConverter,
                            @Nonnull ProjectOntologiesIndex projectOntologies,
                            @Nonnull AnnotationAssertionAxiomsBySubjectIndex annotationAssertions) {
        this.changeManager = changeManager;
        this.dataFactory = dataFactory;
        this.xrefExtractor = xrefExtractor;
        this.xrefConverter = xrefConverter;
        this.projectOntologies = projectOntologies;
        this.annotationAssertions = annotationAssertions;
    }

    @Nonnull
    public List<OBOXRef> getXRefs(@Nonnull OWLEntity term) {
        return xrefExtractor.getXRefs(term);
    }

    public void setXRefs(UserId userId, OWLEntity term, List<OBOXRef> xrefs) {
        var subject = term.getIRI();
        var changes = new ArrayList<OntologyChange>();
        projectOntologies.getOntologyIds()
                         .forEach(ontId -> {
                             annotationAssertions.getAxiomsForSubject(subject, ontId)
                                                 .forEach(existingAx -> {
                                                     if(isXRefProperty(existingAx.getProperty())) {
                                                         xrefs.stream()
                                                              .map(xrefConverter::toAnnotation)
                                                              .map(annotation -> toAnnotationAssertion(subject,
                                                                                                       annotation))
                                                              .map(ax -> AddAxiomChange.of(ontId, ax))
                                                              .forEach(changes::add);
                                                         var removeAx = RemoveAxiomChange.of(ontId,
                                                                                             existingAx);
                                                         changes.add(removeAx);
                                                     }
                                                 });
                         });
        changeManager.applyChanges(userId,
                                   new FixedChangeListGenerator<>(changes, term, "Edited term XRefs"));
    }

    private OWLAnnotationAssertionAxiom toAnnotationAssertion(IRI subject, OWLAnnotation annotation) {
        return dataFactory.getOWLAnnotationAssertionAxiom(
                subject,
                annotation);
    }

}
