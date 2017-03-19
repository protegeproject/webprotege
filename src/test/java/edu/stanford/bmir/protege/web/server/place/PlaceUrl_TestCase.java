package edu.stanford.bmir.protege.web.server.place;

import edu.stanford.bmir.protege.web.server.app.*;
import edu.stanford.bmir.protege.web.server.perspective.EntityTypePerspectiveMapper;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.app.ApplicationScheme;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;

import java.util.Optional;
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

    public static final String THE_APPLICATION_HOST = "the.application.host";

    public static final String THE_APPLICATION_PATH = "/the/application/path";

    public static final String THE_APPLICATION_NAME = "the.application.name";

    public static final String EXPECTED_URL_BASE = "https://the.application.host/the/application/path";

    private PlaceUrl placeUrl;

    private ProjectId projectId = ProjectId.get(UUID.randomUUID().toString());

    private IRI entityIri = IRI.create("http://the.ontology/entity");

    private EntityType<OWLClass> entityType = EntityType.CLASS;

    private OWLEntity entity = DataFactory.getOWLEntity(entityType, entityIri);

    @Mock
    private EntityTypePerspectiveMapper typeMapper;

    @Mock
    private ApplicationHostProvider hostProvider;

    @Mock
    private ApplicationPortProvider portProvider;

    @Mock
    private ApplicationPathProvider pathProvider;

    @Mock
    private ApplicationNameProvider appNameProvider;

    @Mock
    private ApplicationSchemeProvider schemeProvider;

    @Before
    public void setUp() throws Exception {
        when(typeMapper.getPerspectiveId(Matchers.any())).thenReturn(new PerspectiveId("TheClassPerspective"));
        when(typeMapper.getDefaultPerspectiveId()).thenReturn(new PerspectiveId("TheDefaultPerspective"));

        when(hostProvider.getApplicationHost()).thenReturn(THE_APPLICATION_HOST);
        when(portProvider.getApplicationPort()).thenReturn(Optional.empty());
        when(pathProvider.getApplicationPath()).thenReturn(THE_APPLICATION_PATH);
        when(appNameProvider.getApplicationName()).thenReturn(THE_APPLICATION_NAME);
        when(schemeProvider.getApplicationScheme()).thenReturn(ApplicationScheme.HTTPS);

        placeUrl = new PlaceUrl(schemeProvider,
                                hostProvider,
                                portProvider,
                                pathProvider,
                                appNameProvider,
                                typeMapper);
    }

    @Test
    public void shouldBuildApplicationUrl() {
        String url = placeUrl.getApplicationUrl();
        assertThat(url, is(EXPECTED_URL_BASE));
    }

    @Test
    public void shouldBuildApplicationAnchor() {
        String anchor = placeUrl.getApplicationAnchor();
        assertThat(anchor, is("<a href=\"the.application.name\">" + EXPECTED_URL_BASE + "</a>"));
    }

    @Test
    public void shouldBuildProjectUrl() {
        String url = placeUrl.getProjectUrl(projectId);
        assertThat(url, is(EXPECTED_URL_BASE + "#ProjectViewPlace:/projects/"
                                   + projectId.getId()
                                   + "/edit/TheDefaultPerspective"));
    }

    @Test
    public void shouldBuildEntityUrl() {

        String url = placeUrl.getEntityUrl(projectId, entity);
        assertThat(url, is(EXPECTED_URL_BASE + "#ProjectViewPlace:/projects/"
                                   + projectId.getId()
                                   + "/edit/TheClassPerspective?selection=Class(%3C" + entityIri.toString() + "%3E)"));
    }
}
