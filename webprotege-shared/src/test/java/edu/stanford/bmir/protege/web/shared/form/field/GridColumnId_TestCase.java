package edu.stanford.bmir.protege.web.shared.form.field;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class GridColumnId_TestCase {

    public static final String UUID = "12345678-1234-1234-1234-123456789abc";

    @Test
    public void shouldCreateId() {
        GridColumnId id = GridColumnId.get(UUID);
        assertThat(id.getId(), equalTo(UUID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForMalformedId() {
        GridColumnId.get("NotAUuid");
    }
}