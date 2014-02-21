package edu.stanford.bmir.protege.web.server.owlapi;

import java.io.File;
import java.io.IOException;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
public interface RawProjectSourcesExtractor {

    RawProjectSources extractProjectSources(File inputFile) throws IOException;
}
