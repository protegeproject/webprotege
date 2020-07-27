package edu.stanford.bmir.protege.web.server.index;

import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-27
 */
public class BuiltInOwlEntitiesIndexImpl implements BuiltInOwlEntitiesIndex {

    @Nonnull
    private final ImmutableSet<OWLEntity> builtInEntities;

    @Inject
    public BuiltInOwlEntitiesIndexImpl(@Nonnull OWLDataFactory dataFactory) {
        builtInEntities = ImmutableSet.of(
                dataFactory.getOWLThing(),
                dataFactory.getOWLNothing(),
                dataFactory.getOWLTopObjectProperty(),
                dataFactory.getOWLBottomObjectProperty(),
                dataFactory.getOWLTopDataProperty(),
                dataFactory.getOWLBottomDataProperty(),
                dataFactory.getRDFSLabel(),
                dataFactory.getRDFSComment(),
                dataFactory.getRDFSIsDefinedBy(),
                dataFactory.getRDFSSeeAlso(),
                dataFactory.getOWLBackwardCompatibleWith(),
                dataFactory.getOWLIncompatibleWith()
        );
    }

    @Nonnull
    @Override
    public Stream<OWLEntity> getBuiltInEntities() {
        return builtInEntities.stream();
    }
}
