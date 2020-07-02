package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.form.data.LiteralFormControlData;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityTypeIsOneOfCriteria;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.EntityType;

import java.io.IOException;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-11
 */
public class ChoiceListSourceDescriptor_Serialization_TestCase {

    ImmutableList<ChoiceDescriptor> choices;

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
    public void shouldSerialize_FixedList() throws IOException {
        testSerialization(
                FixedChoiceListSourceDescriptor.get(choices)
        );
    }

    @Test
    public void shouldSerialize_DynamicList() throws IOException {
        testSerialization(
                DynamicChoiceListSourceDescriptor.get(EntityTypeIsOneOfCriteria.get(ImmutableSet.of(EntityType.CLASS)))
        );
    }

    private static <V extends ChoiceListSourceDescriptor> void testSerialization(V value) throws IOException {
        JsonSerializationTestUtil.testSerialization(value, ChoiceListSourceDescriptor.class);
    }
}
