package edu.stanford.bmir.protege.web.server.shortform;

import com.google.common.collect.Multimaps;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.server.util.Counter;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.singletonList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
public class DictionaryBuilder {

    private static final Logger logger = LoggerFactory.getLogger(DictionaryBuilder.class);

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final ProjectOntologiesIndex projectOntologiesIndex;

    @Nonnull
    private final AxiomsByEntityReferenceIndex axiomsByEntityReferenceIndex;

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex;

    @Nonnull
    private final ProjectSignatureIndex projectSignatureIndex;

    @Nonnull
    private final OWLEntityProvider entityProvider;

    @Inject
    public DictionaryBuilder(@Nonnull ProjectId projectId,
                             @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                             @Nonnull AxiomsByEntityReferenceIndex axiomsByEntityReferenceIndex,
                             @Nonnull EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex,
                             @Nonnull ProjectSignatureIndex projectSignatureIndex,
                             @Nonnull OWLEntityProvider entityProvider) {
        this.projectId = checkNotNull(projectId);
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.axiomsByEntityReferenceIndex = checkNotNull(axiomsByEntityReferenceIndex);
        this.entitiesInProjectSignatureByIriIndex = checkNotNull(entitiesInProjectSignatureByIriIndex);
        this.projectSignatureIndex = checkNotNull(projectSignatureIndex);
        this.entityProvider = checkNotNull(entityProvider);
    }

    /**
     * Builds a dictionary.
     */
    public void build(@Nonnull Dictionary dictionary) {
        buildAll(singletonList(dictionary));
    }

    /**
     * Builds the specified dictionaries
     */
    public void buildAll(List<Dictionary> dictionaries) {
        List<Dictionary> annotationBasedDictionaries = new ArrayList<>();
        Dictionary localNameDictionary = null;
        for (Dictionary dictionary : dictionaries) {
            if (dictionary.getLanguage().isAnnotationBased()) {
                annotationBasedDictionaries.add(dictionary);
            }
            else {
                localNameDictionary = dictionary;
            }
        }
        buildAnnotationBasedDictionaries(annotationBasedDictionaries);
        if (localNameDictionary != null) {
            buildLocalNameDictionary(localNameDictionary);
        }
    }

    private void buildLocalNameDictionary(Dictionary localNameDictionary) {
        LocalNameExtractor extractor = new LocalNameExtractor();
        projectSignatureIndex.getSignature()
                    .forEach(entity -> {
                        var shortForm = extractor.getLocalName(entity.getIRI());
                        if (!shortForm.isEmpty()) {
                            localNameDictionary.put(entity, shortForm);
                        }
                    });
    }

    private void buildAnnotationBasedDictionaries(List<Dictionary> annotationBasedDictionaries) {
        if (annotationBasedDictionaries.isEmpty()) {
            return;
        }

        annotationBasedDictionaries
                .stream()
                .filter(dictionary -> dictionary.getLanguage().isAnnotationBased())
                .forEach(this::buildAnnotationBasedDictionary);
    }

    private void buildAnnotationBasedDictionary(Dictionary dictionary) {
        projectOntologiesIndex.getOntologyIds().forEach(ontId -> buildDictionaryForOntology(dictionary, ontId));
    }

    private void buildDictionaryForOntology(Dictionary dictionary, OWLOntologyID ontId) {
        var dictionaryLanguage = dictionary.getLanguage();
        var propertyIri = dictionaryLanguage.getAnnotationPropertyIri();
        var annotationProperty = entityProvider.getOWLAnnotationProperty(propertyIri);
        var tempMap = new HashMap<OWLEntity, String>();
        axiomsByEntityReferenceIndex.getReferencingAxioms(annotationProperty, ontId)
                                    .filter(ax -> ax instanceof OWLAnnotationAssertionAxiom)
                                    .map(ax -> (OWLAnnotationAssertionAxiom) ax)
                                    .filter(ax -> ax.getValue().isLiteral())
                                    .filter(ax -> ax.getSubject().isIRI())
                                    .forEach(ax -> {
                                        var iri = (IRI) ax.getSubject();
                                        var literal = ((OWLLiteral) ax.getValue()).getLiteral();
                                        entitiesInProjectSignatureByIriIndex.getEntitiesInSignature(iri)
                                                                            .forEach(entity -> {
                                                                                tempMap.put(entity, literal);
                                                                            });

                                    });
        dictionary.putAll(tempMap);
    }
}
