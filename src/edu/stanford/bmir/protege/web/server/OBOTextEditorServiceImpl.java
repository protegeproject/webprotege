package edu.stanford.bmir.protege.web.server;

import edu.stanford.bmir.protege.web.client.rpc.OBOTextEditorService;
import edu.stanford.bmir.protege.web.client.rpc.data.NotSignedInException;
import edu.stanford.bmir.protege.web.server.obo.OBONamespaceCache;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProject;
import edu.stanford.bmir.protege.web.server.owlapi.OWLAPIProjectManager;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLObjectPropertyData;
import edu.stanford.bmir.protege.web.shared.obo.*;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import org.coode.owlapi.obo.parser.OBOPrefix;
import org.coode.owlapi.obo.parser.OBOVocabulary;
import org.obolibrary.obo2owl.Obo2OWLConstants;
import org.obolibrary.oboformat.parser.OBOFormatConstants;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 20/05/2012
 */
public class OBOTextEditorServiceImpl extends WebProtegeRemoteServiceServlet implements OBOTextEditorService {


    public synchronized Set<OBONamespace> getNamespaces(ProjectId projectId) {
//        if (cache == null) {
        OWLAPIProject project = getProject(projectId);
        OBONamespaceCache cache = OBONamespaceCache.createCache(project);
//        }
        return cache.getNamespaces();
    }

    public OBOTermId getTermId(ProjectId projectId, OWLEntity entity) {
        // IRI
        IRI iri = entity.getIRI();
        String id = toOBOId(iri);
        // rdfs:label
        String label = getStringAnnotationValue(projectId, iri, OWLRDFVocabulary.RDFS_LABEL.getIRI(), id);
        // namespace
        String namespace = getStringAnnotationValue(projectId, iri, OBOVocabulary.NAMESPACE.getIRI(), "");
        return new OBOTermId(id, label, namespace);
    }


    public void setTermId(ProjectId projectId, OWLEntity entity, OBOTermId termId) {
        OWLAPIProject project = getProject(projectId);
        OBOTermId existingTermId = getTermId(projectId, entity);
        IRI iri = entity.getIRI();
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        StringBuilder description = new StringBuilder();
        if (!existingTermId.getName().equals(termId.getName())) {
            // Update label
            changes.addAll(replaceStringAnnotationValue(projectId, iri, OWLRDFVocabulary.RDFS_LABEL.getIRI(), termId.getName()));
            description.append("Set term name to ");
            description.append(termId.getName());
            description.append(" ");
        }
        if (!existingTermId.getNamespace().equals(termId.getNamespace())) {
            changes.addAll(replaceStringAnnotationValue(projectId, iri, OBOVocabulary.NAMESPACE.getIRI(), termId.getNamespace()));
            description.append("Set term namespace to ");
            description.append(termId.getNamespace());
        }
        if (!changes.isEmpty()) {
            UserId userId = getUserInSessionAndEnsureSignedIn();
            project.applyChanges(userId, changes, description.toString().trim());
        }
    }

    public List<OBOXRef> getXRefs(ProjectId projectId, OWLEntity term) {
        OWLAPIProject project = getProject(projectId);
        IRI subject = term.getIRI();
        List<OBOXRef> xrefs = new ArrayList<OBOXRef>();
        for (OWLAnnotationAssertionAxiom ax : project.getRootOntology().getAnnotationAssertionAxioms(subject)) {
            final OWLAnnotationProperty property = ax.getProperty();
            if (isXRefProperty(property)) {
                OBOXRef xref = convertAnnotationToXRef(ax.getAnnotation(), ax.getAnnotations());
                xrefs.add(xref);
            }
        }
        return xrefs;
    }

    private boolean isXRefProperty(OWLAnnotationProperty property) {
        IRI iri = getXRefPropertyIRI();
        return property.getIRI().equals(iri);
    }

    private IRI getXRefPropertyIRI() {
        return getIRI(OBOFormatConstants.OboFormatTag.TAG_XREF);
    }

    public void setXRefs(ProjectId projectId, OWLEntity term, List<OBOXRef> xrefs) throws NotSignedInException {
        ensureSignedIn();
        IRI subject = term.getIRI();
        Set<OWLAnnotation> annotations = convertOBOXRefsToOWLAnnotations(projectId, xrefs);
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        OWLAPIProject project = getProject(projectId);
        OWLOntology rootOntology = project.getRootOntology();

        // Remove OLD
        for (OWLAnnotationAssertionAxiom ax : rootOntology.getAnnotationAssertionAxioms(subject)) {
            if (isXRefProperty(ax.getProperty())) {
                changes.add(new RemoveAxiom(rootOntology, ax));
            }
        }

        // Add NEW
        for (OWLAnnotation annotation : annotations) {
            OWLDataFactory df = project.getDataFactory();
            changes.add(new AddAxiom(rootOntology, df.getOWLAnnotationAssertionAxiom(subject, annotation)));
        }

        project.applyChanges(getUserInSessionAndEnsureSignedIn(), changes, "Set XRefs");

    }

    private String getStringAnnotationValue(ProjectId projectId, org.semanticweb.owlapi.model.IRI annotationSubject, org.semanticweb.owlapi.model.IRI annotationPropertyIRI, String defaultValue) {
        OWLAPIProject project = getProject(projectId);
        OWLAnnotationAssertionAxiom labelAnnotation = null;
        for (OWLOntology ontology : project.getRootOntology().getImportsClosure()) {
            Set<OWLAnnotationAssertionAxiom> annotationAssertionAxioms = ontology.getAnnotationAssertionAxioms(annotationSubject);
            for (OWLAnnotationAssertionAxiom ax : annotationAssertionAxioms) {
                if (ax.getProperty().getIRI().equals(annotationPropertyIRI)) {
                    labelAnnotation = ax;
                    break;
                }
            }
        }

        String label = defaultValue;
        if (labelAnnotation != null) {
            label = getStringValue(labelAnnotation);
        }
        return label;
    }

    public List<OWLOntologyChange> replaceStringAnnotationValue(ProjectId projectId, org.semanticweb.owlapi.model.IRI annotationSubject, org.semanticweb.owlapi.model.IRI annotationPropertyIRI, String replaceWith) {
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        OWLAPIProject project = getProject(projectId);
        OWLOntology rootOntology = project.getRootOntology();

        OWLDataFactory df = project.getDataFactory();
        OWLAnnotationProperty property = df.getOWLAnnotationProperty(annotationPropertyIRI);
        OWLLiteral value = df.getOWLLiteral(replaceWith);


        for (OWLOntology ontology : rootOntology.getImportsClosure()) {
            for (OWLAnnotationAssertionAxiom ax : ontology.getAnnotationAssertionAxioms(annotationSubject)) {
                if (ax.getProperty().getIRI().equals(annotationPropertyIRI)) {
                    changes.add(new RemoveAxiom(ontology, ax));
                    if (!replaceWith.isEmpty()) {
                        changes.add(new AddAxiom(rootOntology, df.getOWLAnnotationAssertionAxiom(property, annotationSubject, value, ax.getAnnotations())));
                    }
                }
            }
        }
        if (!replaceWith.isEmpty() && changes.isEmpty()) {
            // No previous value, so set new one
            changes.add(new AddAxiom(rootOntology, df.getOWLAnnotationAssertionAxiom(property, annotationSubject, value)));
        }
        return changes;
    }

    private String getStringValue(OWLAnnotationAssertionAxiom labelAnnotation) {
        return labelAnnotation.getValue().accept(new OWLAnnotationValueVisitorEx<String>() {
            public String visit(org.semanticweb.owlapi.model.IRI iri) {
                return iri.toString();
            }

            public String visit(OWLAnonymousIndividual individual) {
                return individual.getID().getID();
            }

            public String visit(OWLLiteral literal) {
                return literal.getLiteral();
            }
        });
    }

    public Set<OBOTermSubset> getSubsets() {
        return null;
    }

    public void addSubset(OBOTermSubset subset) {
    }

    public void removeSubset(OBOTermSubset subset) {
    }

    public OBOTermDefinition getDefinition(ProjectId projectId, OWLEntity term) {
        if (!(term.isOWLClass())) {
            return OBOTermDefinition.empty();
        }
        OWLAPIProject project = getProject(projectId);
        OWLAnnotationAssertionAxiom definitionAnnotation = null;
        for (OWLOntology ontology : project.getRootOntology().getImportsClosure()) {
            Set<OWLAnnotationAssertionAxiom> annotationAssertionAxioms = ontology.getAnnotationAssertionAxioms(term.getIRI());
            for (OWLAnnotationAssertionAxiom ax : annotationAssertionAxioms) {
                OWLAnnotationProperty property = ax.getProperty();
                if (isOBODefinitionProperty(property)) {
                    definitionAnnotation = ax;
                    break;
                }
            }
        }

        if (definitionAnnotation != null) {
            String value = definitionAnnotation.getValue().accept(new OWLAnnotationValueVisitorEx<String>() {
                public String visit(org.semanticweb.owlapi.model.IRI iri) {
                    return iri.toString();
                }

                public String visit(OWLAnonymousIndividual individual) {
                    return individual.getID().getID();
                }

                public String visit(OWLLiteral literal) {
                    return literal.getLiteral();
                }
            });
            List<OBOXRef> xrefs = getXRefs(definitionAnnotation);
            return new OBOTermDefinition(xrefs, value);
        }
        else {
            return null;
        }
    }

    private boolean isOBODefinitionProperty(OWLAnnotationProperty property) {
        IRI propertyIRI = property.getIRI();
        IRI defIRI = getIRI(OBOFormatConstants.OboFormatTag.TAG_DEF);
        if (propertyIRI.equals(defIRI)) {
            return true;
        }
        String fragment = propertyIRI.getFragment();
        if (fragment == null) {
            return false;
        }
        return fragment.endsWith(IAOVocabulary.DEFINITION.getSuffix());
    }

    public void setDefinition(ProjectId projectId, OWLEntity term, OBOTermDefinition definition) {
        List<OBOXRef> xRefs = definition.getXRefs();
        Set<OWLAnnotation> xrefAnnotations = convertOBOXRefsToOWLAnnotations(projectId, xRefs);

        OWLAPIProject project = getProject(projectId);
        IRI subject = term.getIRI();
        OWLDataFactory df = project.getDataFactory();
        final IRI defIRI = getIRI(OBOFormatConstants.OboFormatTag.TAG_DEF);
        OWLAnnotationProperty defAnnotationProperty = df.getOWLAnnotationProperty(defIRI);
        OWLLiteral defLiteral = df.getOWLLiteral(definition.getDefinition());
        OWLAnnotationAssertionAxiom definitionAssertion = df.getOWLAnnotationAssertionAxiom(defAnnotationProperty, subject, defLiteral, xrefAnnotations);

        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        OWLOntology ont = project.getRootOntology();
        for (OWLAnnotationAssertionAxiom existingAx : ont.getAnnotationAssertionAxioms(subject)) {
            if (existingAx.getProperty().getIRI().equals(defIRI)) {
                changes.add(new RemoveAxiom(ont, existingAx));
                Set<OWLAnnotation> nonXRefAnnotations = getAxiomAnnotationsExcludingXRefs(existingAx);
                OWLAxiom fullyAnnotatedDefinitionAssertion = definitionAssertion.getAnnotatedAxiom(nonXRefAnnotations);
                changes.add(new AddAxiom(ont, fullyAnnotatedDefinitionAssertion));
            }
        }
        if (changes.isEmpty()) {
            // New
            changes.add(new AddAxiom(ont, definitionAssertion));
        }
        UserId userId = getUserInSessionAndEnsureSignedIn();
        project.applyChanges(userId, changes, "Set term definition");
    }

    private Set<OWLAnnotation> getAxiomAnnotationsExcludingXRefs(OWLAnnotationAssertionAxiom existingAx) {
        Set<OWLAnnotation> annotationsToCopy = new HashSet<OWLAnnotation>();
        for (OWLAnnotation existingAnnotation : existingAx.getAnnotations()) {
            if (!isXRefProperty(existingAnnotation.getProperty())) {
                annotationsToCopy.add(existingAnnotation);
            }
        }
        return annotationsToCopy;
    }

    private Set<OWLAnnotation> convertOBOXRefsToOWLAnnotations(ProjectId projectId, List<OBOXRef> xRefs) {
        Set<OWLAnnotation> xrefAnnotations = new HashSet<OWLAnnotation>();
        for (OBOXRef xref : xRefs) {
            if (!xref.isEmpty()) {
                OWLAnnotation xrefAnnotation = convertXRefToAnnotation(projectId, xref);
                xrefAnnotations.add(xrefAnnotation);
            }
        }
        return xrefAnnotations;
    }

    private String escapeSpaces(String s) {
        return s.replace(" ", "%20");
    }


    private OWLAnnotation convertXRefToAnnotation(ProjectId projectId, OBOXRef xref) {
        OWLAPIProject project = getProject(projectId);
        OWLDataFactory df = project.getDataFactory();
        OWLAnnotationProperty xrefAnnotationProperty = df.getOWLAnnotationProperty(getXRefPropertyIRI());
        String oboId = xref.toOBOId();
        Set<OWLAnnotation> descriptionAnnotations;
        if (xref.getDescription().isEmpty()) {
            descriptionAnnotations = Collections.emptySet();
        }
        else {
            OWLAnnotation descriptionAnnotation = df.getOWLAnnotation(df.getRDFSLabel(), df.getOWLLiteral(xref.getDescription()));
            descriptionAnnotations = Collections.singleton(descriptionAnnotation);
        }
        return df.getOWLAnnotation(xrefAnnotationProperty, df.getOWLLiteral(oboId), descriptionAnnotations);
    }

    private List<OBOXRef> getXRefs(OWLAnnotationAssertionAxiom annotationAssertion) {
        List<OBOXRef> result = new ArrayList<OBOXRef>();
        for (OWLAnnotation annotation : annotationAssertion.getAnnotations()) {
            if (isXRefProperty(annotation.getProperty())) {
                OBOXRef oboxRef = convertAnnotationToXRef(annotation, annotation.getAnnotations());
                if (oboxRef != null) {
                    result.add(oboxRef);
                }
            }
        }
        return result;
    }

    private OBOXRef convertAnnotationToXRef(final OWLAnnotation annotation, final Set<OWLAnnotation> annoAnnos) {


        return annotation.getValue().accept(new OWLAnnotationValueVisitorEx<OBOXRef>() {
            public OBOXRef visit(org.semanticweb.owlapi.model.IRI iri) {
                String description = "";
                for (OWLAnnotation anno : annoAnnos) {
                    if (anno.getProperty().isLabel()) {
                        description = anno.getValue().accept(new OWLAnnotationValueVisitorEx<String>() {
                            public String visit(org.semanticweb.owlapi.model.IRI iri) {
                                return iri.toString();
                            }

                            public String visit(OWLAnonymousIndividual individual) {
                                return individual.toString();
                            }

                            public String visit(OWLLiteral literal) {
                                return literal.getLiteral();
                            }
                        });
                        break;
                    }
                }
                return toOBOXRef(iri.toString(), description);
            }

            public OBOXRef visit(OWLAnonymousIndividual individual) {
                return toOBOXRef(individual.getID().getID(), "");
            }

            public OBOXRef visit(OWLLiteral literal) {
                String description = "";
                for (OWLAnnotation anno : annoAnnos) {
                    if (anno.getProperty().isLabel()) {
                        description = anno.getValue().accept(new OWLAnnotationValueVisitorEx<String>() {
                            public String visit(org.semanticweb.owlapi.model.IRI iri) {
                                return iri.toString();
                            }

                            public String visit(OWLAnonymousIndividual individual) {
                                return individual.toString();
                            }

                            public String visit(OWLLiteral literal) {
                                return literal.getLiteral();
                            }
                        });
                        break;
                    }
                }
                return toOBOXRef(literal.getLiteral(), description);
            }
        });
    }

    private static Pattern SEPARATOR_PATTERN = Pattern.compile("([^#_|_]+)(#_|_)(.+)");

    private OBOXRef toOBOXRef(String value, String description) {
        // Need to peel apart the ID
        if (value.startsWith(OBOPrefix.OBO.getPrefix())) {
            String localValue = value.substring(OBOPrefix.OBO.getPrefix().length());
            Matcher matcher = SEPARATOR_PATTERN.matcher(localValue);
            if (matcher.matches()) {
                String dbname = unescapeSpaces(matcher.group(1));
                String dbid = matcher.group(3);
                return new OBOXRef(dbname, dbid, description);
            }
            else {
                return new OBOXRef("", value, description);
            }
        }
        else {
            final int nameIdSeparatorIndex = value.indexOf(':');
            if (nameIdSeparatorIndex != -1) {
                return new OBOXRef(value.substring(0, nameIdSeparatorIndex), value.substring(nameIdSeparatorIndex + 1), description);
            }
            return new OBOXRef("", value, description);
        }
    }

    private String unescapeSpaces(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("%20", " ");
    }


    private String toOBOId(org.semanticweb.owlapi.model.IRI iri) {
        String value = iri.toString();
        String localPart = "";
        if (value.startsWith(OBOPrefix.OBO.getPrefix())) {
            localPart = value.substring(OBOPrefix.OBO.getPrefix().length());
        }
        else if (value.startsWith(OBOPrefix.OBO_IN_OWL.getPrefix())) {
            localPart = value.substring(OBOPrefix.OBO_IN_OWL.getPrefix().length());

        }
        else if (value.startsWith(OBOPrefix.IAO.getPrefix())) {
            localPart = value.substring(OBOPrefix.IAO.getPrefix().length());
        }
        else {
            String fragment = iri.getFragment();
            if (fragment != null) {
                localPart = fragment;
            }
            else {
                localPart = value;
            }
        }
        Matcher matcher = SEPARATOR_PATTERN.matcher(localPart);
        if (matcher.matches()) {
            StringBuilder sb = new StringBuilder();
            sb.append(matcher.group(1));
            sb.append(":");
            sb.append(matcher.group(3));
            return sb.toString();
        }
        else {
            return value;
        }
    }

    private OWLAPIProject getProject(ProjectId projectId) {
        OWLAPIProjectManager pm = OWLAPIProjectManager.getProjectManager();
        return pm.getProject(projectId);
    }

    public Collection<OBOTermSynonym> getSynonyms(ProjectId projectId, OWLEntity term) {
        Set<OBOTermSynonym> result = new HashSet<OBOTermSynonym>();
        OWLAPIProject project = getProject(projectId);
        for (OWLOntology ontology : project.getRootOntology().getImportsClosure()) {
            Set<OWLAnnotationAssertionAxiom> annotationAssertionAxioms = ontology.getAnnotationAssertionAxioms(term.getIRI());
            for (OWLAnnotationAssertionAxiom ax : annotationAssertionAxioms) {
                OBOTermSynonymScope synonymScope = getSynonymScope(ax);
                if (synonymScope != null) {
                    OBOTermSynonym termSynonym = new OBOTermSynonym(getXRefs(ax), getStringValue(ax), synonymScope);
                    result.add(termSynonym);
                }

            }
        }

        return result;
    }

    public void setSynonyms(ProjectId projectId, OWLEntity term, Collection<OBOTermSynonym> synonyms) throws NotSignedInException {
        org.semanticweb.owlapi.model.IRI subject = term.getIRI();
        OWLAPIProject project = getProject(projectId);
        OWLOntology rootOntology = project.getRootOntology();
        OWLDataFactory df = project.getDataFactory();
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();

        for (OWLAnnotationAssertionAxiom ax : project.getRootOntology().getAnnotationAssertionAxioms(subject)) {
            if (getSynonymScope(ax) != null) {
                changes.add(new RemoveAxiom(rootOntology, ax));
            }
        }

        for (OBOTermSynonym synonym : synonyms) {
            OWLAnnotationProperty synonymProperty = getSynonymAnnoationProperty(df, synonym.getScope());
            OWLLiteral synonymNameLiteral = df.getOWLLiteral(synonym.getName());
            Set<OWLAnnotation> synonymXRefs = convertOBOXRefsToOWLAnnotations(projectId, synonym.getXRefs());
            OWLAnnotationAssertionAxiom synonymAnnotationAssertion = df.getOWLAnnotationAssertionAxiom(synonymProperty, subject, synonymNameLiteral, synonymXRefs);
            changes.add(new AddAxiom(rootOntology, synonymAnnotationAssertion));
        }


        project.applyChanges(getUserInSessionAndEnsureSignedIn(), changes, "Set synonym");
    }

    public OWLAnnotationProperty getSynonymAnnoationProperty(OWLDataFactory df, OBOTermSynonymScope scope) {
        switch (scope) {
            case EXACT:
                return df.getOWLAnnotationProperty(getIRI(OBOFormatConstants.OboFormatTag.TAG_EXACT));
            case NARROWER:
                return df.getOWLAnnotationProperty(getIRI(OBOFormatConstants.OboFormatTag.TAG_NARROW));
            case BROADER:
                return df.getOWLAnnotationProperty(getIRI(OBOFormatConstants.OboFormatTag.TAG_BROAD));
            case RELATED:
                return df.getOWLAnnotationProperty(getIRI(OBOFormatConstants.OboFormatTag.TAG_RELATED));
            default:
                throw new RuntimeException("Unknown synonym scope: " + scope);
        }
    }


    public OBOTermSynonymScope getSynonymScope(OWLAnnotationAssertionAxiom ax) {
        IRI iri = ax.getProperty().getIRI();

        if (isExactSynonymIRI(iri)) {
            return OBOTermSynonymScope.EXACT;
        }
        else if (isRelatedSynonymIRI(iri)) {
            return OBOTermSynonymScope.RELATED;
        }
        else if (isNarrowSynonymIRI(iri)) {
            return OBOTermSynonymScope.NARROWER;
        }
        else if (isBroadSynonymIRI(iri)) {
            return OBOTermSynonymScope.BROADER;
        }
        else {
            return null;
        }

    }

    private boolean isBroadSynonymIRI(IRI iri) {
        return isOBOIRI(iri, OBOFormatConstants.OboFormatTag.TAG_BROAD);
    }

    private boolean isNarrowSynonymIRI(IRI iri) {
        return isOBOIRI(iri, OBOFormatConstants.OboFormatTag.TAG_NARROW);
    }

    private boolean isRelatedSynonymIRI(IRI iri) {
        return isOBOIRI(iri, OBOFormatConstants.OboFormatTag.TAG_RELATED);
    }

    private boolean isExactSynonymIRI(IRI iri) {
        return isOBOIRI(iri, OBOFormatConstants.OboFormatTag.TAG_EXACT);
    }

    private boolean isOBOIRI(IRI iriToCheck, OBOFormatConstants.OboFormatTag tag) {
        final Obo2OWLConstants.Obo2OWLVocabulary obo2OWLVocab = Obo2OWLConstants.getVocabularyObj(tag.getTag());
        return obo2OWLVocab != null && obo2OWLVocab.getIRI().equals(iriToCheck);
    }

    private IRI getIRI(OBOFormatConstants.OboFormatTag tag) {
        return Obo2OWLConstants.getVocabularyObj(tag.getTag()).getIRI();

//        return Obo2OWLConstants.getVocabularyObj(vocabulary.getName()).getIRI();
    }

    public OBOTermRelationships getRelationships(ProjectId projectId, OWLClass term) {
        OWLAPIProject project = getProject(projectId);
        OWLClass cls = project.getDataFactory().getOWLClass(term.getIRI());
        Set<OWLSubClassOfAxiom> subClassOfAxioms = project.getRootOntology().getSubClassAxiomsForSubClass(cls);
        Set<OBORelationship> rels = new HashSet<OBORelationship>();
        for (OWLSubClassOfAxiom ax : subClassOfAxioms) {
            Set<OWLObjectSomeValuesFrom> relationships = new HashSet<OWLObjectSomeValuesFrom>();
            Set<OWLClassExpression> conjuncts = ax.getSuperClass().asConjunctSet();
            for (OWLClassExpression conjunct : conjuncts) {
                if (conjunct instanceof OWLObjectSomeValuesFrom) {
                    OWLObjectSomeValuesFrom svf = (OWLObjectSomeValuesFrom) conjunct;
                    if (!svf.getProperty().isAnonymous() && !svf.getFiller().isAnonymous()) {
                        relationships.add((OWLObjectSomeValuesFrom) conjunct);
                    }
                }
            }
            if (relationships.size() == conjuncts.size()) {
                for (OWLObjectSomeValuesFrom rel : relationships) {
                    OWLObjectPropertyData property = toData(rel.getProperty().asOWLObjectProperty(), project);
                    OWLClassData filler = toData(rel.getFiller().asOWLClass(), project);
                    OBORelationship oboRel = new OBORelationship(property, filler);
                    rels.add(oboRel);
                }
            }
        }
        return new OBOTermRelationships(rels);
    }

    public void setRelationships(ProjectId projectId, OWLClass lastEntity, OBOTermRelationships relationships) {
        ensureSignedIn();
        if (relationships == null) {
            throw new NullPointerException("relationships must not be null");
        }
        OWLAPIProject project = getProject(projectId);

        OWLDataFactory dataFactory = project.getDataFactory();

        Set<OWLObjectSomeValuesFrom> superClsesToSet = new HashSet<OWLObjectSomeValuesFrom>();
        for (OBORelationship relationship : relationships.getRelationships()) {
            OWLObjectSomeValuesFrom someValuesFrom = toSomeValuesFrom(dataFactory, relationship);
            superClsesToSet.add(someValuesFrom);
        }


        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();

        OWLOntology ontology = project.getRootOntology();
        OWLClass cls = dataFactory.getOWLClass(lastEntity.getIRI());
        Set<OWLObjectSomeValuesFrom> existingSuperClsesToReplace = new HashSet<OWLObjectSomeValuesFrom>();
        for (OWLSubClassOfAxiom ax : ontology.getSubClassAxiomsForSubClass(cls)) {
            if (ax.getSuperClass() instanceof OWLObjectSomeValuesFrom) {
                OWLObjectSomeValuesFrom existing = (OWLObjectSomeValuesFrom) ax.getSuperClass();
                existingSuperClsesToReplace.add(existing);
            }
        }
        // What's changed?

        StringBuilder description = new StringBuilder();
        for (OWLObjectSomeValuesFrom toReplace : existingSuperClsesToReplace) {
            if (!superClsesToSet.contains(toReplace)) {
                // Was there but not any longer
                changes.add(new RemoveAxiom(ontology, dataFactory.getOWLSubClassOfAxiom(cls, toReplace)));
                description.append("Removed ");
                description.append(project.getRenderingManager().getBrowserText(toReplace.getProperty()));
                description.append(" relationship to ");
                description.append(project.getRenderingManager().getBrowserText(toReplace.getFiller()));
                description.append("    ");
            }
        }
        // What do we add?
        for (OWLObjectSomeValuesFrom toSet : superClsesToSet) {
            if (!existingSuperClsesToReplace.contains(toSet)) {
                // Not already there - we're adding it.
                changes.add(new AddAxiom(ontology, dataFactory.getOWLSubClassOfAxiom(cls, toSet)));
                description.append("Added ");
                description.append(project.getRenderingManager().getBrowserText(toSet.getProperty()));
                description.append(" relationship to ");
                description.append(project.getRenderingManager().getBrowserText(toSet.getFiller()));
                description.append("    ");
            }
        }


        if (!changes.isEmpty()) {
            UserId userId = getUserInSessionAndEnsureSignedIn();
            project.applyChanges(userId, changes, "Edited relationship values: " + description.toString());
        }

    }

    private OWLObjectSomeValuesFrom toSomeValuesFrom(OWLDataFactory dataFactory, OBORelationship relationship) {
        OWLObjectProperty property = relationship.getRelation().getEntity();
        OWLObjectProperty owlObjectProperty = dataFactory.getOWLObjectProperty(property.getIRI());
        OWLClass filler = relationship.getValue().getEntity();
        OWLClass owlCls = dataFactory.getOWLClass(filler.getIRI());
        return dataFactory.getOWLObjectSomeValuesFrom(owlObjectProperty, owlCls);
    }

    public OBOTermCrossProduct getCrossProduct(ProjectId projectId, OWLClass term) {
        OWLAPIProject project = getProject(projectId);
        OWLDataFactory df = project.getDataFactory();
        OWLClass cls = toOWLClass(df, term);
        OWLEquivalentClassesAxiom axiom = getCrossProductEquivalentClassesAxiom(project.getRootOntology(), cls);
        if (axiom == null) {
            return OBOTermCrossProduct.emptyOBOTermCrossProduct();
        }

        Set<OWLObjectSomeValuesFrom> relationships = new HashSet<OWLObjectSomeValuesFrom>();
        OWLClass genus = null;
        for (OWLClassExpression operand : axiom.getClassExpressionsMinus(cls)) {
            Set<OWLClassExpression> conjuncts = operand.asConjunctSet();
            for (OWLClassExpression conjunct : conjuncts) {
                if (conjunct instanceof OWLObjectSomeValuesFrom) {
                    OWLObjectSomeValuesFrom svf = (OWLObjectSomeValuesFrom) conjunct;
                    if (!svf.getProperty().isAnonymous() && !svf.getFiller().isAnonymous()) {
                        relationships.add((OWLObjectSomeValuesFrom) conjunct);
                    }
                }
                else if (conjunct instanceof OWLClass) {
                    genus = (OWLClass) conjunct;
                }
            }
        }
        Set<OBORelationship> discriminatingRelationships = new HashSet<OBORelationship>();
        OWLClassData visualCls = null;
        if (genus != null) {
            visualCls = toData(genus, project);
        }

        for (OWLObjectSomeValuesFrom rel : relationships) {
            OWLObjectPropertyData property = toData(rel.getProperty().asOWLObjectProperty(), project);
            OWLClassData filler = toData(rel.getFiller().asOWLClass(), project);
            OBORelationship oboRel = new OBORelationship(property, filler);
            discriminatingRelationships.add(oboRel);
        }
        return new OBOTermCrossProduct(visualCls, new OBOTermRelationships(discriminatingRelationships));

    }

    /**
     * Gets an equivalent classes axiom that corresponds to an OBO Cross Product.  An equivalent classes axiom AX
     * corresponds to a cross product for a class C if AX contains C as an operand, and AX contains one other class
     * which is either an ObjectSomeValuesFrom restriction, or an intersection of ObjectSomeValuesFrom restrictions
     * plus an optional named class.  i.e.   AX = EquivalentClasses(C ObjectIntersectionOf(A
     * ObjectSomeValuesFrom(..)...
     * ObjectSomeValuesFrom(..))
     * @param ontology The ontology in which to search
     * @param cls The subject of the cross product
     * @return An {@link OWLEquivalentClassesAxiom} that corresponds to a cross product for the class, or
     *         <code>null</code> if the ontology doesn't contain an equivalent classes axiom that corresponds to a
     *         cross
     *         product.
     */
    public OWLEquivalentClassesAxiom getCrossProductEquivalentClassesAxiom(OWLOntology ontology, OWLClass cls) {
        Set<OWLEquivalentClassesAxiom> candidates = new TreeSet<OWLEquivalentClassesAxiom>();
        for (OWLEquivalentClassesAxiom axiom : ontology.getEquivalentClassesAxioms(cls)) {
            Set<OWLClassExpression> equivalentClasses = axiom.getClassExpressionsMinus(cls);
            int namedCount = 0;
            int someValuesFromCount = 0;
            int otherCount = 0;
            for (OWLClassExpression operand : equivalentClasses) {
                for (OWLClassExpression ce : operand.asConjunctSet()) {
                    if (ce instanceof OWLClass) {
                        namedCount++;
                    }
                    else if (ce instanceof OWLObjectSomeValuesFrom) {
                        OWLObjectSomeValuesFrom svf = (OWLObjectSomeValuesFrom) ce;
                        if (!svf.getProperty().isAnonymous() && !svf.getFiller().isAnonymous()) {
                            someValuesFromCount++;
                        }
                    }
                    else {
                        otherCount++;
                    }
                }
            }
            if (namedCount <= 1 && someValuesFromCount > 0 && otherCount == 0) {
                candidates.add(axiom.getAxiomWithoutAnnotations());
            }
        }
        if (candidates.isEmpty()) {
            return null;
        }
        if (candidates.size() == 1) {
            return candidates.iterator().next();
        }
        // More than one
        // Return the first one (they are ordered by the OWLObject comparator, so for a given set of class expression this
        // is consistent
        return candidates.iterator().next();
    }


    public void setCrossProduct(ProjectId projectId, OWLClass term, OBOTermCrossProduct crossProduct) throws NotSignedInException {
        if (crossProduct == null) {
            throw new RuntimeException("crossProduct must not be null");
        }

        OWLAPIProject project = getProject(projectId);
        OWLDataFactory df = project.getDataFactory();

        Set<OWLClassExpression> intersectionOperands = new HashSet<OWLClassExpression>();

        OWLClassData visualGenus = crossProduct.getGenus();
        if (visualGenus != null) {
            OWLClass cls = toOWLClass(df, visualGenus.getEntity());
            intersectionOperands.add(cls);
        }

        for (OBORelationship relationship : crossProduct.getRelationships().getRelationships()) {
            OWLObjectSomeValuesFrom someValuesFrom = toSomeValuesFrom(df, relationship);
            intersectionOperands.add(someValuesFrom);
        }
        OWLObjectIntersectionOf intersectionOf = df.getOWLObjectIntersectionOf(intersectionOperands);

        OWLClass owlClass = toOWLClass(df, term);
        OWLEquivalentClassesAxiom newXPAxiom = df.getOWLEquivalentClassesAxiom(owlClass, intersectionOf);

        OWLOntology rootOntology = project.getRootOntology();
        OWLEquivalentClassesAxiom existingXPAxiom = getCrossProductEquivalentClassesAxiom(rootOntology, owlClass);

        UserId userId = getUserInSessionAndEnsureSignedIn();
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        changes.add(new AddAxiom(rootOntology, newXPAxiom));
        if (existingXPAxiom != null) {
            changes.add(new RemoveAxiom(rootOntology, existingXPAxiom));
        }
        project.applyChanges(userId, changes, "Set cross product values");

    }

    private OWLClass toOWLClass(OWLDataFactory dataFactory, OWLClass cls) {
        return dataFactory.getOWLClass(cls.getIRI());
    }


    private OWLClassData toData(OWLClass cls, OWLAPIProject project) {
        return new OWLClassData(cls, project.getRenderingManager().getBrowserText(cls));
    }

    private OWLObjectPropertyData toData(OWLObjectProperty property, OWLAPIProject project) {
        return new OWLObjectPropertyData(property, project.getRenderingManager().getBrowserText(property));
    }

}
