package edu.stanford.bmir.protege.web.server.index;

import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

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
public class ProjectOntologiesIndexImpl implements ProjectOntologiesIndex {

    @Nonnull
    private final OWLOntologyManager projectOntologyManager;

    @Inject
    public ProjectOntologiesIndexImpl(@Nonnull OWLOntology rootOntology) {
        this.projectOntologyManager = checkNotNull(checkNotNull(rootOntology).getOWLOntologyManager());
    }

    @Nonnull
    @Override
    public Stream<OWLOntologyID> getOntologyIds() {
        return projectOntologyManager.getOntologies().stream().map(OWLOntology::getOntologyID);
    }
}
