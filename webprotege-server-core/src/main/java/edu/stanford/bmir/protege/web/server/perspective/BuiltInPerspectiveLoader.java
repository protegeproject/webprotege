package edu.stanford.bmir.protege.web.server.perspective;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveLayout;
import edu.stanford.protege.widgetmap.shared.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.io.IOException;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-28
 */
public class BuiltInPerspectiveLoader {

    @Nonnull
    private final ObjectMapper objectMapper;

    @Inject
    public BuiltInPerspectiveLoader(@Nonnull ObjectMapper objectMapper) {
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Nonnull
    public BuiltInPerspective load(String path) throws IOException {
        var classLoader = Thread.currentThread().getContextClassLoader();
        try(var input = classLoader.getResourceAsStream(path)) {
            if(input == null) {
                throw new RuntimeException("Builtin perspective not found: " + path);
            }
            return objectMapper.readValue(input, BuiltInPerspective.class);
        }
    }
}
