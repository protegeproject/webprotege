package edu.stanford.bmir.protege.web.server.inject;

import edu.stanford.bmir.protege.web.server.app.WebProtegeProperties;
import edu.stanford.bmir.protege.web.server.filemanager.ConfigDirectorySupplier;
import edu.stanford.bmir.protege.web.server.filemanager.ConfigInputStreamSupplier;
import edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

import static edu.stanford.bmir.protege.web.shared.app.WebProtegePropertyName.NEO4J_HOME_DIR;

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
        var properties = getWebProtegeProperties();
        var homeDir = properties.getNeo4jHomeDir().orElse(NEO4J_HOME_DIR.getDefaultValue().get());
        return new File(homeDir, IMPORT_DIRECTORY_NAME);
    }

    @Nonnull
    private static WebProtegeProperties getWebProtegeProperties() {
        ConfigInputStreamSupplier configInputStreamSupplier = new ConfigInputStreamSupplier(new ConfigDirectorySupplier());
        WebProtegePropertiesProvider propertiesProvider = new WebProtegePropertiesProvider(configInputStreamSupplier);
        return propertiesProvider.get();
    }
}
