package edu.stanford.bmir.protege.web.server.search;

import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityIsDeprecatedCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilter;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilterId;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-08-15
 */
public class EntitySearchFilter_Serialization_TestCase {

    @Test
    public void shouldSerializeAsJson() throws IOException {
        EntitySearchFilter in = EntitySearchFilter.get(EntitySearchFilterId.createFilterId(),
                                                       ProjectId.get(UUID.randomUUID().toString()),
                                                       LanguageMap.of("en", "Hello"),
                                                       EntityIsDeprecatedCriteria.get());

        JsonSerializationTestUtil.testSerialization(in, EntitySearchFilter.class);
    }

}
