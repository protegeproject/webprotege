package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.shared.inject.ApplicationSingleton;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.semanticweb.owlapi.model.EntityType.ANNOTATION_PROPERTY;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
@ApplicationSingleton
public class BuiltInShortFormCache {

    @Nonnull
    private final ShortFormCache shortFormCache;

    @Inject
    public BuiltInShortFormCache(@Nonnull ShortFormCache shortFormCache) {
        this.shortFormCache = checkNotNull(shortFormCache);
    }

    public String getShortForm(OWLEntity entity, String defaultShortForm) {
        return shortFormCache.getShortFormOrElse(entity.getIRI(), (i) -> defaultShortForm);
    }

    public BuiltInShortFormCache create(@Nonnull ShortFormCache cache) {
        BuiltInShortFormCache builtInShortFormCache = new BuiltInShortFormCache(cache);
        builtInShortFormCache.load();
        return builtInShortFormCache;
    }

    private void load() {
        loadBuiltInOwlVocabulary();
        loadBuiltInDublinCoreVocabulary();
        loadBuiltInSkosVocabulary();
    }

    private void loadBuiltInOwlVocabulary() {
        put(OWL_THING);
        put(OWL_NOTHING);
        put(OWL_TOP_OBJECT_PROPERTY);
        put(OWL_BOTTOM_OBJECT_PROPERTY);
        put(OWL_TOP_DATA_PROPERTY);
        put(OWL_BOTTOM_DATA_PROPERTY);

        put(OWL_VERSION_INFO);
        put(OWL_BACKWARD_COMPATIBLE_WITH);
        put(OWL_PRIOR_VERSION);
        put(OWL_INCOMPATIBLE_WITH);
        put(OWL_DEPRECATED);

        put(RDFS_LABEL);
        put(RDFS_COMMENT);
        put(RDFS_SEE_ALSO);
        put(RDFS_IS_DEFINED_BY);
    }

    private void put(@Nonnull OWLRDFVocabulary v) {
        shortFormCache.put(v.getIRI(), v.getPrefixedName());
    }

    private void loadBuiltInDublinCoreVocabulary() {
        for(DublinCoreVocabulary v : DublinCoreVocabulary.values()) {
            shortFormCache.put(v.getIRI(), v.getPrefixedName());
        }
    }

    private void loadBuiltInSkosVocabulary() {
        for(SKOSVocabulary v : SKOSVocabulary.values()) {
            if (v.getEntityType().equals(ANNOTATION_PROPERTY)) {
                shortFormCache.put(v.getIRI(), v.getPrefixedName());
            }
        }
    }

    private void loadOwl2Datatypes() {
        for(OWL2Datatype d : OWL2Datatype.values()) {
            shortFormCache.put(d.getIRI(), d.getPrefixedName());
        }
    }



}
