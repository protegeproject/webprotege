package edu.stanford.bmir.protege.web.server.project;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.server.inject.OverridableFile;
import edu.stanford.bmir.protege.web.server.inject.OverridableFileFactory;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-04-30
 */
@ApplicationSingleton
public class BuiltInPrefixDeclarationsLoader {

    private static final Logger logger = LoggerFactory.getLogger(BuiltInPrefixDeclarations.class);

    private final OverridableFileFactory overridableFileFactory;

    @Nullable
    private BuiltInPrefixDeclarations builtInPrefixDeclarations = null;

    @Inject
    public BuiltInPrefixDeclarationsLoader(@Nonnull OverridableFileFactory overridableFileFactory) {
        this.overridableFileFactory = checkNotNull(overridableFileFactory);
    }

    @Nonnull
    public synchronized BuiltInPrefixDeclarations getBuiltInPrefixDeclarations() {
        if(builtInPrefixDeclarations == null) {
            loadBuiltInPrefixDeclarations();
        }
        return builtInPrefixDeclarations;
    }

    private void loadBuiltInPrefixDeclarations() {
        OverridableFile prefixesFile = overridableFileFactory.getOverridableFile("built-in-prefixes.csv");
        try(InputStream is = new BufferedInputStream(new FileInputStream(prefixesFile.get()))) {
            PrefixDeclarationsCsvParser parser = new PrefixDeclarationsCsvParser();
            var prefixDeclarations = parser.parse(is);
            builtInPrefixDeclarations = BuiltInPrefixDeclarations.get(prefixDeclarations);
        }
        catch(IOException ex) {
            logger.error("Could not load built in prefix declarations", ex);
            builtInPrefixDeclarations = BuiltInPrefixDeclarations.get(ImmutableList.of());
        }
    }


}
