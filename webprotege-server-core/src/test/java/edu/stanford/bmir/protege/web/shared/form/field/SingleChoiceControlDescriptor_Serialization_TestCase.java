package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.form.data.LiteralFormControlData;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-08
 */
public class SingleChoiceControlDescriptor_Serialization_TestCase {

    private ImmutableList<ChoiceDescriptor> choices;

    @Before
    public void setUp() {
        choices = ImmutableList.of(
                ChoiceDescriptor.choice(LanguageMap.empty(),
                                        LiteralFormControlData.get(DataFactory.getOWLLiteral("A"))),
                ChoiceDescriptor.choice(LanguageMap.empty(),
                                        LiteralFormControlData.get(DataFactory.getOWLLiteral("B")))
        );
    }

    @Test
    public void shouldSerialize_AnnotationComponentCriteria() throws IOException {
        testSerialization(
                SingleChoiceControlDescriptor.get(SingleChoiceControlType.SEGMENTED_BUTTON,
                                                  FixedChoiceListSourceDescriptor.get(choices))
        );
    }

    private static <V extends SingleChoiceControlDescriptor> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, SingleChoiceControlDescriptor.class);
    }
}
