package edu.stanford.bmir.protege.web.shared.project;

import edu.stanford.bmir.protege.web.MockingUtils;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.projectsettings.EntityDeprecationSettings;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-10-23
 */
public class EntityDeprecationSettings_Serialization_TestCase {

    @Test
    public void shouldSerializeEmpty() throws IOException {
        JsonSerializationTestUtil.testSerialization(EntityDeprecationSettings.empty(),
                                                    EntityDeprecationSettings.class);
    }

    @Test
    public void shouldSerializeNonEmpty() throws IOException {
        var settings = EntityDeprecationSettings.get(MockingUtils.mockOWLClass(),
                                                     MockingUtils.mockOWLObjectProperty(),
                                                     MockingUtils.mockOWLDataProperty(),
                                                     MockingUtils.mockOWLAnnotationProperty(),
                                                     MockingUtils.mockOWLClass());
        JsonSerializationTestUtil.testSerialization(settings,
                                                    EntityDeprecationSettings.class);
    }
}
