package edu.stanford.bmir.protege.web.server.index;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import edu.stanford.bmir.protege.web.server.index.impl.*;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManager;
import edu.stanford.bmir.protege.web.server.project.DefaultOntologyIdManagerImpl;
import edu.stanford.bmir.protege.web.server.revision.RevisionManager;
import edu.stanford.bmir.protege.web.server.shortform.DeprecatedEntitiesByEntityIndexLuceneImpl;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;
import edu.stanford.owl2lpg.client.bind.index.*;
import edu.stanford.owl2lpg.client.read.annotation.OntologyAnnotationsAccessorModule;
import edu.stanford.owl2lpg.client.read.axiom.AnnotationAssertionAxiomAccessorModule;
import edu.stanford.owl2lpg.client.read.axiom.AssertionAxiomBySubjectAccessorModule;
import edu.stanford.owl2lpg.client.read.axiom.AxiomAccessorModule;
import edu.stanford.owl2lpg.client.read.axiom.CharacteristicsAxiomAccessorModule;
import edu.stanford.owl2lpg.client.read.axiom.ClassAssertionAxiomAccessorModule;
import edu.stanford.owl2lpg.client.read.axiom.AxiomBySubjectAccessorModule;
import edu.stanford.owl2lpg.client.read.axiom.HierarchyAxiomBySubjectAccessorModule;
import edu.stanford.owl2lpg.client.read.entity.EntityAccessorModule;
import edu.stanford.owl2lpg.client.read.individual.NamedIndividualAccessorModule;
import edu.stanford.owl2lpg.client.read.ontology.OntologyAccessorModule;
import edu.stanford.owl2lpg.client.read.ontology.ProjectAccessorModule;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-09-10
 */
@Module(includes = {
    ProjectAccessorModule.class,
    OntologyAccessorModule.class,
    OntologyAnnotationsAccessorModule.class,
    EntityAccessorModule.class,
    NamedIndividualAccessorModule.class,
    AxiomAccessorModule.class,
    AxiomBySubjectAccessorModule.class,
    HierarchyAxiomBySubjectAccessorModule.class,
    AssertionAxiomBySubjectAccessorModule.class,
    AnnotationAssertionAxiomAccessorModule.class,
    ClassAssertionAxiomAccessorModule.class,
    CharacteristicsAxiomAccessorModule.class
})
public class Neo4jIndexModule {

    @Provides
    ClassFrameAxiomsIndex provideClassFrameAxiomsIndex(Neo4jClassFrameAxiomsIndex impl) {
        return impl;
    }

    @Provides
    NamedIndividualFrameAxiomIndex provideNamedIndividualFrameAxiomIndex(Neo4jNamedIndividualFrameAxiomsIndex impl) {
        return impl;
    }

    @Provides
    AnnotationAssertionAxiomsBySubjectIndex provideAnnotationAssertionAxiomsBySubjectIndex(
        Neo4jAnnotationAssertionAxiomsBySubjectIndex impl) {
        return impl;
    }

    @Provides
    AnnotationAssertionAxiomsIndex provideAnnotationAssertionAxiomsIndex(Neo4jAnnotationAssertionAxiomsIndex impl) {
        return impl;
    }

    @Provides
    AnnotationPropertyDomainAxiomsIndex provideAnnotationPropertyDomainAxiomsIndex(
        Neo4jAnnotationPropertyDomainAxiomsIndex impl) {
        return impl;
    }

    @Provides
    AnnotationPropertyRangeAxiomsIndex provideAnnotationPropertyRangeAxiomsIndex(Neo4jAnnotationPropertyRangeAxiomsIndex impl) {
        return impl;
    }

    @Provides
    AxiomsByEntityReferenceIndex provideAxiomsByEntityReferenceIndex(Neo4jAxiomsByEntityReferenceIndex impl) {
        return impl;
    }

    @Provides
    AnnotationAxiomsByIriReferenceIndex provideAxiomsByIriReferenceIndex(Neo4jAnnotationAxiomsByIriReferenceIndex impl) {
        return impl;
    }

    @Provides
    AxiomsByReferenceIndex provideAxiomsByReferenceIndex(Neo4jAxiomsByReferenceIndex impl) {
        return impl;
    }

    @Provides
    AxiomsByTypeIndex provideAxiomsByTypeIndex(Neo4jAxiomsByTypeIndex impl) {
        return impl;
    }

    @Provides
    @ProjectSingleton
    public ClassAssertionAxiomsByClassIndex provideClassAssertionAxiomsByClassIndex(Neo4jClassAssertionAxiomsByClassIndex impl) {
        return impl;
    }

    @Provides
    ClassAssertionAxiomsByIndividualIndex provideClassAssertionAxiomsByIndividualIndex(
        Neo4jClassAssertionAxiomsByIndividualIndex impl) {
        return impl;
    }

    @Provides
    DataPropertyAssertionAxiomsBySubjectIndex provideDataPropertyAssertionAxiomsBySubjectIndex(
        Neo4jDataPropertyAssertionAxiomsBySubjectIndex impl) {
        return impl;
    }

    @Provides
    DataPropertyCharacteristicsIndex provideDataPropertyCharacteristicsIndex(Neo4jDataPropertyCharacteristicsIndex impl) {
        return impl;
    }

    @Provides
    DataPropertyDomainAxiomsIndex provideDataPropertyDomainAxiomsIndex(Neo4jDataPropertyDomainAxiomsIndex impl) {
        return impl;
    }

    @Provides
    DataPropertyRangeAxiomsIndex provideDataPropertyRangeAxiomsIndex(Neo4jDataPropertyRangeAxiomsIndex impl) {
        return impl;
    }

    @Provides
    DefaultOntologyIdManager provideDefaultOntologyIdManager(DefaultOntologyIdManagerImpl impl) {
        return impl;
    }

    @Provides
    DeprecatedEntitiesByEntityIndex provideDeprecatedEntitiesByEntityIndex(DeprecatedEntitiesByEntityIndexLuceneImpl impl) {
        return impl;
    }

    @Provides
    DifferentIndividualsAxiomsIndex provideDifferentIndividualsAxiomsIndex(Neo4jDifferentIndividualsAxiomsIndex impl) {
        return impl;
    }

    @Provides
    DisjointClassesAxiomsIndex provideDisjointClassesAxiomsIndex(Neo4jDisjointClassesAxiomsIndex impl) {
        return impl;
    }

    @Provides
    DisjointDataPropertiesAxiomsIndex provideDisjointDataPropertiesAxiomsIndex(Neo4jDisjointDataPropertiesAxiomsIndex impl) {
        return impl;
    }

    @Provides
    DisjointObjectPropertiesAxiomsIndex provideDisjointObjectPropertiesAxiomsIndex(
        Neo4jDisjointObjectPropertiesAxiomsIndex impl) {
        return impl;
    }

    @Provides
    EntitiesInOntologySignatureByIriIndex provideEntitiesInOntologySignatureByIriIndex(
        Neo4jEntitiesInOntologySignatureByIriIndex impl) {
        return impl;
    }

    @Provides
    EntitiesInOntologySignatureIndex provideEntitiesInOntologySignatureIndex(Neo4jEntitiesInOntologySignatureIndex impl) {
        return impl;
    }

    @Provides
    EntitiesInProjectSignatureByIriIndex provideEntitiesInProjectSignatureByIriIndex(
        Neo4jEntitiesInProjectSignatureByIriIndex impl) {
        return impl;
    }

    @Provides
    EntitiesInProjectSignatureIndex provideEntitiesInProjectSignatureIndexImpl(Neo4jEntitiesInProjectSignatureIndex impl) {
        return impl;
    }

    @Provides
    EquivalentClassesAxiomsIndex provideEquivalentClassesAxiomsIndex(Neo4jEquivalentClassesAxiomsIndex impl) {
        return impl;
    }

    @Provides
    EquivalentDataPropertiesAxiomsIndex provideEquivalentDataPropertiesAxiomsIndex(
        Neo4jEquivalentDataPropertiesAxiomsIndex impl) {
        return impl;
    }

    @Provides
    EquivalentObjectPropertiesAxiomsIndex provideEquivalentObjectPropertiesAxiomsIndex(
        Neo4jEquivalentObjectPropertiesAxiomsIndex impl) {
        return impl;
    }

    @Provides
    IndividualsByTypeIndex provideIndividualsByTypeIndex(Neo4jIndividualsByTypeIndex impl) {
        return impl;
    }

    @Provides
    @ProjectSingleton
    IndividualsIndex provideIndividualsIndex(Neo4jIndividualsIndex impl) {
        return impl;
    }

    @Provides
    InverseObjectPropertyAxiomsIndex provideInverseObjectPropertyAxiomsIndex(Neo4jInverseObjectPropertyAxiomsIndex impl) {
        return impl;
    }

    @Provides
    ObjectPropertyAssertionAxiomsBySubjectIndex provideObjectPropertyAssertionAxiomsBySubjectIndex(
        Neo4jObjectPropertyAssertionAxiomsBySubjectIndex impl) {
        return impl;
    }

    @Provides
    ObjectPropertyCharacteristicsIndex provideObjectPropertyCharacteristicsIndex(Neo4jObjectPropertyCharacteristicsIndex impl) {
        return impl;
    }

    @Provides
    ObjectPropertyDomainAxiomsIndex provideObjectPropertyDomainAxiomsIndex(Neo4jObjectPropertyDomainAxiomsIndex impl) {
        return impl;
    }

    @Provides
    ObjectPropertyRangeAxiomsIndex provideObjectPropertyRangeAxiomsIndex(Neo4jObjectPropertyRangeAxiomsIndex impl) {
        return impl;
    }

    @ProjectSingleton
    @Provides
    OntologyAnnotationsIndex provideOntologyAnnotationsIndex(Neo4jOntologyAnnotationsIndex impl) {
        return impl;
    }

    @Provides
    OntologyAnnotationsSignatureIndex provideOntologyAnnotationsSignatureIndex(@Nonnull Neo4jOntologyAnnotationsSignatureIndex impl) {
        return impl;
    }

    @Provides
    OntologyAxiomsIndex provideOntologyAxiomsIndex(Neo4jOntologyAxiomsIndex impl) {
        return impl;
    }

    @Provides
    OntologyAxiomsSignatureIndex provideOntologyAxiomsSignatureIndex(Neo4jOntologyAxiomsSignatureIndex impl) {
        return impl;
    }

    @Provides
    OntologySignatureByTypeIndex provideOntologySignatureByTypeIndex(Neo4jOntologySignatureByTypeIndex impl) {
        return impl;
    }

    @Provides
    OntologySignatureIndex provideOntologySignatureIndex(Neo4jOntologySignatureIndex impl) {
        return impl;
    }

    @Provides
    ProjectClassAssertionAxiomsByIndividualIndex provideProjectClassAssertionAxiomsByIndividualIndex(
        Neo4jProjectClassAssertionAxiomsByIndividualIndex impl) {
        return impl;
    }

    @Provides
    ProjectOntologiesIndex provideProjectOntologiesIndex(Neo4jProjectOntologiesIndex impl,
                                                         RevisionManager revisionManager) {
//        impl.init(revisionManager);
        return impl;
    }

    @Provides
    ProjectSignatureByTypeIndex provideProjectSignatureByTypeIndex(Neo4jProjectSignatureByTypeIndex impl) {
        return impl;
    }

    @Provides
    ProjectSignatureIndex provideProjectSignatureIndex(Neo4jProjectSignatureIndex impl) {
        return impl;
    }

    @Provides
    PropertyAssertionAxiomsBySubjectIndex providePropertyAssertionAxiomsBySubjectIndex(
        Neo4jPropertyAssertionAxiomsBySubjectIndex impl) {
        return impl;
    }

    @Provides
    SameIndividualAxiomsIndex provideSameIndividualAxiomsIndex(Neo4jSameIndividualAxiomsIndex impl) {
        return impl;
    }

    @Provides
    SubAnnotationPropertyAxiomsBySubPropertyIndex provideSubAnnotationPropertyAxiomsBySubPropertyIndex(
        Neo4jSubAnnotationPropertyAxiomsBySubPropertyIndex impl) {
        return impl;
    }

    @Provides
    SubAnnotationPropertyAxiomsBySuperPropertyIndex provideSubAnnotationPropertyAxiomsBySuperPropertyIndex(
        Neo4jSubAnnotationPropertyAxiomsBySuperPropertyIndex impl) {
        return impl;
    }

    @Provides
    SubClassOfAxiomsBySubClassIndex provideSubClassOfAxiomsBySubClassIndex(Neo4jSubClassOfAxiomsBySubClassIndex impl) {
        return impl;
    }

    @Provides
    SubDataPropertyAxiomsBySubPropertyIndex provideSubDataPropertyAxiomsBySubPropertyIndex(
        Neo4jSubDataPropertyAxiomsBySubPropertyIndex impl) {
        return impl;
    }

    @Provides
    SubObjectPropertyAxiomsBySubPropertyIndex provideSubObjectPropertyAxiomsBySubPropertyIndex(
        Neo4jSubObjectPropertyAxiomsBySubPropertyIndex impl) {
        return impl;
    }

    @Provides
    ProjectAnnotationAssertionAxiomsBySubjectIndex providesHasAnnotationAssertionAxioms(
        Neo4jProjectAnnotationAssertionAxiomsBySubjectIndex impl) {
        return impl;
    }

    @Provides
    @ProjectSingleton
    public AnnotationAssertionAxiomsByValueIndex provideAnnotationAssertionAxiomsByValueIndex(Neo4jAnnotationAssertionAxiomsByValueIndex impl) {
        return impl;
    }


    @Provides
    @IntoSet
    public UpdatableIndex provideAnnotationAssertionAxiomsBySubjectIndexImplIntoSet(AnnotationAssertionAxiomsBySubjectIndexImpl impl) {
        return impl;
    }

    @Provides
    @IntoSet
    public UpdatableIndex provideAnnotationAxiomsByIriReferenceIndexImplIntoSet(AnnotationAxiomsByIriReferenceIndexImpl impl) {
        return impl;
    }

    @Provides
    @IntoSet
    public UpdatableIndex provideAxiomsByEntityReferenceIndexImplIntoSet(AxiomsByEntityReferenceIndexImpl impl) {
        return impl;
    }



    @Provides
    @IntoSet
    public UpdatableIndex provideAnnotationAssertionAxiomsByValueIndexIntoSet(AnnotationAssertionAxiomsByValueIndexImpl impl) {
        return impl;
    }

    @Provides
    @IntoSet
    public UpdatableIndex provideAxiomsByTypeIndexImplIntoSet(AxiomsByTypeIndexImpl impl) {
        return impl;
    }

    @Provides
    @IntoSet
    public UpdatableIndex provideClassAssertionAxiomsByClassIndexImplIntoSet(ClassAssertionAxiomsByClassIndexImpl impl) {
        return impl;
    }

    @Provides
    @IntoSet
    public UpdatableIndex provideClassAssertionAxiomsByIndividualIndexImplIntoSet(ClassAssertionAxiomsByIndividualIndexImpl impl) {
        return impl;
    }

    @Provides
    @IntoSet
    public UpdatableIndex provideDataPropertyAssertionAxiomsBySubjectIndexImplIntoSet(DataPropertyAssertionAxiomsBySubjectIndexImpl impl) {
        return impl;
    }

    @Provides
    @IntoSet
    public UpdatableIndex provideDifferentIndividualsAxiomsIndexImplIntoSet(DifferentIndividualsAxiomsIndexImpl impl) {
        return impl;
    }

    @Provides
    @IntoSet
    public UpdatableIndex provideDisjointClassesAxiomsIndexImplIntoSet(DisjointClassesAxiomsIndexImpl impl) {
        return impl;
    }

    @Provides
    @IntoSet
    public UpdatableIndex provideObjectPropertyAssertionAxiomsBySubjectIndexImplIntoSet(ObjectPropertyAssertionAxiomsBySubjectIndexImpl impl) {
        return impl;
    }

    @Provides
    @IntoSet
    public UpdatableIndex provideOntologyAnnotationsIndexImplIntoSet(OntologyAnnotationsIndexImpl impl) {
        return impl;
    }

    @Provides
    @IntoSet
    public UpdatableIndex provideSameIndividualAxiomsIndexImplIntoSet(SameIndividualAxiomsIndexImpl impl) {
        return impl;
    }

    @Provides
    @IntoSet
    public UpdatableIndex provideSubAnnotationPropertyAxiomsBySuperPropertyIndexImplIntoSet(SubAnnotationPropertyAxiomsBySuperPropertyIndexImpl impl) {
        return impl;
    }

    @Provides
    @IntoSet
    public UpdatableIndex provideSubClassOfAxiomsBySubClassIndexImplIntoSet(SubClassOfAxiomsBySubClassIndexImpl impl) {
        return impl;
    }

    @Provides
    @IntoSet
    public UpdatableIndex provideEquivalentClassesAxiomsIndexIntoSet(EquivalentClassesAxiomsIndexImpl impl) {
        return impl;
    }

    @ProjectSingleton
    @Provides
    IndexUpdater provideIndexUpdater(IndexUpdaterFactory factory) {
        var updater = factory.create();
        updater.buildIndexes();
        return updater;
    }

    @ProjectSingleton
    @Provides
    RootIndex provideRootIndex(RootIndexImpl impl) {
        return impl;
    }

    @Provides
    @IntoSet
    UpdatableIndex provideProjectOntologiesIndexIntoSet(ProjectOntologiesIndexImpl impl) {
        return impl;
    }

    @Provides
    @ProjectSingleton
    public BuiltInOwlEntitiesIndex provideBuiltInOwlEntitiesIndex(@Nonnull BuiltInOwlEntitiesIndexImpl impl) {
        return impl;
    }

    @Provides
    @ProjectSingleton
    public BuiltInSkosEntitiesIndex provideBuiltInSkosEntitiesIndex(@Nonnull BuiltInSkosEntitiesIndexImpl impl) {
        return impl;
    }

    @Provides
    @ProjectSingleton
    public DeprecatedEntitiesIndex provideDeprecatedEntitiesIndex(@Nonnull DeprecatedEntitiesIndexLuceneImpl impl) {
        return impl;
    }
}
