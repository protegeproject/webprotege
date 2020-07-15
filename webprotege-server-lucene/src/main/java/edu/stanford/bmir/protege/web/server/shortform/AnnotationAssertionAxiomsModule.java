package edu.stanford.bmir.protege.web.server.shortform;

import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.index.AnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectAnnotationAssertionAxiomsBySubjectIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectSignatureIndex;
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

    public AnnotationAssertionAxiomsModule(ProjectAnnotationAssertionAxiomsBySubjectIndex annotationAssertionAxiomsBySubjectIndex,
                                           ProjectSignatureIndex provideProjectSignatureIndex) {
        this.annotationAssertionAxiomsBySubjectIndex = annotationAssertionAxiomsBySubjectIndex;
        this.provideProjectSignatureIndex = provideProjectSignatureIndex;
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


}
