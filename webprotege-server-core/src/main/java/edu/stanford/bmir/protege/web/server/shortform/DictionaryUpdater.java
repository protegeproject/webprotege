package edu.stanford.bmir.protege.web.server.shortform;

import edu.stanford.bmir.protege.web.server.index.ProjectAnnotationAssertionAxiomsBySubjectIndex;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.bmir.protege.web.server.shortform.DictionaryPredicates.isAxiomForDictionary;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Apr 2018
 */
public class DictionaryUpdater {

    @Nonnull
    private final ProjectAnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxioms;

    @Nonnull
    private final LocalNameExtractor extractor = new LocalNameExtractor();

    @Inject
    public DictionaryUpdater(@Nonnull ProjectAnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxioms) {
        this.annotationAssertionAxioms = checkNotNull(annotationAssertionAxioms);
    }

    public void update(@Nonnull Dictionary dictionary,
                       @Nonnull Collection<OWLEntity> entities) {
        checkNotNull(entities).forEach(entity -> {
            dictionary.remove(entity);
            if (dictionary.getLanguage().isAnnotationBased()) {
                annotationAssertionAxioms.getAnnotationAssertionAxioms(entity.getIRI())
                        .filter(ax -> isAxiomForDictionary(ax, dictionary))
                            .forEach(ax -> {
                                OWLLiteral literal = (OWLLiteral) ax.getValue();
                                String lexicalValue = literal.getLiteral();
                                dictionary.put(entity, lexicalValue);
                            });
            }
            else {
                String localName = extractor.getLocalName(entity.getIRI());
                dictionary.put(entity, localName);
            }
        });

    }


}
