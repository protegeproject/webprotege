package edu.stanford.bmir.protege.web.server.perspective;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;
import static dagger.internal.codegen.DaggerStreams.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-28
 */
@ApplicationSingleton
public class BuiltInPerspectivesProvider {

    private final Logger logger = LoggerFactory.getLogger(BuiltInPerspectivesProvider.class);

    public static final String CLASSES = "Classes.json";

    public static final String PROPERTIES = "Properties.json";

    public static final String INDIVIDUALS = "Individuals.json";

    public static final String COMMENTS = "Comments.json";

    public static final String CHANGES_BY_ENTITY = "Changes by Entity.json";

    public static final String HISTORY = "History.json";

    public static final String QUERY = "Query.json";

    public static final String OWL_CLASSES = "OWL Classes.json";

    public static final String OWL_PROPERTIES = "OWL Properties.json";

    private static final ImmutableList<String> builtInPerspectives = ImmutableList.of(CLASSES,
                                                                                      PROPERTIES,
                                                                                      INDIVIDUALS,
                                                                                      COMMENTS,
                                                                                      CHANGES_BY_ENTITY,
                                                                                      HISTORY,
                                                                                      QUERY,
                                                                                      OWL_CLASSES,
                                                                                      OWL_PROPERTIES);

    @Nonnull
    private final Provider<BuiltInPerspectiveLoader> loaderFactory;

    @Nonnull
    private ImmutableList<BuiltInPerspective> loadedPerspectives = ImmutableList.of();

    @Inject
    public BuiltInPerspectivesProvider(@Nonnull Provider<BuiltInPerspectiveLoader> loaderFactory) {
        this.loaderFactory = checkNotNull(loaderFactory);
    }

    @Nonnull
    public synchronized ImmutableList<BuiltInPerspective> getBuiltInPerspectives() {
        if (!loadedPerspectives.isEmpty()) {
            return loadedPerspectives;
        }
        loadedPerspectives = builtInPerspectives.stream()
                                                .map(this::toPath)
                                                .map(this::load)
                                                .filter(Objects::nonNull)
                                                .collect(toImmutableList());
        return loadedPerspectives;
    }


    private String toPath(String fileName) {
        return "builtin-perspective-data/" + fileName;
    }

    @Nullable
    private BuiltInPerspective load(String path) {
        try {
            var loader = loaderFactory.get();
            return loader.load(path);
        } catch (Exception e) {
            logger.error("Could not load perspective: {}", path, e);
            return null;
        }
    }
}
