package edu.stanford.bmir.protege.web.server.change;

import edu.stanford.bmir.protege.web.server.owlapi.RenameMap;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
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

    private OWLOntology rootOntology;

    public FindAndReplaceIRIPrefixChangeGenerator(String fromPrefix, String toPrefix, OWLOntology rootOntology) {
        this.fromPrefix = checkNotNull(fromPrefix);
        this.toPrefix = checkNotNull(toPrefix);
        this.rootOntology = checkNotNull(rootOntology);
    }

    @Override
    public OntologyChangeList<Void> generateChanges(ChangeGenerationContext context) {
        OntologyChangeList.Builder<Void> builder = OntologyChangeList.builder();
        Map<OWLEntity, IRI> renameMap = new HashMap<OWLEntity, IRI>();
        for(OWLEntity entity : rootOntology.getSignature(Imports.INCLUDED)) {
            if(!entity.isBuiltIn()) {
                IRI iri = entity.getIRI();
                String iriString = iri.toString();
                if(iriString.startsWith(fromPrefix)) {
                    IRI toIRI = IRI.create(toPrefix + iri.subSequence(fromPrefix.length(), iri.length()));
                    renameMap.put(entity, toIRI);
                }
            }
        }
        OWLEntityRenamer entityRenamer = new OWLEntityRenamer(rootOntology.getOWLOntologyManager(),
                                                              rootOntology.getImportsClosure());
        List<OWLOntologyChange> changeList = entityRenamer.changeIRI(renameMap);
        builder.addAll(changeList);
        return builder.build();
    }

    @Override
    public Void getRenamedResult(Void result, RenameMap renameMap) {
        return result;
    }
}
