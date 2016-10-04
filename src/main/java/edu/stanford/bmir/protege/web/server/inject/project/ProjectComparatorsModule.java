package edu.stanford.bmir.protege.web.server.inject.project;

import com.google.common.collect.ImmutableList;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import edu.stanford.bmir.protege.web.server.hierarchy.AssertedClassHierarchyProvider;
import edu.stanford.bmir.protege.web.server.hierarchy.OWLObjectHierarchyProvider;
import edu.stanford.bmir.protege.web.server.metrics.MetricCalculator;
import edu.stanford.bmir.protege.web.server.metrics.MetricCalculatorsProvider;
import edu.stanford.bmir.protege.web.server.owlapi.*;
import edu.stanford.bmir.protege.web.server.owlapi.HasApplyChanges;
import edu.stanford.bmir.protege.web.server.shortform.DefaultShortFormAnnotationPropertyIRIs;
import edu.stanford.bmir.protege.web.shared.HasAnnotationAssertionAxioms;
import edu.stanford.bmir.protege.web.shared.HasGetEntitiesWithIRI;
import edu.stanford.bmir.protege.web.shared.axiom.AxiomComparatorImpl;
import edu.stanford.bmir.protege.web.shared.object.*;
import edu.stanford.bmir.protege.web.shared.renderer.HasHtmlBrowserText;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.model.*;

import java.util.Comparator;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 4 Oct 2016
 */
@Module
abstract class ProjectComparatorsModule {

}
