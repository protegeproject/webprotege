package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.LocalNameDictionaryLanguage;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLEntityProvider;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.semanticweb.owlapi.vocab.SKOSVocabulary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.semanticweb.owlapi.model.EntityType.ANNOTATION_PROPERTY;
import static org.semanticweb.owlapi.vocab.OWLRDFVocabulary.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
@ProjectSingleton
public class BuiltInShortFormDictionary {

    @Nonnull
    private final ShortFormCache shortFormCache;

    @Nonnull
    private final OWLEntityProvider entityProvider;

    @Inject
    public BuiltInShortFormDictionary(@Nonnull ShortFormCache shortFormCache,
                                      @Nonnull OWLEntityProvider entityProvider) {
        this.shortFormCache = checkNotNull(shortFormCache);
        this.entityProvider = checkNotNull(entityProvider);
    }

    @Nullable
    public String getShortForm(OWLEntity entity, @Nullable String defaultShortForm) {
        return shortFormCache.getShortFormOrElse(entity, defaultShortForm);
    }

    @Nonnull
    public Stream<ShortFormMatch> getShortFormsContaining(@Nonnull List<SearchString> searchStrings,
                                                          @Nonnull Set<EntityType<?>> entityTypes) {
        if(entityTypes.isEmpty()) {
            return Stream.empty();
        }
        return shortFormCache.getShortFormsContaining(searchStrings,
                                                      entityTypes,
                                                      (entity, shortForm, matchCount, matchPositions)
                -> ShortFormMatch.get(entity, shortForm, LocalNameDictionaryLanguage.get(),
                                      ImmutableList.of()));
    }

    public void load() {
        loadBuiltInOwlVocabulary();
        loadBuiltInDublinCoreVocabulary();
        loadBuiltInSkosVocabulary();
        loadOwl2Datatypes();
    }

    private void loadBuiltInOwlVocabulary() {
        putClass(OWL_THING);
        putClass(OWL_NOTHING);
        putObjectProperty(OWL_TOP_OBJECT_PROPERTY);
        putObjectProperty(OWL_BOTTOM_OBJECT_PROPERTY);
        putDataProperty(OWL_TOP_DATA_PROPERTY);
        putDataProperty(OWL_BOTTOM_DATA_PROPERTY);

        putAnnotationProperty(OWL_VERSION_INFO);
        putAnnotationProperty(OWL_BACKWARD_COMPATIBLE_WITH);
        putAnnotationProperty(OWL_PRIOR_VERSION);
        putAnnotationProperty(OWL_INCOMPATIBLE_WITH);
        putAnnotationProperty(OWL_DEPRECATED);

        putAnnotationProperty(RDFS_LABEL);
        putAnnotationProperty(RDFS_COMMENT);
        putAnnotationProperty(RDFS_SEE_ALSO);
        putAnnotationProperty(RDFS_IS_DEFINED_BY);
    }

    private void putClass(OWLRDFVocabulary vocabulary) {
        shortFormCache.put(entityProvider.getOWLClass(vocabulary.getIRI()), vocabulary.getPrefixedName());
    }

    private void putAnnotationProperty(OWLRDFVocabulary vocabulary) {
        shortFormCache.put(entityProvider.getOWLAnnotationProperty(vocabulary.getIRI()), vocabulary.getPrefixedName());
    }

    private void putObjectProperty(OWLRDFVocabulary vocabulary) {
        shortFormCache.put(entityProvider.getOWLObjectProperty(vocabulary.getIRI()), vocabulary.getPrefixedName());
    }

    private void putDataProperty(OWLRDFVocabulary vocabulary) {
        shortFormCache.put(entityProvider.getOWLDataProperty(vocabulary.getIRI()), vocabulary.getPrefixedName());
    }

    private void loadBuiltInDublinCoreVocabulary() {
        for(DublinCoreVocabulary v : DublinCoreVocabulary.values()) {
            shortFormCache.put(entityProvider.getOWLAnnotationProperty(v.getIRI()), v.getPrefixedName());
        }
    }

    private void loadBuiltInSkosVocabulary() {
        for(SKOSVocabulary v : SKOSVocabulary.values()) {
            if (v.getEntityType().equals(ANNOTATION_PROPERTY)) {
                shortFormCache.put(entityProvider.getOWLAnnotationProperty(v.getIRI()), v.getPrefixedName());
            }
        }
    }

    private void loadOwl2Datatypes() {
        for(OWL2Datatype d : OWL2Datatype.values()) {
            shortFormCache.put(entityProvider.getOWLDatatype(d.getIRI()), d.getPrefixedName());
        }
    }



}
