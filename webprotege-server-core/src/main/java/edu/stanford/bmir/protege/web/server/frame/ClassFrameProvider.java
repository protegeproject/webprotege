package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.shared.frame.ClassFrameTranslatorOptions;
import edu.stanford.bmir.protege.web.shared.frame.PlainClassFrame;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-02
 */
public interface ClassFrameProvider {

    /**
     * Gets the class frame for the specified subject.
     * @param subject The subject.
     * @return The class frame for the subject
     */
    @Nonnull
    PlainClassFrame getFrame(@Nonnull OWLClass subject,
                             @Nonnull ClassFrameTranslatorOptions options);
}
