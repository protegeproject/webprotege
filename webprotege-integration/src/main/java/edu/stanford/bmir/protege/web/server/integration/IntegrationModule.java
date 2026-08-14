package edu.stanford.bmir.protege.web.server.integration;

import dagger.Module;

/**
 * Dagger module for the third-party integration layer ({@code webprotege-integration}).
 *
 * <p>Runtime REST types ({@code IndividualRuntimeDataService},
 * {@code OntologyClassService}, {@code OntologyPropertyService} and their
 * resources) use constructor {@code @Inject} and are created by the
 * application graph when {@link edu.stanford.bmir.protege.web.server.api.ApiModule}
 * includes this module. {@code ActionDispatch} stays in {@code ApiModule} because it
 * binds to server-side {@code ActionExecutor}.</p>
 *
 * <p>Keep this class as a valid compilation unit (package + {@code @Module} type). An empty
 * {@code .java} file is rejected by {@code javac} and breaks the Maven build.</p>
 */
@Module
public class IntegrationModule {
}
