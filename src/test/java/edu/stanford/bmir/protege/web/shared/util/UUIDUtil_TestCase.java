package edu.stanford.bmir.protege.web.shared.util;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2017
 */
public class UUIDUtil_TestCase {

    @Test
    public void shouldBeWellFormedUuid() {
        String uuid = "0d8f03d4-d9bb-496d-a78c-146868af8265";
        boolean wellFormed = UUIDUtil.isWellFormed(uuid);
        assertThat(wellFormed, is(true));
    }

    @Test
    public void shouldBeMalformedUuid() {
        String uuid = "wrong";
        boolean wellFormed = UUIDUtil.isWellFormed(uuid);
        assertThat(wellFormed, is(false));
    }
}
