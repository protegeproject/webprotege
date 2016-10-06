package edu.stanford.bmir.protege.web.server.persistence;

import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.converters.Converters;
import org.mongodb.morphia.mapping.Mapper;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
public class MorphiaProvider implements Provider<Morphia> {

    @Nonnull
    private final UserIdConverter userIdConverter;

    @Nonnull
    private final OWLEntityConverter entityConverter;

    @Nonnull
    private final ProjectIdConverter projectIdConverter;

    @Nonnull
    private final ThreadIdConverter threadIdConverter;

    @Inject
    public MorphiaProvider(@Nonnull UserIdConverter userIdConverter,
                           @Nonnull OWLEntityConverter entityConverter,
                           @Nonnull ProjectIdConverter projectIdConverter,
                           @Nonnull ThreadIdConverter threadIdConverter) {
        this.userIdConverter = userIdConverter;
        this.entityConverter = entityConverter;
        this.projectIdConverter = projectIdConverter;
        this.threadIdConverter = threadIdConverter;
    }

    @Override
    public Morphia get() {
        Morphia morphia = new Morphia();
        Mapper mapper = morphia.getMapper();
        Converters converters = mapper.getConverters();
        converters.addConverter(userIdConverter);
        converters.addConverter(entityConverter);
        converters.addConverter(projectIdConverter);
        converters.addConverter(threadIdConverter);
        return morphia;
    }
}
