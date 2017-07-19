package edu.stanford.bmir.protege.web.server.persistence;

import edu.stanford.bmir.protege.web.server.access.RoleAssignment;
import edu.stanford.bmir.protege.web.server.collection.CollectionIdConverter;
import edu.stanford.bmir.protege.web.server.form.FormDataConverter;
import edu.stanford.bmir.protege.web.server.form.FormDataValueConverter;
import edu.stanford.bmir.protege.web.server.form.FormIdConverter;
import edu.stanford.bmir.protege.web.server.user.UserActivityRecord;
import edu.stanford.bmir.protege.web.shared.issues.EntityDiscussionThread;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.converters.Converters;
import org.mongodb.morphia.mapping.Mapper;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

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

    @Nonnull
    private final CollectionIdConverter collectionIdConverter;

    @Nonnull
    private final FormIdConverter formIdConverter;


    @Inject
    public MorphiaProvider(@Nonnull UserIdConverter userIdConverter,
                           @Nonnull OWLEntityConverter entityConverter,
                           @Nonnull ProjectIdConverter projectIdConverter,
                           @Nonnull ThreadIdConverter threadIdConverter,
                           @Nonnull CommentIdConverter commentIdConverter,
                           @Nonnull CollectionIdConverter collectionIdConverter,
                           @Nonnull FormIdConverter formIdConverter) {
        this.userIdConverter = userIdConverter;
        this.entityConverter = entityConverter;
        this.projectIdConverter = projectIdConverter;
        this.threadIdConverter = threadIdConverter;
        this.commentIdConverter = commentIdConverter;
        this.collectionIdConverter = collectionIdConverter;
        this.formIdConverter = formIdConverter;
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
        FormDataValueConverter formDataValueConverter = new FormDataValueConverter(new OWLDataFactoryImpl(),
                                                               entityConverter);
        converters.addConverter(formDataValueConverter);
        converters.addConverter(new FormDataConverter(formDataValueConverter));
        converters.addConverter(collectionIdConverter);
        converters.addConverter(formIdConverter);
        converters.addConverter(new CollectionIdConverter());
        morphia.map(EntityDiscussionThread.class);
        morphia.map(UserActivityRecord.class);
        morphia.map(RoleAssignment.class);

        return morphia;
    }
}
