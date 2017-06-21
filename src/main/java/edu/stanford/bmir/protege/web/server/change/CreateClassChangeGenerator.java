package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class CreateClassChangeGenerator implements ChangeListGenerator<OWLClass> {

    @Nonnull
    private final String browserText;

    @Nonnull
    private final OWLClass superClass;

    @Nonnull
    private final OWLOntology rootOntology;

    @Inject
    public CreateClassChangeGenerator(@Nonnull String browserText,
                                      @Nonnull OWLClass superClass,
                                      @Nonnull OWLOntology rootOntology) {
        this.browserText = browserText;
        this.superClass = superClass;
        this.rootOntology = rootOntology;
    }

    @Override
    public OntologyChangeList<OWLClass> generateChanges(ChangeGenerationContext context) {
        OWLClass freshClass = DataFactory.getFreshOWLEntity(EntityType.CLASS, browserText);

        OntologyChangeList.Builder<OWLClass> builder = new OntologyChangeList.Builder<>();
        builder.addAxiom(rootOntology, DataFactory.get().getOWLDeclarationAxiom(freshClass));
        builder.addAxiom(rootOntology, DataFactory.get().getOWLSubClassOfAxiom(freshClass, superClass));

        return builder.build(freshClass);
    }

    @Override
    public OWLClass getRenamedResult(OWLClass result, RenameMap renameMap) {
        return renameMap.getRenamedEntity(result);
    }
}
