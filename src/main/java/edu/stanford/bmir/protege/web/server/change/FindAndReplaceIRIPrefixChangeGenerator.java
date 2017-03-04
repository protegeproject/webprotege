package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.util.OWLEntityRenamer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 11/09/2013
 */
public class FindAndReplaceIRIPrefixChangeGenerator implements ChangeListGenerator<Void> {

    private String fromPrefix;

    private String toPrefix;

    public FindAndReplaceIRIPrefixChangeGenerator(String fromPrefix, String toPrefix) {
        this.fromPrefix = checkNotNull(fromPrefix);
        this.toPrefix = checkNotNull(toPrefix);
    }

    @Override
    public OntologyChangeList<Void> generateChanges(Project project, ChangeGenerationContext context) {
        OntologyChangeList.Builder<Void> builder = OntologyChangeList.builder();
        Map<OWLEntity, IRI> renameMap = new HashMap<OWLEntity, IRI>();
        for(OWLEntity entity : project.getRootOntology().getSignature(Imports.INCLUDED)) {
            if(!entity.isBuiltIn()) {
                IRI iri = entity.getIRI();
                String iriString = iri.toString();
                if(iriString.startsWith(fromPrefix)) {
                    StringBuilder sb = new StringBuilder(toPrefix);
                    sb.append(iri.subSequence(fromPrefix.length(), iri.length()));
                    IRI toIRI = IRI.create(sb.toString());
                    renameMap.put(entity, toIRI);
                }
            }
        }
        OWLEntityRenamer entityRenamer = new OWLEntityRenamer(project.getRootOntology().getOWLOntologyManager(), project.getRootOntology().getImportsClosure());
        List<OWLOntologyChange> changeList = entityRenamer.changeIRI(renameMap);
        builder.addAll(changeList);
        return builder.build();
    }

    @Override
    public Void getRenamedResult(Void result, RenameMap renameMap) {
        return result;
    }
}
