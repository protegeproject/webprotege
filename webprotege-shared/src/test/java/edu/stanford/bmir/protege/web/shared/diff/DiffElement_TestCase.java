package edu.stanford.bmir.protege.web.shared.diff;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 29/01/15
 */
@RunWith(MockitoJUnitRunner.class)
public class DiffElement_TestCase<D extends Serializable, E extends Serializable> {


    private DiffElement<D, E> diffElement;

    private DiffElement<D, E> otherDiffElement;

    @Mock
    private D sourceDocument;

    @Mock
    private E lineElement;

    private DiffOperation diffOperation = DiffOperation.ADD;


    @Before
    public void setUp() throws Exception {
        diffElement = new DiffElement<>(diffOperation, sourceDocument, lineElement);
        otherDiffElement = new DiffElement<>(diffOperation, sourceDocument, lineElement);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_DiffOperation_IsNull() {
        new DiffElement<>(null, sourceDocument, lineElement);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_SourceDocument_IsNull() {
        new DiffElement<>(diffOperation, null, lineElement);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerExceptionIf_LineElement_IsNull() {
        new DiffElement<>(diffOperation, sourceDocument, null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(diffElement, is(equalTo(diffElement)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(diffElement, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(diffElement, is(equalTo(otherDiffElement)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(diffElement.hashCode(), is(otherDiffElement.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(diffElement.toString(), startsWith("DiffElement"));
    }

    @Test
    public void shouldReturnSuppliedDiffOperation() {
        assertThat(diffElement.getDiffOperation(), is(diffOperation));
    }

    @Test
    public void shouldReturnSuppliedSourceDocument() {
        assertThat(diffElement.getSourceDocument(), is(sourceDocument));
    }

    @Test
    public void shouldReturnSuppliedLineElement() {
        assertThat(diffElement.getLineElement(), is(lineElement));
    }
}