package edu.stanford.bmir.protege.web.server.change;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 16/03/16
 */
public interface ReverseEngineeredChangeDescriptionGeneratorFactory {

    ReverseEngineeredChangeDescriptionGenerator get(String defaultDescription);
}
