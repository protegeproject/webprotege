package edu.stanford.bmir.protege.web.server.place;

import edu.stanford.bmir.protege.web.client.place.ItemSelection;
import edu.stanford.bmir.protege.web.server.inject.ApplicationHost;
import edu.stanford.bmir.protege.web.server.inject.ApplicationName;
import edu.stanford.bmir.protege.web.server.inject.ApplicationPath;
import edu.stanford.bmir.protege.web.server.inject.ApplicationPort;
import edu.stanford.bmir.protege.web.server.perspective.EntityTypePerspectiveMapper;
import edu.stanford.bmir.protege.web.shared.app.ApplicationScheme;
import edu.stanford.bmir.protege.web.shared.perspective.PerspectiveId;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlaceTokenizer;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 */
public class PlaceUrl {

    private final ApplicationScheme applicationScheme;

    private final String applicationHost;

    private final Optional<Integer> applicationPort;

    private final String applicationPath;

    private final String applicationName;

    private final EntityTypePerspectiveMapper mapper;

    /**
     * Construct a {@link PlaceUrl} object that provides URLs for places for a given application host,
     * path, and name.
     * @param applicationScheme The scheme for the application.
     * @param applicationHost The application host.  For example, webprotege.stanford.edu.
     * @param applicationPort The application port.  May be empty for the default port.
     * @param applicationPath The application path.  May be empty.  If not empty the path must be an absolute
     *                        path, i.e. it must start with a '/' character.
     * @param applicationName The name of the application e.g. WebProtege
     * @param mapper An {@link EntityTypePerspectiveMapper} that will be used to retrieve the perspective id that
     *               should be shown for a given entity type.  For example for OWLClass entities the "Classes"
     *               perspective might be shown.
     */
    @Inject
    public PlaceUrl(@Nonnull ApplicationScheme applicationScheme,
                    @Nonnull @ApplicationHost String applicationHost,
                    @Nonnull @ApplicationPort Optional<Integer> applicationPort,
                    @Nonnull @ApplicationPath String applicationPath,
                    @Nonnull @ApplicationName String applicationName,
                    @Nonnull EntityTypePerspectiveMapper mapper) {
        this.applicationScheme = checkNotNull(applicationScheme);
        this.applicationHost = checkNotNull(applicationHost);
        this.applicationPort = checkNotNull(applicationPort);
        this.applicationPath = checkNotNull(applicationPath);
        this.applicationName = checkNotNull(applicationName);
        this.mapper = checkNotNull(mapper);
    }

    /**
     * Gets the application Url.  This is the root Url of the WebProtege application.
     * @return A string representing the Url.
     */
    @Nonnull
    public String getApplicationUrl() {
        return createUrl(null);
    }

    /**
     * Gets the application anchor.  This is the name of the WebProtege application surrounded by
     * anchor tags whose href is the value provided by {@link #getApplicationUrl()}.
     * @return A string representing the Url.
     */
    @Nonnull
    public String getApplicationAnchor() {
        return String.format("<a href=\"%s\">%s</a>", applicationName, getApplicationUrl());
    }

    /**
     * Get the Url for the specified project.
     * @param projectId The project whose Url will be returned.
     * @return A string representing the Url.
     */
    @Nonnull
    public String getProjectUrl(@Nonnull ProjectId projectId) {
        ProjectViewPlace place = new ProjectViewPlace(projectId, mapper.getDefaultPerspectiveId(), ItemSelection.empty());
        String projectPlaceFragment = getProjectPlaceFragment(place);
        return createUrl(projectPlaceFragment);
    }

    /**
     * Gets the Url for the specified entity in the specified project.
     * @param projectId The project.
     * @param entity The entity.
     * @return A string representing the Url.
     */
    @Nonnull
    public String getEntityUrl(@Nonnull ProjectId projectId,
                               @Nonnull OWLEntity entity) {
        ProjectViewPlace place = new ProjectViewPlace(projectId,
                                                      mapper.getPerspectiveId(entity.getEntityType()),
                                                      ItemSelection.builder().addEntity(entity).build());
        String projectPlaceFragment = getProjectPlaceFragment(place);
        return createUrl(projectPlaceFragment);

    }

    private String getProjectPlaceFragment(ProjectViewPlace place) {
        ProjectViewPlaceTokenizer tokenizer = new ProjectViewPlaceTokenizer();
        String placeToken = tokenizer.getToken(place);
        return "ProjectViewPlace:" + placeToken;
    }


    private String createUrl(@Nullable String fragment) {
        try {
            final URI uri;
            String scheme = applicationScheme.toString().toLowerCase();
            if(applicationPort.isPresent()) {
                uri = new URI(scheme, null, applicationHost, applicationPort.get(), applicationPath, null, fragment);
            }
            else {
                uri = new URI(scheme, applicationHost, applicationPath, fragment);
            }
            return uri.toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
