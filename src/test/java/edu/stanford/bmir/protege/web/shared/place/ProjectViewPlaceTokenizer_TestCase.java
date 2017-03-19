package edu.stanford.bmir.protege.web.shared.place;

import edu.stanford.bmir.protege.web.client.place.Item;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/12/15
 */
public class ProjectViewPlaceTokenizer_TestCase {

    private final ProjectId projectId = ProjectId.get("aaaabbbb-cccc-dddd-eeee-ffffffffffffffff");

    private final IRI entityIri = IRI.create("http://stuff.com#A");

    private ProjectViewPlaceTokenizer tokenizer;

    private String token = "projects/" + projectId + "/edit/MyPerspective?selection=Class(<" + entityIri.toString() + ">)";

//    @Before
//    public void setUp() throws Exception {
//        tokenizer = new ProjectViewPlaceTokenizer();
//    }
//
//    @Test
//    public void shouldGetPlaceWithProjectId() {
//        ProjectViewPlace place = tokenizer.getPlace(token);
//        assertThat(place.getProjectId(), is(projectId));
//    }
//
//    @Test
//    public void shouldGetPlaceWithPerspectiveId() {
//        ProjectViewPlace place = tokenizer.getPlace(token);
//        assertThat(place.getPerspectiveId().getId(), is("MyPerspective"));
//    }
//
//    @Test
//    public void shouldGetPlaceWithSelection() {
//        ProjectViewPlace place = tokenizer.getPlace(token);
//        Optional<Item<?>> firstItem = place.getItemSelection().getFirst();
//        assertThat(firstItem.isPresent(), is(true));
//    }
}
