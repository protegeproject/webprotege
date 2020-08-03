package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableList;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.index.BuiltInOwlEntitiesIndex;
import edu.stanford.bmir.protege.web.server.index.BuiltInOwlEntitiesIndexImpl;
import edu.stanford.bmir.protege.web.server.index.BuiltInSkosEntitiesIndex;
import edu.stanford.bmir.protege.web.server.index.BuiltInSkosEntitiesIndexImpl;
import edu.stanford.bmir.protege.web.server.project.BuiltInPrefixDeclarations;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import org.semanticweb.owlapi.model.OWLDataFactory;
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
}
