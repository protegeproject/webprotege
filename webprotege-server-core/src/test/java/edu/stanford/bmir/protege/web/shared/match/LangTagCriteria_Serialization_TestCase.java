package edu.stanford.bmir.protege.web.shared.match;

import edu.stanford.bmir.protege.web.shared.match.criteria.AnyLangTagOrEmptyLangTagCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.LangTagCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.LangTagIsEmptyCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.LangTagMatchesCriteria;
import org.junit.Test;

import java.io.IOException;

import static edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil.testSerialization;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 18 Jun 2018
 */
public class LangTagCriteria_Serialization_TestCase {


    @Test
    public void shouldSerialize_AnyLangTagOrEmptyLangTagCriteria() throws IOException {
        testSerialization(AnyLangTagOrEmptyLangTagCriteria.get(), LangTagCriteria.class);
    }

    @Test
    public void shouldSerialize_LangTagIsEmptyCriteria() throws IOException {
        testSerialization(LangTagIsEmptyCriteria.get(), LangTagCriteria.class);
    }

    @Test
    public void shouldSerialize_LangTagMatchesCriteria() throws IOException {
        testSerialization(LangTagMatchesCriteria.get("*-GB"), LangTagCriteria.class);
    }

}
