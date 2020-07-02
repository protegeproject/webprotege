package edu.stanford.bmir.protege.web.server.frame.translator;

import edu.stanford.bmir.protege.web.server.frame.Mode;
import edu.stanford.bmir.protege.web.server.owlapi.NonCachingDataFactory;
import edu.stanford.bmir.protege.web.shared.frame.PlainClassFrame;
import edu.stanford.bmir.protege.web.shared.frame.PlainPropertyValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-03
 */
public class ClassFrame2FrameAxiomsTranslator {


    @Nonnull
    private final OWLDataFactory dataFactory;

    @Nonnull
    private final PropertyValue2AxiomTranslator propertyValue2AxiomTranslator;

    @Inject
    public ClassFrame2FrameAxiomsTranslator(@Nonnull OWLDataFactory dataFactory,
                                            @Nonnull PropertyValue2AxiomTranslator propertyValue2AxiomTranslator) {
        this.dataFactory = dataFactory;
        this.propertyValue2AxiomTranslator = propertyValue2AxiomTranslator;
    }

    @Nonnull
    public Set<OWLAxiom> getAxioms(@Nonnull PlainClassFrame frame, @Nonnull Mode mode) {
        return translateToAxioms(frame.getSubject(), frame, mode);
    }

    private Set<OWLAxiom> translateToAxioms(OWLClass subject, PlainClassFrame classFrame, Mode mode) {
        var result = new HashSet<OWLAxiom>();
        for(OWLClass parent : classFrame.getParents()) {
            result.add(dataFactory.getOWLSubClassOfAxiom(subject, parent));
        }
        for(PlainPropertyValue propertyValue : classFrame.getPropertyValues()) {
            result.addAll(propertyValue2AxiomTranslator.getAxioms(subject, propertyValue, mode));
        }
        return result;
    }



}
