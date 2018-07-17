package edu.stanford.bmir.protege.web.shared.place;

import com.google.gwt.place.shared.Place;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 17 Jul 2018
 */
@RunWith(MockitoJUnitRunner.class)
public class LanguageSettingsPlace_TestCase {

    @Mock
    private ProjectId projectId;

    @Mock
    private Place nextPlace;

    private LanguageSettingsPlace place;

    @Before
    public void setUp() {
        place = LanguageSettingsPlace.get(projectId, nextPlace);
    }

    @Test
    public void shouldReturnProvidedProjectId() {
        assertThat(place.getProjectId(), is(projectId));
    }

    @Test
    public void shouldReturnNextPlace() {
        assertThat(place.getNextPlace(), is(Optional.of(nextPlace)));
    }


}
