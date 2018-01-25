package edu.stanford.bmir.protege.web.server.perspective;

import com.google.common.io.Files;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 27/02/16
 */
public class PerspectiveListSerializer {

    public PerspectiveListSerializer() {
    }

    public List<PerspectiveId> deserializePerspectiveList(File file) throws IOException {
        String s = Files.toString(file, Charset.forName("utf-8"));
        return createGson().fromJson(s, new TypeToken<List<PerspectiveId>>(){}.getType());
    }

    public void serializePerspectiveList(List<PerspectiveId> perspectiveIds, File toFile) throws IOException {
        String json = createGson().toJson(perspectiveIds);
        Files.write(json.getBytes(Charset.forName("utf-8")), toFile);
    }

    private Gson createGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.disableHtmlEscaping();
        builder.registerTypeAdapter(PerspectiveId.class, new PerspectiveIdSerializer());
        builder.registerTypeAdapter(PerspectiveId.class, new PerspectiveIdDeserializer());
        return builder.create();
    }
}
