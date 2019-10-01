package edu.stanford.bmir.protege.web.server.project;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;

import java.io.File;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
@RunWith(MockitoJUnitRunner.class)
public class SingleDocumentProjectSourcesExtractor_TestCase {

    @Mock
    private File input;

    @Test
    public void shouldExtractProvidedDocument() {
        // Given
        SingleDocumentProjectSourcesExtractor extractor = new SingleDocumentProjectSourcesExtractor();
        // When
        RawProjectSources projectSources = extractor.extractProjectSources(input);
        // Then
        Collection<OWLOntologyDocumentSource> documentSources = projectSources.getDocumentSources();
        assertThat(documentSources, hasSize(1));
    }

}
