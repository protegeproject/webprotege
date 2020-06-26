package edu.stanford.bmir.protege.web.shared.form.field;

import edu.stanford.bmir.protege.web.shared.match.JsonSerializationTestUtil;
import org.junit.Test;

import java.io.IOException;

public class GridControlOrderBy_Serialization_TestCase {

    @Test
    public void shouldSerializeOrderBy() throws IOException {
        var orderBy = GridControlOrderBy.get(GridColumnId.get("12345678-1234-1234-1234-123456789abc"),
                                             GridControlOrderByDirection.DESC);
        JsonSerializationTestUtil.testSerialization(orderBy, GridControlOrderBy.class);
    }
}
