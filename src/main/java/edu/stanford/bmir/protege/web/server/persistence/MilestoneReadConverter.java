package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.issues.Milestone;
import org.springframework.core.convert.converter.Converter;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2 Aug 16
 */
public class MilestoneReadConverter implements Converter<String, Milestone> {

    @Override
    public Milestone convert(String s) {
        return new Milestone(s);
    }
}
