package edu.stanford.bmir.protege.web.shared.form.field;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class FormFieldId_TestCase {

    public static final String UUID = "12345678-1234-1234-1234-123456789abc";

    @Test
    public void shouldGetFormFieldIdWithSuppliedUUID() {
        FormFieldId id = FormFieldId.get(UUID);
        assertThat(id.getId(), equalTo(UUID));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAcceptMalformedId() {
        FormFieldId.get("NotAUUID");
    }

    @Test(expected = NullPointerException.class)
    public void shouldNotAcceptNull() {
        FormFieldId.get(null);
    }
}