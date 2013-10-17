package edu.stanford.bmir.protege.web.server.obo;

import org.coode.owlapi.obo.parser.OBOOntologyFormat;
import org.obolibrary.obo2owl.Obo2Owl;
import org.obolibrary.oboformat.model.OBODoc;
import org.obolibrary.oboformat.parser.OBOFormatParser;
import org.obolibrary.oboformat.parser.OBOFormatParserException;
import org.semanticweb.owlapi.io.*;
import org.semanticweb.owlapi.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

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
            return new OBOOntologyFormat();
        }
        catch (OBOFormatParserException e) {
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
            return new BufferedReader(new InputStreamReader(source.getInputStream(),"utf-8"));
        }
        else {
            return new BufferedReader(new InputStreamReader(getInputStream(source.getDocumentIRI()), "utf-8"));
        }
    }
}
