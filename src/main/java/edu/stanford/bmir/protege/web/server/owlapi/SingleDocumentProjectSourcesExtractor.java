package edu.stanford.bmir.protege.web.server.owlapi;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.util.NonMappingOntologyIRIMapper;

import javax.inject.Inject;
import java.io.File;
import java.util.Collection;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
public class SingleDocumentProjectSourcesExtractor implements RawProjectSourcesExtractor {

    @Override
    public RawProjectSources extractProjectSources(File inputFile) {
        return new SingleDocumentProjectSources(inputFile);
    }

    @Inject
    public SingleDocumentProjectSourcesExtractor() {
    }

    private static class SingleDocumentProjectSources implements RawProjectSources {

        public static final NonMappingOntologyIRIMapper EMPTY_IRI_MAPPER = new NonMappingOntologyIRIMapper();

        private File ontologyDocument;

        private SingleDocumentProjectSources(File ontologyDocument) {
            this.ontologyDocument = ontologyDocument;
        }

        @Override
        public Collection<OWLOntologyDocumentSource> getDocumentSources() {
            return Lists.<OWLOntologyDocumentSource>newArrayList(new FileDocumentSource(ontologyDocument));
        }

        @Override
        public OWLOntologyIRIMapper getOntologyIRIMapper() {
            return EMPTY_IRI_MAPPER;
        }

        @Override
        public void cleanUpTemporaryFiles() {
        }
    }
}
