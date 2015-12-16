package edu.stanford.bmir.protege.web.shared.place;

import com.google.common.base.Optional;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Test;
import org.semanticweb.owlapi.model.IRI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 14/12/15
 */
public class ProjectViewPlaceTokenizer_TestCase {

    private final ProjectId projectId = ProjectId.get("aaaabbbb-cccc-dddd-eeee-ffffffffffffffff");

    private final TabId tabId = new TabId("MyTab");

    private final IRI entityIri = IRI.create("http://stuff.com#A");

    @Test
    public void shouldTokenizeProjectViewPlace() {
        String token = "projectId="+projectId.getId()+"&tab="+tabId.getTabName()+"&entity=Class("+entityIri.toQuotedString()+")";
        ProjectViewPlace.Tokenizer tokenizer = new ProjectViewPlace.Tokenizer();
        ProjectViewPlace place = tokenizer.getPlace(token);
        assertThat(place.getProjectId(), is(projectId));
        assertThat(place.getTabId(), is(Optional.of(tabId)));
        assertThat(place.getEntity(), is(Optional.of(DataFactory.getOWLClass(entityIri))));
    }


    @Test
    public void shouldTokenizeIgnoringVarNameCaseProjectViewPlace() {
        String token = "ProjectId="+projectId.getId()+"&Tab="+tabId.getTabName()+"&Entity=Class("+entityIri.toQuotedString()+")";
        ProjectViewPlace.Tokenizer tokenizer = new ProjectViewPlace.Tokenizer();
        ProjectViewPlace place = tokenizer.getPlace(token);
        assertThat(place.getProjectId(), is(projectId));
        assertThat(place.getTabId(), is(Optional.of(tabId)));
        assertThat(place.getEntity(), is(Optional.of(DataFactory.getOWLClass(entityIri))));
    }



}
