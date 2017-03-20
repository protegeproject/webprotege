package edu.stanford.bmir.protege.web.server.place;

import edu.stanford.bmir.protege.web.client.place.ItemSelection;
import edu.stanford.bmir.protege.web.server.app.*;
import edu.stanford.bmir.protege.web.server.perspective.EntityTypePerspectiveMapper;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlace;
import edu.stanford.bmir.protege.web.shared.place.ProjectViewPlaceTokenizer;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 10 Mar 2017
 */
public class PlaceUrl {

    private final ApplicationSchemeProvider applicationSchemeProvider;

    private final ApplicationHostSupplier applicationHostSupplier;

    private final ApplicationPortProvider applicationPortProvider;

    private final ApplicationPathProvider applicationPathProvider;

    private final ApplicationNameSupplier applicationNameSupplier;

    private final EntityTypePerspectiveMapper mapper;

    /**
     * Construct a {@link PlaceUrl} object that provides URLs for places for a given application host,
     * path, port and name.
     * @param applicationSchemeProvider The scheme for the application.
     * @param applicationHostSupplier A provider for the application host.
     * @param applicationPortProvider A provider for the application port.
     * @param applicationPathProvider A provider for the application path.
     * @param applicationNameSupplier A provider for the application name.
     * @param mapper An {@link EntityTypePerspectiveMapper} that will be used to retrieve the perspective id that
     *               should be shown for a given entity type.  For example for OWLClass entities the "Classes"
     *               perspective might be shown.
     */
    @Inject
    public PlaceUrl(@Nonnull ApplicationSchemeProvider applicationSchemeProvider,
                    @Nonnull ApplicationHostSupplier applicationHostSupplier,
                    @Nonnull ApplicationPortProvider applicationPortProvider,
                    @Nonnull ApplicationPathProvider applicationPathProvider,
                    @Nonnull ApplicationNameSupplier applicationNameSupplier,
                    @Nonnull EntityTypePerspectiveMapper mapper) {
        this.applicationSchemeProvider = checkNotNull(applicationSchemeProvider);
        this.applicationHostSupplier = checkNotNull(applicationHostSupplier);
        this.applicationPortProvider = checkNotNull(applicationPortProvider);
        this.applicationPathProvider = checkNotNull(applicationPathProvider);
        this.applicationNameSupplier = checkNotNull(applicationNameSupplier);
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
        return String.format("<a href=\"%s\">%s</a>", applicationNameSupplier, getApplicationUrl());
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
            String scheme = applicationSchemeProvider.getApplicationScheme().name().toLowerCase();
            if(applicationPortProvider.getApplicationPort().isPresent()) {
                uri = new URI(scheme,
                              null,
                              applicationHostSupplier.getApplicationHost(),
                              applicationPortProvider.getApplicationPort().get(),
                              applicationPathProvider.getApplicationPath(),
                              null,
                              fragment);
            }
            else {
                uri = new URI(scheme,
                              applicationHostSupplier.getApplicationHost(),
                              applicationPathProvider.getApplicationPath(),
                              fragment);
            }
            return uri.toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
