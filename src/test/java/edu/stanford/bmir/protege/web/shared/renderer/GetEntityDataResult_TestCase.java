package edu.stanford.bmir.protege.web.shared.renderer;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 22/02/15
 */
@RunWith(MockitoJUnitRunner.class)
public class GetEntityDataResult_TestCase {


    private GetEntityDataResult result;

    private GetEntityDataResult otherResult;

    @Mock
    private ImmutableMap<OWLEntity, OWLEntityData> entity2EntityDataMap;


    @Before
    public void setUp() throws Exception {
        result = new GetEntityDataResult(entity2EntityDataMap);
        otherResult = new GetEntityDataResult(entity2EntityDataMap);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNullPointerException() {
        new GetEntityDataResult(null);
    }

    @Test
    public void shouldBeEqualToSelf() {
        assertThat(result, is(equalTo(result)));
    }

    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(result, is(not(equalTo(null))));
    }

    @Test
    public void shouldBeEqualToOther() {
        assertThat(result, is(equalTo(otherResult)));
    }

    @Test
    public void shouldHaveSameHashCodeAsOther() {
        assertThat(result.hashCode(), is(otherResult.hashCode()));
    }

    @Test
    public void shouldGenerateToString() {
        assertThat(result.toString(), startsWith("GetEntityDataResult"));
    }

    @Test
    public void shouldReturnSuppliedMap() {
        assertThat(result.getEntityDataMap(), is(entity2EntityDataMap));
    }
}