package edu.stanford.bmir.protege.web.server.filemanager;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 20 Mar 2017
 */
public interface HasGetFile {

    @Nonnull
    File getFile();
}
