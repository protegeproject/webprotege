package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.change.FixedChangeListGenerator;
import edu.stanford.bmir.protege.web.server.change.FixedMessageChangeDescriptionGenerator;
import edu.stanford.bmir.protege.web.server.inject.project.RootOntology;
import edu.stanford.bmir.protege.web.server.project.ChangeManager;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.shared.obo.OBOXRef;
import edu.stanford.bmir.protege.web.shared.user.NotSignedInException;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.semanticweb.owlapi.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final OWLOntology rootOntology;

    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final XRefExtractor xrefExtractor;

    @Nonnull
    private final AnnotationToXRefConverter annotationToXRefConverter;

    @Inject
    public TermXRefsManager(@Nonnull ChangeManager changeManager,
                            @Nonnull @RootOntology OWLOntology rootOntology,
                            @Nonnull OWLDataFactory dataFactory,
                            @Nonnull XRefExtractor xrefExtractor,
                            @Nonnull AnnotationToXRefConverter annotationToXRefConverter) {
        this.changeManager = changeManager;
        this.rootOntology = rootOntology;
        this.dataFactory = dataFactory;
        this.xrefExtractor = xrefExtractor;
        this.annotationToXRefConverter = annotationToXRefConverter;
    }

    @Nonnull
    public List<OBOXRef> getXRefs(@Nonnull OWLEntity term) {
        return xrefExtractor.getXRefs(term);
    }

    public void setXRefs(UserId userId, OWLEntity term, List<OBOXRef> xrefs) throws NotSignedInException {
        IRI subject = term.getIRI();
        Set<OWLAnnotation> annotations = xrefs.stream()
                .map(annotationToXRefConverter::toAnnotation)
                .collect(Collectors.toSet());
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        // Remove OLD
        for (OWLAnnotationAssertionAxiom ax : rootOntology.getAnnotationAssertionAxioms(subject)) {
            if (isXRefProperty(ax.getProperty())) {
                changes.add(new RemoveAxiom(rootOntology, ax));
            }
        }
        // Add NEW
        for (OWLAnnotation annotation : annotations) {
            changes.add(new AddAxiom(rootOntology, dataFactory.getOWLAnnotationAssertionAxiom(subject, annotation)));
        }
        changeManager.applyChanges(userId,
                                   new FixedChangeListGenerator<>(changes),
                                   new FixedMessageChangeDescriptionGenerator<>("Edited term XRefs"));
    }

}
