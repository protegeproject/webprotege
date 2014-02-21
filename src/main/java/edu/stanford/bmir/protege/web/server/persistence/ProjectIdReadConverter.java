package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 8/20/13
 * <p>
 *     An implementation of {@link Converter} that reads a {@link ProjectId} from a {@link String}.
 * </p>
 */
@ReadingConverter
public class ProjectIdReadConverter implements Converter<String, ProjectId> {

    public ProjectId convert(String id) {
        return ProjectId.get(id);
    }
}