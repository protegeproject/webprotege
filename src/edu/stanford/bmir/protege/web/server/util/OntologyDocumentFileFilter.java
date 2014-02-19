package edu.stanford.bmir.protege.web.server.util;

import org.apache.commons.io.filefilter.AbstractFileFilter;

import java.io.File;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
public class OntologyDocumentFileFilter extends AbstractFileFilter {

    @Override
    public boolean accept(File file) {
        return !file.isDirectory() && !file.isHidden();
    }
}
