package edu.stanford.bmir.protege.web.shared.project;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class OntologyDocumentIdTest {

    public static final String ID = "12345678-1234-1234-1234-123456789abc";

    @Test
    public void shouldGetOntologyDocumentIdWithSuppliedId() {
        OntologyDocumentId documentId = OntologyDocumentId.get(ID);
        assertThat(documentId.getId(), is(ID));
    }

    /** @noinspection ConstantConditions*/
    @Test(expected = NullPointerException.class)
    public void shouldThrowNpeIfIdIsNull() {
        OntologyDocumentId.get(null);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionIfIdIsNotUuid() {
        OntologyDocumentId.get("x");
    }
}