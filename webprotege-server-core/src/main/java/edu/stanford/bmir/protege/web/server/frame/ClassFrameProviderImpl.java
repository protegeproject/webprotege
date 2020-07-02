package edu.stanford.bmir.protege.web.server.frame;

import edu.stanford.bmir.protege.web.server.frame.translator.Class2ClassFrameTranslatorFactory;
import edu.stanford.bmir.protege.web.shared.frame.ClassFrameTranslationOptions;
import edu.stanford.bmir.protege.web.shared.frame.PlainClassFrame;
import org.semanticweb.owlapi.model.OWLClass;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-04-02
 */
public class ClassFrameProviderImpl implements ClassFrameProvider {

    @Nonnull
    private final Class2ClassFrameTranslatorFactory translatorFactory;

    @Inject
    public ClassFrameProviderImpl(@Nonnull Class2ClassFrameTranslatorFactory classFrameTranslatorFactory) {
        this.translatorFactory = classFrameTranslatorFactory;
    }

    @Nonnull
    @Override
    public PlainClassFrame getFrame(@Nonnull OWLClass subject,
                                    @Nonnull ClassFrameTranslationOptions options) {
        var translator = translatorFactory.create(options);
        return translator.getFrame(subject);
    }
}
