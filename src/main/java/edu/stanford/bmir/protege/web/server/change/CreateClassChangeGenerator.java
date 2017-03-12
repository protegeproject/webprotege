package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLClass;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 22/02/2013
 */
public class CreateClassChangeGenerator implements ChangeListGenerator<OWLClass> {


    private String browserText;

    private OWLClass superClass;

    public CreateClassChangeGenerator(String browserText, OWLClass superClass) {
        this.browserText = checkNotNull(browserText);
        this.superClass = checkNotNull(superClass);
    }

    @Override
    public OntologyChangeList<OWLClass> generateChanges(Project project, ChangeGenerationContext context) {
        OWLClass freshClass = DataFactory.getFreshOWLEntity(EntityType.CLASS, browserText);

        OntologyChangeList.Builder<OWLClass> builder = new OntologyChangeList.Builder<OWLClass>();
        builder.addAxiom(project.getRootOntology(), DataFactory.get().getOWLDeclarationAxiom(freshClass));
        builder.addAxiom(project.getRootOntology(), DataFactory.get().getOWLSubClassOfAxiom(freshClass, superClass));

        return builder.build(freshClass);
    }

    @Override
    public OWLClass getRenamedResult(OWLClass result, RenameMap renameMap) {
        return renameMap.getRenamedEntity(result);
    }
}
