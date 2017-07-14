package edu.stanford.bmir.protege.web.server.persistence;

import com.mongodb.MongoClient;
import edu.stanford.bmir.protege.web.server.form.CollectionIdConverter;
import edu.stanford.bmir.protege.web.server.form.FormIdConverter;
import edu.stanford.bmir.protege.web.server.inject.MongoClientProvider;
import org.mongodb.morphia.Morphia;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 6 Oct 2016
 */
public class MongoTestUtils {

    private static final String TEST_DB_NAME = "webprotege-test";

    public static MongoClient createMongoClient() {
        return new MongoClientProvider("localhost", 27017).get();
    }

    public static Morphia createMorphia() {
        return new MorphiaProvider(
                new UserIdConverter(),
                new OWLEntityConverter(new OWLDataFactoryImpl()),
                new ProjectIdConverter(),
                new ThreadIdConverter(),
                new CommentIdConverter(),
                new CollectionIdConverter(),
                new FormIdConverter()).get();
    }


    public static String getTestDbName() {
        return TEST_DB_NAME;
    }
}
