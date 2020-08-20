package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableList;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.server.match.EntityMatcherFactory;
import edu.stanford.bmir.protege.web.server.match.Matcher;
import edu.stanford.bmir.protege.web.server.project.BuiltInPrefixDeclarations;
import edu.stanford.bmir.protege.web.server.repository.ProjectSearchFiltersRepository;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityMatchCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilter;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.annotation.Nonnull;
import java.nio.file.Path;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-14
 */
@Module
public class StandaloneObjectSupportModule {

    @Provides
    @ProjectSingleton
    OWLDataFactory provideOWLDataFactory() {
        return new OWLDataFactoryImpl();
    }

    @Provides
    BuiltInPrefixDeclarations builtInPrefixDeclarations() {
        return BuiltInPrefixDeclarations.get(ImmutableList.of());
    }

    @Provides
    @ProjectSingleton
    public BuiltInOwlEntitiesIndex provideBuiltInOwlEntitiesIndex(@Nonnull BuiltInOwlEntitiesIndexImpl impl) {
        return impl;
    }

    @Provides
    @ProjectSingleton
    public BuiltInSkosEntitiesIndex provideBuiltInSkosEntitiesIndex(@Nonnull BuiltInSkosEntitiesIndexImpl impl) {
        return impl;
    }

    @Provides
    @LuceneIndexesDirectory
    Path provideLuceneIndexesDirectory() {
        return Path.of("/tmp", "lucene");
    }

    @Provides
    ProjectSearchFiltersRepository provideProjectSearchFiltersRepository() {
        return new ProjectSearchFiltersRepository() {
            @Nonnull
            @Override
            public ImmutableList<EntitySearchFilter> getSearchFilters(@Nonnull ProjectId projectId) {
                return ImmutableList.of();
            }
        };
    }

    @Provides
    EntityMatcherFactory provideEntityMatcherFactory() {
        return new EntityMatcherFactory() {
            @Nonnull
            @Override
            public Matcher<OWLEntity> getEntityMatcher(@Nonnull EntityMatchCriteria criteria) {
                return entity -> false;
            }
        };
    }
}
