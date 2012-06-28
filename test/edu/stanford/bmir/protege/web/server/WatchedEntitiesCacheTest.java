package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.data.Watch;
import edu.stanford.bmir.protegex.chao.ontologycomp.api.OntologyComponentFactory;
import edu.stanford.bmir.protegex.chao.ontologycomp.api.Ontology_Component;
import edu.stanford.bmir.protegex.chao.ontologycomp.api.User;
import edu.stanford.smi.protege.model.Project;
import junit.framework.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

import java.util.Arrays;
import java.util.Collection;

/**
 */
public class WatchedEntitiesCacheTest extends TestCase {
    Mockery context = new Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};

    public void setUp(){
        WatchedEntitiesCache.purgeCache();
    }

    public void testWatchedEntitiesByProjectAndUserWithOneResult(){
        final Project project = createProject("myProjectName");
        final Ontology_Component ontologyComponent = createOntologyComponent("one");
        final User user = createUser("userName", Arrays.asList(ontologyComponent), null);
        final OntologyComponentFactory factory = createFactory(user);
        WatchedEntitiesCache.init(project, factory);
        final WatchedEntitiesCache cache = WatchedEntitiesCache.getCache(project);
        assertTrue(cache.isWatchedEntity(user, ontologyComponent));
        assertFalse(cache.isWatchedBranch(user, ontologyComponent));
        assertEquals(Watch.ENTITY_WATCH , cache.getWatchType(user, ontologyComponent));
        assertNull(cache.getWatchedBranches().get(ontologyComponent.getCurrentName()));
    }

    public void testWatchedBranchesByProjectAndUserWithOneResult(){
        final Project project = createProject("myProjectName");
        final Ontology_Component ontologyComponent = createOntologyComponent("one");
        final User user = createUser("userName", null, Arrays.asList(ontologyComponent));
        final OntologyComponentFactory factory = createFactory(user);
        WatchedEntitiesCache.init(project, factory);
        final WatchedEntitiesCache cache = WatchedEntitiesCache.getCache(project);
        assertTrue(cache.isWatchedBranch(user, ontologyComponent));
        assertFalse(cache.isWatchedEntity(user, ontologyComponent));
        assertEquals(Watch.BRANCH_WATCH , cache.getWatchType(user, ontologyComponent));
        assertTrue(cache.getWatchedBranches().get(ontologyComponent.getCurrentName()).contains(user));
    }

    public void testAdditionOfEntityWatchToBranchWatch(){
        final Project project = createProject("myProjectName");
        final Ontology_Component ontologyComponentOne = createOntologyComponent("one");
        final User user = createUser("userName", null, Arrays.asList(ontologyComponentOne));
        final OntologyComponentFactory factory = createFactory(user);
        WatchedEntitiesCache.init(project, factory);
        final WatchedEntitiesCache cache = WatchedEntitiesCache.getCache(project);
        cache.addEntityWatch(ontologyComponentOne, user);
        assertTrue(cache.isWatchedBranch(user, ontologyComponentOne));
        assertTrue(cache.isWatchedEntity(user, ontologyComponentOne));
        assertEquals(Watch.BOTH , cache.getWatchType(user, ontologyComponentOne));
        assertTrue(cache.getWatchedBranches().get(ontologyComponentOne.getCurrentName()).contains(user));
    }

    public void testAdditionOfExtraEntityForEntityWatch(){
        final Project project = createProject("myProjectName");
        final Ontology_Component ontologyComponentOne = createOntologyComponent("one");
        final Ontology_Component ontologyComponentTwo = createOntologyComponent("two");
        final User user = createUser("userName", Arrays.asList(ontologyComponentOne), null);
        final OntologyComponentFactory factory = createFactory(user);
        WatchedEntitiesCache.init(project, factory);
        final WatchedEntitiesCache cache = WatchedEntitiesCache.getCache(project);
        cache.addEntityWatch(ontologyComponentTwo, user);
        assertFalse(cache.isWatchedBranch(user, ontologyComponentOne));
        assertTrue(cache.isWatchedEntity(user, ontologyComponentOne));
        assertFalse(cache.isWatchedBranch(user, ontologyComponentTwo));
        assertTrue(cache.isWatchedEntity(user, ontologyComponentTwo));
        assertEquals(Watch.ENTITY_WATCH, cache.getWatchType(user, ontologyComponentOne));
        assertEquals(Watch.ENTITY_WATCH, cache.getWatchType(user, ontologyComponentTwo));
        assertNull(cache.getWatchedBranches().get(ontologyComponentOne.getCurrentName()));
        assertNull(cache.getWatchedBranches().get(ontologyComponentTwo.getCurrentName()));
    }

    public void testEntityWatchReturnsSet(){
        final Project project = createProject("myProjectName");
        final Ontology_Component ontologyComponentOne = createOntologyComponent("one");
        final Ontology_Component ontologyComponentTwo = createOntologyComponent("two");
        final User user = createUser("userName", Arrays.asList(ontologyComponentOne), null);
        final OntologyComponentFactory factory = createFactory(user);
        WatchedEntitiesCache.init(project, factory);
        final WatchedEntitiesCache cache = WatchedEntitiesCache.getCache(project);
        assertFalse(cache.isWatchedBranch(user, ontologyComponentTwo));
        assertFalse(cache.isWatchedEntity(user, ontologyComponentTwo));
        assertTrue(cache.getEntityWatches(ontologyComponentTwo).isEmpty());
        assertNull(cache.getWatchedBranches().get(ontologyComponentTwo.getCurrentName()));
    }

    public void testAdditionOfExtraEntityForBranchWatch(){
        final Project project = createProject("myProjectName");
        final Ontology_Component ontologyComponentOne = createOntologyComponent("one");
        final Ontology_Component ontologyComponentTwo = createOntologyComponent("two");
        final User user = createUser("userName", null, Arrays.asList(ontologyComponentOne));
        final OntologyComponentFactory factory = createFactory(user);
        WatchedEntitiesCache.init(project, factory);
        final WatchedEntitiesCache cache = WatchedEntitiesCache.getCache(project);
        cache.addBranchWatch(ontologyComponentTwo, user);
        assertTrue(cache.isWatchedBranch(user, ontologyComponentOne));
        assertFalse(cache.isWatchedEntity(user, ontologyComponentOne));
        assertTrue(cache.isWatchedBranch(user, ontologyComponentTwo));
        assertFalse(cache.isWatchedEntity(user, ontologyComponentTwo));
        assertEquals(Watch.BRANCH_WATCH, cache.getWatchType(user, ontologyComponentOne));
        assertEquals(Watch.BRANCH_WATCH, cache.getWatchType(user, ontologyComponentTwo));
        assertTrue(cache.getWatchedBranches().get(ontologyComponentOne.getCurrentName()).contains(user));
        assertTrue(cache.getWatchedBranches().get(ontologyComponentTwo.getCurrentName()).contains(user));
    }


    public void testRemovalOfExtraEntityForEntityWatch(){
        final Project project = createProject("myProjectName");
        final Ontology_Component ontologyComponentOne = createOntologyComponent("one");
        final Ontology_Component ontologyComponentTwo = createOntologyComponent("two");
        final User user = createUser("userName", Arrays.asList(ontologyComponentOne), null);
        final OntologyComponentFactory factory = createFactory(user);
        WatchedEntitiesCache.init(project, factory);
        final WatchedEntitiesCache cache = WatchedEntitiesCache.getCache(project);
        cache.addEntityWatch(ontologyComponentTwo, user);
        assertFalse(cache.isWatchedBranch(user, ontologyComponentOne));
        assertTrue(cache.isWatchedEntity(user, ontologyComponentOne));
        assertFalse(cache.isWatchedBranch(user, ontologyComponentTwo));
        assertTrue(cache.isWatchedEntity(user, ontologyComponentTwo));
        assertEquals(Watch.ENTITY_WATCH, cache.getWatchType(user, ontologyComponentOne));
        assertEquals(Watch.ENTITY_WATCH, cache.getWatchType(user, ontologyComponentTwo));
        assertNull(cache.getWatchedBranches().get(ontologyComponentOne.getCurrentName()));
        assertNull(cache.getWatchedBranches().get(ontologyComponentTwo.getCurrentName()));
        cache.removeEntityWatch(ontologyComponentTwo, user);
        assertFalse(cache.isWatchedEntity(user, ontologyComponentTwo));
        assertNull(cache.getWatchType(user, ontologyComponentTwo));
    }

    public void testRemovalOfExtraEntityForBranchWatch(){
        final Project project = createProject("myProjectName");
        final Ontology_Component ontologyComponentOne = createOntologyComponent("one");
        final Ontology_Component ontologyComponentTwo = createOntologyComponent("two");
        final User user = createUser("userName", null, Arrays.asList(ontologyComponentOne));
        final OntologyComponentFactory factory = createFactory(user);
        WatchedEntitiesCache.init(project, factory);
        final WatchedEntitiesCache cache = WatchedEntitiesCache.getCache(project);
        cache.addBranchWatch(ontologyComponentTwo, user);
        assertTrue(cache.isWatchedBranch(user, ontologyComponentOne));
        assertFalse(cache.isWatchedEntity(user, ontologyComponentOne));
        assertTrue(cache.isWatchedBranch(user, ontologyComponentTwo));
        assertFalse(cache.isWatchedEntity(user, ontologyComponentTwo));
        assertEquals(Watch.BRANCH_WATCH, cache.getWatchType(user, ontologyComponentOne));
        assertEquals(Watch.BRANCH_WATCH, cache.getWatchType(user, ontologyComponentTwo));
        assertTrue(cache.getWatchedBranches().get(ontologyComponentOne.getCurrentName()).contains(user));
        assertTrue(cache.getWatchedBranches().get(ontologyComponentTwo.getCurrentName()).contains(user));
        cache.removeBranchWatch(ontologyComponentTwo, user);
        assertNull(cache.getWatchedBranches().get(ontologyComponentTwo.getCurrentName()));
        assertFalse(cache.isWatchedBranch(user, ontologyComponentTwo));
        assertNull(cache.getWatchType(user, ontologyComponentTwo));
    }

    private OntologyComponentFactory createFactory(final User... users) {
        final OntologyComponentFactory factory = context.mock(OntologyComponentFactory.class);
        context.checking(new Expectations(){{
            allowing(factory).getAllUserObjects(); will(returnValue(Arrays.asList(users)));
        }});
        return factory;
    }

    private Project createProject(final String projectName) {
        final Project project = context.mock(Project.class, projectName);
        context.checking(new Expectations(){{
                allowing(project).getName(); will(returnValue(projectName));
        }});
        return project;
    }

    private User createUser(final String userName, final Collection entityWatches, final Collection branchWatches) {
        final User user = context.mock(User.class, userName);
        context.checking(new Expectations(){{
                allowing(user).getName(); will(returnValue(userName));
                allowing(user).getWatchedEntity(); will(returnValue(entityWatches));
                allowing(user).getWatchedBranch(); will(returnValue(branchWatches));
        }});
        return user;
    }

    private Ontology_Component createOntologyComponent(final String componentName) {
        final Ontology_Component ontology_component = context.mock(Ontology_Component.class, componentName);
        context.checking(new Expectations(){{
                allowing(ontology_component).getCurrentName(); will(returnValue(componentName));
        }});
        return ontology_component;
    }
}
