package edu.stanford.bmir.protege.web.server.api;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import edu.stanford.bmir.protege.web.server.api.exception.PermissionDeniedExceptionMapper;
import edu.stanford.bmir.protege.web.server.api.exception.UnknownProjectExceptionMapper;
import edu.stanford.bmir.protege.web.server.api.resources.ProjectsResource;
import edu.stanford.bmir.protege.web.server.integration.IntegrationModule;
import edu.stanford.bmir.protege.web.server.integration.api.IndividualRuntimeDataResource;
import edu.stanford.bmir.protege.web.server.integration.api.OntologyClassResource;
import edu.stanford.bmir.protege.web.server.integration.api.OntologyPropertyResource;
import edu.stanford.bmir.protege.web.server.integration.dispatch.ActionDispatch;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 13 Apr 2018
 */
@Module(includes = IntegrationModule.class)
public class ApiModule {

    @Provides
    public ServletContainer provideServletContainer(ResourceConfig resourceConfig) {
        return new ServletContainer(resourceConfig);
    }

    @Provides
    public ResourceConfig provideResourceConfig(ApiKeyManager apiKeyManager,
                                                Set<ApiRootResource> apiRootResources,
                                                IndividualRuntimeDataResource individualRuntimeDataResource,
                                                OntologyClassResource ontologyClassResource,
                                                OntologyPropertyResource ontologyPropertyResource) {
        ResourceConfig resourceConfig = new ResourceConfig();

        // A filter to ensure that either a session token (with an associated user) is
        // provided or an api key (also associated with a user) is provided
        resourceConfig.register(new AuthenticationFilter(apiKeyManager));

        // Custom injection into the context of the user id identified by the session
        // token or the api key
        resourceConfig.register(new UserIdBinder());
        resourceConfig.register(new ApiKeyBinder());

        resourceConfig.register(new JacksonContextResolver());

        // Exception mappers
        resourceConfig.register(new PermissionDeniedExceptionMapper());
        resourceConfig.register(new UnknownProjectExceptionMapper());

        // Add injected resources
        apiRootResources.forEach(resourceConfig::register);
        resourceConfig.register(individualRuntimeDataResource);
        resourceConfig.register(ontologyClassResource);
        resourceConfig.register(ontologyPropertyResource);

        return resourceConfig;
    }

    @Provides
    public ActionDispatch provideActionDispatch(ActionExecutor actionExecutor) {
        return actionExecutor::execute;
    }

    @Provides
    @IntoSet
    ApiRootResource provideProjectsResource(ProjectsResource resource) {
        return resource;
    }

}
