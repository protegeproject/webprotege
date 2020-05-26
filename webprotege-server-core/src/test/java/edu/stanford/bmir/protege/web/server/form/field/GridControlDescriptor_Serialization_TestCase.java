package edu.stanford.bmir.protege.web.server.form.field;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.field.*;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-05-05
 */
public class GridControlDescriptor_Serialization_TestCase {

    private GridControlDescriptor descriptor;

    @Before
    public void setUp() {
        descriptor = GridControlDescriptor.get(
                ImmutableList.of(
                        GridColumnDescriptor.get(
                                GridColumnId.get("12345678-1233-1234-1234-123456789abc"),
                                Optionality.OPTIONAL,
                                Repeatability.NON_REPEATABLE,
                                null,
                                LanguageMap.empty(),
                                TextControlDescriptor.getDefault()
                        )
                ),
                null
        );
    }

    @Test
    public void shouldRoundTrip() throws IOException {
        JsonSerializationTestUtil.testSerialization(descriptor, FormControlDescriptor.class);
    }
}
