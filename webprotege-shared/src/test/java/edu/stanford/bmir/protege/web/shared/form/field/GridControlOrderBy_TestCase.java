package edu.stanford.bmir.protege.web.shared.form.field;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(MockitoJUnitRunner.class)
public class GridControlOrderBy_TestCase {

    @Mock
    private GridColumnId columnId;

    private GridControlOrderByDirection direction = GridControlOrderByDirection.ASC;

    private GridControlOrderBy orderBy;

    @Before
    public void setUp() throws Exception {
        orderBy = GridControlOrderBy.get(columnId, direction);
    }

    @Test
    public void shouldReturnSuppliedColumnId() {
        assertThat(orderBy.getColumnId(), equalTo(columnId));
    }

    @Test
    public void shouldReturnSuppliedDirection() {
        assertThat(orderBy.getDirection(), equalTo(direction));
    }

    @Test
    public void shouldReturnTrueForAsc() {
        assertThat(orderBy.isAscending(), equalTo(true));
    }

    @Test
    public void shouldReturnFalseForDesc() {
        GridControlOrderBy orderByDesc = GridControlOrderBy.get(columnId, GridControlOrderByDirection.DESC);
        assertThat(orderByDesc.isAscending(), equalTo(false));
    }
}