package edu.stanford.bmir.protege.web.server.util;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.ImmutableMap;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.util.OWLObjectDuplicator;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-28
 *
 * Replaces IRIs in an OWLObject.  Note IRI replacers are not thread safe.
 */
public class IriReplacer {

    @Nonnull
    private final OWLObjectDuplicator duplicator;

    @AutoFactory
    @Inject
    public IriReplacer(@Provided @Nonnull OWLDataFactory dataFactory,
                       @Nonnull ImmutableMap<IRI, IRI> iriMap) {
        this.duplicator = new OWLObjectDuplicator(dataFactory, checkNotNull(iriMap));
    }


    public <O extends OWLObject> O replaceIris(O owlObject) {
        return duplicator.duplicateObject(owlObject);
    }
}
