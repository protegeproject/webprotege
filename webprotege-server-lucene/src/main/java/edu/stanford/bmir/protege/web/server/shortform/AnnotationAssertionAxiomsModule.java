package edu.stanford.bmir.protege.web.server.shortform;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.index.*;
import edu.stanford.bmir.protege.web.shared.inject.ProjectSingleton;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-07-13
 */
@Module
public class AnnotationAssertionAxiomsModule {

    private ProjectOntologiesIndex projectOntologiesIndex;

    private ProjectAnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubjectIndex;

    private ProjectSignatureIndex provideProjectSignatureIndex;

    private EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex;

    private EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex;

    private AnnotationAssertionAxiomsByValueIndex annotationAssertionAxiomsByValueIndex;

    public AnnotationAssertionAxiomsModule(ProjectOntologiesIndex projectOntologiesIndex,
                                           ProjectAnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubjectIndex,
                                           ProjectSignatureIndex provideProjectSignatureIndex,
                                           EntitiesInProjectSignatureIndex entitiesInProjectSignatureIndex,
                                           EntitiesInProjectSignatureByIriIndex entitiesInProjectSignatureByIriIndex,
                                           AnnotationAssertionAxiomsByValueIndex annotationAssertionAxiomsByValueIndex) {
        this.projectOntologiesIndex = projectOntologiesIndex;
        this.annotationAssertionAxiomsBySubjectIndex = annotationAssertionAxiomsBySubjectIndex;
        this.provideProjectSignatureIndex = provideProjectSignatureIndex;
        this.entitiesInProjectSignatureIndex = entitiesInProjectSignatureIndex;
        this.entitiesInProjectSignatureByIriIndex = entitiesInProjectSignatureByIriIndex;
        this.annotationAssertionAxiomsByValueIndex = annotationAssertionAxiomsByValueIndex;
    }

    @Provides
    @ProjectSingleton
    ProjectAnnotationAssertionAxiomsBySubjectIndex provideAnnotationAssertionAxiomsBySubjectIndex() {
        return annotationAssertionAxiomsBySubjectIndex;
    }

    @Provides
    @ProjectSingleton
    public ProjectSignatureIndex getProvideProjectSignatureIndex() {
        return provideProjectSignatureIndex;
    }

    @Provides
    @ProjectSingleton
    public EntitiesInProjectSignatureIndex provideEntitiesInProjectSignatureIndex() {
        return entitiesInProjectSignatureIndex;
    }

    @Provides
    @ProjectSingleton
    public AnnotationAssertionAxiomsByValueIndex provideAnnotationAssertionAxiomsByValueIndex() {
        return annotationAssertionAxiomsByValueIndex;
    }

    @Provides
    @ProjectSingleton
    public EntitiesInProjectSignatureByIriIndex provideEntitiesInProjectSignatureByIriIndex() {
        return entitiesInProjectSignatureByIriIndex;
    }

    @Provides
    @ProjectSingleton
    public ProjectOntologiesIndex provideProjectOntologiesIndex() {
        return projectOntologiesIndex;
    }
}
