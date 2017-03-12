package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.server.access.RoleAssignment;
import edu.stanford.bmir.protege.web.server.user.UserActivityRecord;
import edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread;
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

    @Nonnull
    private final CommentIdConverter commentIdConverter;

    @Inject
    public MorphiaProvider(@Nonnull UserIdConverter userIdConverter,
                           @Nonnull OWLEntityConverter entityConverter,
                           @Nonnull ProjectIdConverter projectIdConverter,
                           @Nonnull ThreadIdConverter threadIdConverter,
                           @Nonnull CommentIdConverter commentIdConverter) {
        this.userIdConverter = userIdConverter;
        this.entityConverter = entityConverter;
        this.projectIdConverter = projectIdConverter;
        this.threadIdConverter = threadIdConverter;
        this.commentIdConverter = commentIdConverter;
    }

    @Override
    public Morphia get() {
        Morphia morphia = new Morphia();

        Mapper mapper = morphia.getMapper();
        mapper.getOptions().setStoreEmpties(true);
        mapper.getOptions().setObjectFactory(new CustomMorphiaObjectFactory());

        Converters converters = mapper.getConverters();
        converters.addConverter(userIdConverter);
        converters.addConverter(entityConverter);
        converters.addConverter(projectIdConverter);
        converters.addConverter(threadIdConverter);
        converters.addConverter(commentIdConverter);

        morphia.map(EntityDiscussionThread.class);
        morphia.map(UserActivityRecord.class);
        morphia.map(RoleAssignment.class);

        return morphia;
    }
}
