package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.UnknownOWLOntologyException;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-06
 */
@ProjectSingleton
public class OntologyAnnotationsIndexImpl implements OntologyAnnotationsIndex {

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Inject
    public OntologyAnnotationsIndexImpl(@Nonnull ProjectOntologiesIndex projectOntologiesIndex) {
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
    }

    @Nonnull
    @Override
    public Stream<OWLAnnotation> getOntologyAnnotations(@Nonnull OWLOntologyID ontologyID) {
        return projectOntologiesIndex.getOntology(ontologyID)
                .map(ont -> ont.getAnnotations().stream())
                .orElseThrow(() -> new UnknownOWLOntologyException(ontologyID));
    }
}
