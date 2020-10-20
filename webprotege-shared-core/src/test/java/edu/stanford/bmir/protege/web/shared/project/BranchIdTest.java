package edu.stanford.bmir.protege.web.shared.project;

import org.junit.Test;

import static edu.stanford.bmir.protege.web.shared.project.BranchId.generate;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class BranchIdTest {

    public static final String ID = "12345678-1234-1234-1234-123456789abc";

    @Test
    public void shouldGetBranchIdWithSuppliedId() {
        BranchId branchId = BranchId.get(ID);
        assertThat(branchId.getId(), is(ID));
    }

    /** @noinspection ConstantConditions*/
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfBranchIdIsNull() {
        BranchId.get(null);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionIfIdIsNotUuid() {
        BranchId.get("x");
    }

    @Test
    public void shouldGenerate() {
        BranchId branchId = BranchId.generate();
        assertThat(branchId, is(not(nullValue())));
    }
}