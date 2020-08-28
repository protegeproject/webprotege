package edu.stanford.bmir.protege.web.server.search;

import com.google.common.collect.ImmutableList;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import edu.stanford.bmir.protege.web.server.jackson.ObjectMapperProvider;
import edu.stanford.bmir.protege.web.server.persistence.MongoTestUtils;
import edu.stanford.bmir.protege.web.shared.lang.LanguageMap;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityIsDeprecatedCriteria;
import edu.stanford.bmir.protege.web.shared.match.criteria.EntityIsNotDeprecatedCriteria;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilter;
import edu.stanford.bmir.protege.web.shared.search.EntitySearchFilterId;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class EntitySearchFilterRepositoryImpl_IT {

    private MongoClient mongoClient;

    private MongoDatabase database;

    private EntitySearchFilterRepositoryImpl repository;

    private ProjectId projectId;

    @Before
    public void setUp() throws Exception {
        var objectMapper = new ObjectMapperProvider().get();
        mongoClient = MongoTestUtils.createMongoClient();
        database = mongoClient.getDatabase(MongoTestUtils.getTestDbName());
        repository = new EntitySearchFilterRepositoryImpl(database,
                                                          objectMapper);
        projectId = ProjectId.get(UUID.randomUUID().toString());
    }

    @Test
    public void shouldCreateIndexesWithoutError() {
        repository.ensureIndexes();
        repository.ensureIndexes();
    }

    @Test
    public void shouldSaveEntitySearchFilter() {
        EntitySearchFilter filter = saveFirstSearchFilter();
        var savedFilters = repository.getSearchFilters(projectId);
        assertThat(savedFilters, contains(filter));
    }

    @Test
    public void shouldSaveDuplicates() {
        EntitySearchFilter filter = saveFirstSearchFilter();
        repository.saveSearchFilters(ImmutableList.of(filter));
        var filters = repository.getSearchFilters(projectId);
        assertThat(filters, contains(filter));
    }

    @Test
    public void shouldUpdateFilters() {
        var filter = saveFirstSearchFilter();
        var updatedFilter = EntitySearchFilter.get(filter.getId(),
                                                   filter.getProjectId(),
                                                   LanguageMap.of("en", "MyOtherFilter"),
                                                   EntityIsDeprecatedCriteria.get());
        repository.saveSearchFilters(ImmutableList.of(filter));
        repository.saveSearchFilters(ImmutableList.of(updatedFilter));
        var filters = repository.getSearchFilters(projectId);
        assertThat(filters, contains(updatedFilter));
    }

    @After
    public void tearDown() throws Exception {
        database.drop();
        mongoClient.close();
    }

    private EntitySearchFilter saveFirstSearchFilter() {
        var filter = EntitySearchFilter.get(EntitySearchFilterId.createFilterId(),
                                            projectId,
                                            LanguageMap.of("en", "MyFilter"),
                                            EntityIsNotDeprecatedCriteria.get());
        repository.saveSearchFilters(ImmutableList.of(filter));
        return filter;
    }


}