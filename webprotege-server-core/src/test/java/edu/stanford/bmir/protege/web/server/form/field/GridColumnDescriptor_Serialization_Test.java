package edu.stanford.bmir.protege.web.server.form.field;

import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-05-05
 */
public class GridColumnDescriptor_Serialization_Test {

    private GridColumnDescriptor descriptor;

    @Before
    public void setUp() {
        descriptor = GridColumnDescriptor.get(
                GridColumnId.get(UUID.randomUUID().toString()),
                Optionality.OPTIONAL,
                Repeatability.NON_REPEATABLE,
                null,
                LanguageMap.empty(),
                TextControlDescriptor.getDefault()
        );
    }

    @Test
    public void shouldRoundTrip() throws IOException {
        JsonSerializationTestUtil.testSerialization(descriptor, GridColumnDescriptor.class);
    }
}
