package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.shared.issues.ThreadId;
import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.mapping.MappedField;

import javax.inject.Inject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
public class ThreadIdConverter extends TypeSafeConverter<String, ThreadId> implements SimpleValueConverter {

    @Inject
    public ThreadIdConverter() {
        super(ThreadId.class);
    }

    @Override
    public ThreadId decodeObject(String fromDBObject, MappedField optionalExtraInfo) {
        return new ThreadId(fromDBObject);
    }

    @Override
    public String encodeObject(ThreadId value, MappedField optionalExtraInfo) {
        return value.getId();
    }
}
