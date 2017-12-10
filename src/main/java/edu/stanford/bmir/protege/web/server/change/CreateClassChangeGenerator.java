package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.semanticweb.owlapi.model.EntityType.CLASS;

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

    @Nonnull
    private final OWLDataFactory dataFactory;


    @Inject
    public CreateClassChangeGenerator(@Nonnull String browserText,
                                      @Nonnull OWLClass superClass,
                                      @Nonnull OWLOntology rootOntology,
                                      @Nonnull OWLDataFactory dataFactory) {
        this.browserText = checkNotNull(browserText);
        this.superClass = checkNotNull(superClass);
        this.rootOntology = checkNotNull(rootOntology);
        this.dataFactory = checkNotNull(dataFactory);
    }

    @Override
    public OntologyChangeList<OWLClass> generateChanges(ChangeGenerationContext context) {
        OWLClass freshClass = DataFactory.getFreshOWLEntity(CLASS, browserText, dataFactory);

        OntologyChangeList.Builder<OWLClass> builder = new OntologyChangeList.Builder<>();
        builder.addAxiom(rootOntology, dataFactory.getOWLDeclarationAxiom(freshClass));
        builder.addAxiom(rootOntology, dataFactory.getOWLSubClassOfAxiom(freshClass, superClass));

        return builder.build(freshClass);
    }

    @Override
    public OWLClass getRenamedResult(OWLClass result, RenameMap renameMap) {
        return renameMap.getRenamedEntity(result);
    }
}
