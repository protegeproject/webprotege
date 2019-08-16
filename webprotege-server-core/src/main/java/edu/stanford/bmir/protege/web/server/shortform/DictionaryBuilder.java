package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.server.index.AxiomsByTypeIndex;
import edu.stanford.bmir.protege.web.server.index.EntitiesInProjectSignatureByIriIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectSignatureIndex;
import edu.stanford.bmir.protege.web.server.util.Counter;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.shortform.DictionaryPredicates.isAxiomForDictionary;
import static java.util.Collections.singletonList;
import static org.semanticweb.owlapi.model.AxiomType.ANNOTATION_ASSERTION;

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
    private final AxiomsByTypeIndex axiomsByTypeIndex;

    @Nonnull
    private final EntitiesInProjectSignatureByIriIndex entitiesInSignatureIndex;

    @Nonnull
    private final ProjectSignatureIndex projectSignatureIndex;

    @Inject
    public DictionaryBuilder(@Nonnull ProjectId projectId,
                             @Nonnull ProjectOntologiesIndex projectOntologiesIndex,
                             @Nonnull AxiomsByTypeIndex axiomsByTypeIndex,
                             @Nonnull EntitiesInProjectSignatureByIriIndex entitiesInSignatureIndex,
                             @Nonnull ProjectSignatureIndex projectSignatureIndex) {
        this.projectId = checkNotNull(projectId);
        this.projectOntologiesIndex = checkNotNull(projectOntologiesIndex);
        this.axiomsByTypeIndex = checkNotNull(axiomsByTypeIndex);
        this.entitiesInSignatureIndex = checkNotNull(entitiesInSignatureIndex);
        this.projectSignatureIndex = checkNotNull(projectSignatureIndex);
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
        Counter counter = new Counter();
        projectOntologiesIndex.getOntologyIds()
                    .flatMap(ontId -> axiomsByTypeIndex.getAxiomsByType(ANNOTATION_ASSERTION, ontId))
                    .peek(counter::increment)
                    .forEach(ax -> annotationBasedDictionaries.stream()
                                               .filter(dictionary -> isAxiomForDictionary(ax, dictionary))
                                               .forEach(dictionary -> {
                                                   var iri = (IRI) ax.getSubject();
                                                   var literal = ((OWLLiteral) ax.getValue()).getLiteral();
                                                   entitiesInSignatureIndex.getEntityInSignature(iri).forEach(entity -> dictionary.put(entity, literal));
                                               }));
        logger.info("{} Processed {} axioms in order to build annotation based dictionaries",
                    projectId,
                    String.format("%,d", counter.getCounter()));
    }
}
