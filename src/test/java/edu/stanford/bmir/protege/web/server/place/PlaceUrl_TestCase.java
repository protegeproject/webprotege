package edu.stanford.bmir.protege.web.server.place;

import edu.stanford.bmir.protege.web.server.perspective.EntityTypePerspectiveMapper;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class PlaceUrl_TestCase {

    private PlaceUrl placeUrl;

    private EntityTypePerspectiveMapper typeMapper;

    private ProjectId projectId = ProjectId.get(UUID.randomUUID().toString());

    private IRI entityIri = IRI.create("http://the.ontology/entity");


    private EntityType<OWLClass> entityType = EntityType.CLASS;

    private OWLEntity entity = DataFactory.getOWLEntity(entityType, entityIri);

    @Before
    public void setUp() throws Exception {
        typeMapper = new EntityTypePerspectiveMapper();
        placeUrl = new PlaceUrl("the.application.host",
                                "/the/application/path",
                                "the.application.name", typeMapper);
    }

    @Test
    public void shouldBuildApplicationUrl() {
        String url = placeUrl.getApplicationUrl();
        assertThat(url, is("http://the.application.host/the/application/path"));
    }

    @Test
    public void shouldBuildApplicationAnchor() {
        String anchor = placeUrl.getApplicationAnchor();
        assertThat(anchor, is("<a href=\"the.application.name\">http://the.application.host/the/application/path</a>"));
    }

    @Test
    public void shouldBuildProjectUrl() {
        String url = placeUrl.getProjectUrl(projectId);
        assertThat(url, is("http://the.application.host/the/application/path#ProjectViewPlace:/projects/"
                                   + projectId.getId()
                                   + "/edit/Classes"));
    }

    @Test
    public void shouldBuildEntityUrl() {

        String url = placeUrl.getEntityUrl(projectId, entity);
        assertThat(url, is("http://the.application.host/the/application/path#ProjectViewPlace:/projects/"
                                   + projectId.getId()
                                   + "/edit/Classes?selection=Class(%3C" + entityIri.toString() + "%3E)"));
    }
}
