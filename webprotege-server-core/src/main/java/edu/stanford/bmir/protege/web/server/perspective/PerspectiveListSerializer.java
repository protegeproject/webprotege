package edu.stanford.bmir.protege.web.server.perspective;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/16
 */
public class PerspectiveListSerializer {

    @Nonnull
    private final ObjectMapper objectMapper;

    public PerspectiveListSerializer() {
        objectMapper = new ObjectMapper();
    }

    public List<PerspectiveId> deserializePerspectiveList(File fromFile) throws IOException {
        return objectMapper.readValue(fromFile, new TypeReference<List<PerspectiveId>>(){});
    }

    public void serializePerspectiveList(List<PerspectiveId> perspectiveIds, File toFile) throws IOException {
        objectMapper.writeValue(toFile, perspectiveIds);
    }
}
