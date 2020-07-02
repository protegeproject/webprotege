package edu.stanford.bmir.protege.web.server.form;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.functional.parser.OWLFunctionalSyntaxOWLParser;
import org.semanticweb.owlapi.io.OWLParserException;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-15
 */
public class AxiomTemplatesParser {

    private final Logger logger = LoggerFactory.getLogger(AxiomTemplatesParser.class);

    @Inject
    public AxiomTemplatesParser() {
    }

    public Set<OWLAxiom> parseAxiomTemplate(Collection<String> axiomTemplates) {
        var templatesDocument = String.join("\n", axiomTemplates);
        var ontologyDocument = String.format("Ontology(%s)", templatesDocument);
        try {
            var parser = new OWLFunctionalSyntaxOWLParser();
            var ontologyManager = OWLManager.createOWLOntologyManager();
            var ontology = ontologyManager.createOntology();
            parser.parse(new StringDocumentSource(ontologyDocument),
                         ontology,
                         new OWLOntologyLoaderConfiguration());
            return ontology.getAxioms();
        } catch(OWLOntologyCreationException e) {
            logger.error("Error creating ontology", e);
            return Collections.emptySet();
        } catch(IOException e) {
            logger.error("Error parsing ontology", e);
            return Collections.emptySet();
        }
    }
}
