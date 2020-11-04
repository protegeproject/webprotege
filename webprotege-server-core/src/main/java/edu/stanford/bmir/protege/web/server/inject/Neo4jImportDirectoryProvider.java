package edu.stanford.bmir.protege.web.server.inject;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class Neo4jImportDirectoryProvider implements Provider<File> {

    public static final String IMPORT_DIRECTORY_NAME = "import";

    @Inject
    public Neo4jImportDirectoryProvider() {
    }

    @Override
    public File get() {
        var homeDir = System.getenv("NEO4J_HOME");
        return new File(homeDir, IMPORT_DIRECTORY_NAME);
    }
}
