package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.owlapi.manager.WebProtegeOWLManager;
import org.coode.owlapi.obo.parser.OBOOntologyFormat;
import org.obolibrary.oboformat.model.OBODoc;
import org.obolibrary.oboformat.parser.OBOFormatParser;
import org.obolibrary.oboformat.parser.OBOFormatParserException;
import org.semanticweb.owlapi.io.AbstractOWLParser;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/06/2013
 */
public class WebProtegeOBOFormatParser extends AbstractOWLParser {

    public OWLOntologyFormat parse(OWLOntologyDocumentSource documentSource, OWLOntology ontology) throws OWLParserException, IOException, OWLOntologyChangeException, UnloadableImportException {
        return parse(ontology, documentSource, new OWLOntologyLoaderConfiguration());
    }

    public OWLOntologyFormat parse(OWLOntologyDocumentSource documentSource, OWLOntology ontology, OWLOntologyLoaderConfiguration configuration) throws OWLParserException, IOException, OWLOntologyChangeException, UnloadableImportException {
        return parse(ontology, documentSource, configuration);
    }

    private OWLOntologyFormat parse(OWLOntology ont, OWLOntologyDocumentSource source, OWLOntologyLoaderConfiguration configuration) throws IOException, OWLParserException {
//        try {
//            OBOFormatParser parser = new OBOFormatParser();
//            BufferedReader reader = getBufferedReader(source, configuration);
//            OBODoc oboDoc = parser.parse(reader);
//            OWLOntologyManager tempManager = WebProtegeOWLManager.createOWLOntologyManager();
//            Obo2Owl obo2Owl = new Obo2Owl(tempManager);
//            OWLOntology tempOnt = obo2Owl.convert(oboDoc);
//            OWLOntologyManager man = ont.getOWLOntologyManager();
//            man.applyChange(new SetOntologyID(ont, tempOnt.getOntologyID()));
//            for(OWLAnnotation annotation : tempOnt.getAnnotations()) {
//                man.applyChange(new AddOntologyAnnotation(ont, annotation));
//            }
//            man.addAxioms(ont, tempOnt.getAxioms());
//            return new OBOOntologyFormat();
//        }
//        catch (OBOFormatParserException e) {
//            throw new OWLParserException(e.getMessage(), e.getLineNo(), 0);
//        }
//        catch (OWLOntologyCreationException e) {
//            throw new OWLRuntimeException(e);
//        }
        throw new RuntimeException("Commented Out");
    }


    private BufferedReader getBufferedReader(OWLOntologyDocumentSource source, OWLOntologyLoaderConfiguration configuration) throws IOException {
        if(source.isReaderAvailable()) {
            return new BufferedReader(source.getReader());
        }
        else if(source.isInputStreamAvailable()) {
            return new BufferedReader(new InputStreamReader(source.getInputStream(),"utf-8"));
        }
        else {
            return new BufferedReader(new InputStreamReader(getInputStream(source.getDocumentIRI(), configuration), "utf-8"));
        }
    }
}
