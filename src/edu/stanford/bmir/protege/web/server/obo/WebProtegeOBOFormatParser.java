package edu.stanford.bmir.protege.web.server.obo;

import edu.stanford.bmir.protege.web.server.owlapi.manager.WebProtegeOWLManager;
import org.coode.owlapi.obo.parser.OBOOntologyFormat;
import org.obolibrary.obo2owl.Obo2Owl;
import org.obolibrary.oboformat.model.OBODoc;
import org.obolibrary.oboformat.parser.OBOFormatParser;
import org.obolibrary.oboformat.parser.OBOFormatParserException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.*;
import org.semanticweb.owlapi.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 13/06/2013
 */
public class WebProtegeOBOFormatParser extends AbstractOWLParser {

    @Override
    public OWLOntologyFormat parse(OWLOntologyDocumentSource documentSource, OWLOntology ontology) throws OWLParserException, IOException, OWLOntologyChangeException, UnloadableImportException {
        return parse(ontology, documentSource);
    }

    @Override
    public OWLOntologyFormat parse(OWLOntologyDocumentSource documentSource, OWLOntology ontology, OWLOntologyLoaderConfiguration configuration) throws OWLParserException, IOException, OWLOntologyChangeException, UnloadableImportException {
        return parse(ontology, documentSource);
    }

    private OWLOntologyFormat parse(OWLOntology ont, OWLOntologyDocumentSource source) throws IOException, OWLParserException {
        try {
            OBOFormatParser parser = new OBOFormatParser();
            BufferedReader reader = getBufferedReader(source);
            OBODoc oboDoc = parser.parse(reader);
            Obo2Owl obo2Owl = new Obo2Owl();
            OWLOntology tempOnt = obo2Owl.convert(oboDoc);
            OWLOntologyManager man = ont.getOWLOntologyManager();
            man.applyChange(new SetOntologyID(ont, tempOnt.getOntologyID()));
            for(OWLAnnotation annotation : tempOnt.getAnnotations()) {
                man.applyChange(new AddOntologyAnnotation(ont, annotation));
            }
            man.addAxioms(ont, tempOnt.getAxioms());
            System.out.println("PARSED WITH WEBPROTEGE OBOFORMAT PARSER");
            return new OBOOntologyFormat();
        }
        catch (OBOFormatParserException e) {
            System.out.println("FAILED! TO PARSE WITH WEBPROTEGE OBOFORMAT PARSER: " + e.getMessage());
            throw new OWLParserException(e.getMessage(), e.getLineNo(), 0);
        }
        catch (OWLOntologyCreationException e) {
            throw new OWLRuntimeException(e);
        }
    }


    private BufferedReader getBufferedReader(OWLOntologyDocumentSource source) throws IOException {
        if(source.isReaderAvailable()) {
            return new BufferedReader(source.getReader());
        }
        else if(source.isInputStreamAvailable()) {
            return new BufferedReader(new InputStreamReader(source.getInputStream()));
        }
        else {
            return new BufferedReader(new InputStreamReader(getInputStream(source.getDocumentIRI())));
        }
    }


    public static void main(String[] args) throws Exception {
        File file = new File("/tmp/gene_ontology_edit_v2013-06-11.obo");
        WebProtegeOBOFormatParser parser = new WebProtegeOBOFormatParser();
        final OWLOntologyManager man = WebProtegeOWLManager.createOWLOntologyManager();
        final OWLOntology ontology = man.createOntology();
        parser.parse(new FileDocumentSource(file), ontology);
        man.saveOntology(ontology, new RDFXMLOntologyFormat(), IRI.create("file:/tmp/out.rdf.xml"));




    }
}
