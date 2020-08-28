package edu.stanford.bmir.protege.web.server.lang;

import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.server.change.AddAxiomChange;
import edu.stanford.bmir.protege.web.server.change.OntologyChange;
import edu.stanford.bmir.protege.web.server.change.RemoveAxiomChange;
import edu.stanford.bmir.protege.web.server.index.AxiomsByEntityReferenceIndex;
import edu.stanford.bmir.protege.web.server.index.ProjectOntologiesIndex;
import edu.stanford.bmir.protege.web.shared.lang.DictionaryLanguageUsage;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguageData;
import edu.stanford.bmir.protege.web.shared.shortform.LocalNameDictionaryLanguage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLOntologyID;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.AnnotationAssertion;
import static org.semanticweb.owlapi.apibinding.OWLFunctionalSyntaxFactory.Literal;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-31
 */
@RunWith(MockitoJUnitRunner.class)
public class ActiveLanguagesManagerImpl_TestCase {

    private static final String ES = "es";

    private static final String EN = "en";

    private static final String FR = "fr";

    private static final String LOCAL_NAME = "[LocalName]";

    private static final String PREFIXED_NAME = "[PrefixedName]";

    private ActiveLanguagesManagerImpl impl;

    @Mock
    private ProjectId projectId;

    @Mock
    private AxiomsByEntityReferenceIndex axiomsByEntityReference;

    @Mock
    private ProjectOntologiesIndex projectOntologiesIndex;

    private OWLAnnotationProperty rdfsLabel;

    @Mock
    private OWLOntologyID ontologyId;

    private OWLAnnotationAssertionAxiom annotationAssertion1, annotationAssertion2, annotationAssertion3;

    @Before
    public void setUp() {
        when(projectOntologiesIndex.getOntologyIds())
                .thenAnswer(inv -> Stream.of(ontologyId));

        when(axiomsByEntityReference.getReferencingAxioms(any(), any()))
                .then(inv -> Stream.empty());
        var dataFactory = new OWLDataFactoryImpl();

        rdfsLabel = dataFactory.getRDFSLabel();

        var literal1 = dataFactory.getOWLLiteral("Lex1", EN);
        var literal2 = dataFactory.getOWLLiteral("Lex2", EN);
        var literal3 = dataFactory.getOWLLiteral("Lex3", FR);

        annotationAssertion1 = AnnotationAssertion(rdfsLabel,
                                                   mock(OWLAnnotationSubject.class),
                                                   literal1);
        annotationAssertion2 = AnnotationAssertion(rdfsLabel,
                                                   mock(OWLAnnotationSubject.class),
                                                   literal2);
        annotationAssertion3 = AnnotationAssertion(rdfsLabel,
                                                   mock(OWLAnnotationSubject.class),
                                                   literal3);

        when(axiomsByEntityReference.getReferencingAxioms(rdfsLabel, ontologyId))
                .thenAnswer(inv -> Stream.of(annotationAssertion1, annotationAssertion2, annotationAssertion3));
        impl = new ActiveLanguagesManagerImpl(projectId,
                                              axiomsByEntityReference,
                                              projectOntologiesIndex);
    }

    @Test
    public void shouldGetLanguagesRankedByUsage() {
        var langs = impl.getLanguagesRankedByUsage();
        assertThat(langs, contains(DictionaryLanguage.rdfsLabel(EN),
                                   DictionaryLanguage.rdfsLabel(FR),
                                   DictionaryLanguage.prefixedName(),
                                   DictionaryLanguage.localName()));
    }

    @Test
    public void shouldGetLanguageUsage() {
        assertThatLanguageUsageIs(ImmutableMap.of(EN, 2, FR, 1, PREFIXED_NAME, 0, LOCAL_NAME, 0));
    }

    @Test
    public void shouldHandleRemoveChanges() {
        removeEnglishLabelAnnotationAssertions();
        assertThatLanguageUsageIs(ImmutableMap.of(FR, 1, PREFIXED_NAME, 0, LOCAL_NAME, 0));
    }

    @Test
    public void shouldHandleAddChanges() {
        addSpanishLabelAnnotationAssertions();
        assertThatLanguageUsageIs(ImmutableMap.of(ES, 3, EN, 2, FR, 1, PREFIXED_NAME, 0, LOCAL_NAME, 0));
    }


    private void removeEnglishLabelAnnotationAssertions() {
        impl.handleChanges(asList(
                RemoveAxiomChange.of(ontologyId, annotationAssertion1),
                RemoveAxiomChange.of(ontologyId, annotationAssertion2)
        ));
    }

    private void addSpanishLabelAnnotationAssertions() {
        var changes = new ArrayList<OntologyChange>();
        var numberOfAssertionsToAdd = 3;
        for(var i = 0; i < numberOfAssertionsToAdd; i++) {
            var annotationAssertion = AnnotationAssertion(rdfsLabel,
                                                          mock(OWLAnnotationSubject.class),
                                                          Literal("LexVal", ES));
            changes.add(AddAxiomChange.of(ontologyId, annotationAssertion));
        }
        impl.handleChanges(changes);
    }

    private void assertThatLanguageUsageIs(ImmutableMap<String, Integer> usageMap) {
        var usageList = usageMap.entrySet()
                .stream()
                .map(this::toLanguageUsage)
                .toArray();
        assertThat(impl.getLanguageUsage(), contains(usageList));
    }

    private DictionaryLanguageUsage toLanguageUsage(Map.Entry<String, Integer> entry) {
        var languageTag = entry.getKey();
        var usageCount = entry.getValue();
        if(languageTag.equals(LOCAL_NAME)) {
            return DictionaryLanguageUsage.get(DictionaryLanguage.localName(), usageCount);
        }
        else if(languageTag.equals(PREFIXED_NAME)) {
            return DictionaryLanguageUsage.get(DictionaryLanguage.prefixedName(), usageCount);
        }
        else {
            var langData = DictionaryLanguage.rdfsLabel(languageTag);
            return DictionaryLanguageUsage.get(langData, usageCount);
        }
    }
}
